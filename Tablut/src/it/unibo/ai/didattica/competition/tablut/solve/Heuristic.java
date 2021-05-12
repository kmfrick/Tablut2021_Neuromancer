package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import java.util.Deque;
import java.util.ArrayDeque;

public abstract class Heuristic {

	protected static final String weightFilePath = "/tmp/NeuroWeights.txt";
	
	protected static Deque moves;

	static int[][] kingScoreM = new int[9][9];
	static boolean[][] citadels=new boolean [9][9];
	static {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				kingScoreM[i][j] += Math.abs(j - 4) + Math.abs(i - 4);
				if (kingScoreM[i][j] > 4)
					kingScoreM[i][j] = 4;
			}
		}
		kingScoreM[2][6] = 5;
		kingScoreM[2][2] = 5;
		kingScoreM[6][6] = 5;
		kingScoreM[6][2] = 5;// 4 vie libere
		kingScoreM[1][3] = 1;// 2 lati contro gli accampamenti
		kingScoreM[1][5] = 1;
		kingScoreM[7][3] = 1;
		kingScoreM[7][5] = 1;
		kingScoreM[3][1] = 1;
		kingScoreM[3][7] = 1;
		kingScoreM[5][1] = 1;
		kingScoreM[5][7] = 1;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				citadels[i][j]=false;
			}
		}
		citadels[0][3]=true;
		citadels[0][4]=true;
		citadels[0][5]=true;
		citadels[1][4]=true;

		citadels[3][0]=true;
		citadels[4][0]=true;
		citadels[5][0]=true;
		citadels[4][1]=true;

		citadels[8][3]=true;
		citadels[8][4]=true;
		citadels[8][5]=true;
		citadels[7][4]=true;

		citadels[3][8]=true;
		citadels[4][8]=true;
		citadels[5][8]=true;
		citadels[4][7]=true;

		citadels[4][4]=true;//castle
	}

	protected static double calculateKingCentreDistance(Coord kingPos) {
		double result = 0.0;
		result += Math.abs(kingPos.getRow() - 4);
		result += Math.abs(kingPos.getColumn() - 4);
		return result;
	}

	protected static int calculateSurroundingBlackPawn(State state, Coord kingPos) {
		int result = 0;
		State.Pawn pawn = state.getPawn(kingPos.getRow() + 1, kingPos.getColumn());
		if (pawn == State.Pawn.BLACK || isCitadel(kingPos.getRow()+1, kingPos.getColumn()))
			result++;
		pawn = state.getPawn(kingPos.getRow() - 1, kingPos.getColumn());
		if (pawn == State.Pawn.BLACK || isCitadel(kingPos.getRow()-1 , kingPos.getColumn()))
			result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() + 1);
		if (pawn == State.Pawn.BLACK || isCitadel(kingPos.getRow(), kingPos.getColumn() + 1))
			result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() - 1);
		if (pawn == State.Pawn.BLACK || isCitadel(kingPos.getRow(), kingPos.getColumn() - 1))
			result++;
		if(calculateKingCentreDistance(kingPos)>=2)
			result*=2;
		// TODO: considerare il trono come casella "speciale", ovvero la casella (4,4)
		return result;
	}

	// metodo che valuta se l'avversario può mangiare il pezzo appena mosso con una
	// sola mossa successiva
	// newCoord = nuova coordinata del pezzo mosso
	protected static int threat(State state, Coord newCoord, State.Pawn player) {
		if (newCoord == null) return 0;
		var turnPlayer = state.getPawn(newCoord.getRow(), newCoord.getColumn());
		if (!turnPlayer.equals(player)) {
			return 0;
		}
		int result = 0;
		State.Pawn opponent;
		// State.Pawn pawnColumnBotton, pawnColumnUp, pawnRowRight, pawnRowLeft;
		if (player == State.Pawn.WHITE) { // il pezzo mosso Ã¨ bianco
			opponent = State.Pawn.BLACK;
		} else {
			opponent = State.Pawn.WHITE;
		}
		/*
		 * pawnColumnBotton = state.getPawn(newCoord.getRow() + 1,
		 * newCoord.getColumn()); pawnColumnUp = state.getPawn(newCoord.getRow() + -1,
		 * newCoord.getColumn()); pawnRowRight = state.getPawn(newCoord.getRow(),
		 * newCoord.getColumn() + 1); pawnRowLeft = state.getPawn(newCoord.getRow(),
		 * newCoord.getColumn() - 1); if(pawnColumnBotton == State.Pawn.BLACK){
		 * //controllo se nella stessa colonna e di una riga in basso c'Ã¨ adiacente un
		 * pezzo nero //TODO: implementare il controllo, a questo punto, di tutta la
		 * colonna State.Pawn pawnUp = state.checkUp(newCoord); if(pawnUp ==
		 * State.Pawn.BLACK) return 1; //TODO: per ogni casella sopra fare checkRight e
		 * checkLeft int column = newCoord.getColumn(); //... }
		 */

		/*
		 * Controllo se, muovendomi in una nuova posizione, ho al fianco un qualsiasi
		 * pedone del colore opposto.
		 *
		 * Logica: definisco un opponent e due versori. Per ogni iterazione sui versori:
		 * prima controllo che non ci siano opponents a sinistra o a destra sulla stessa
		 * riga, poi controllo che non ci siano opponents sopra o sotto sulla stessa
		 * colonna.
		 */
		int[] versors = { -1, 1 };
		for (int v : versors) {
			if (newCoord.getColumn() + v >= 0 && newCoord.getColumn() + v <= 8) {
				if (state.getPawn(newCoord.getColumn() + v, newCoord.getRow()) == opponent) {
					/*
					 * Non sarà mai i<0 && i>0, uno dei due termini dell'or restituisce sempre F.
					 * Per non implementare quattro controlli diversi ho riassunto in questi due or
					 */
					if ((v < 0 && state.checkRight(newCoord) == opponent)
							|| (v > 0 && state.checkLeft(newCoord) == opponent)) {
						// non so che valore dare: per ora segno 1 per dire che è grande
						result += 1;
					}
					Coord neighborColumn = new Coord(newCoord.getColumn() - v, newCoord.getRow());
					if (state.checkUp(neighborColumn) == opponent || state.checkBottom(neighborColumn) == opponent)
						result += 1;
				}
			}
			/*if (newCoord.getRow() + v >= 0 && newCoord.getRow() + v <= 8) {
				if (state.getPawn(newCoord.getColumn(), newCoord.getRow() + v) == opponent) {
					if ((v < 0 && (state.checkRight(newCoord) == opponent || state.checkBottom(newCoord) == opponent))
					if ((v < 0 && (state.checkLeft(newCoord) == opponent || state.checkBottom(newCoord) == opponent))
						|| (v > 0 && (state.checkLeft(newCoord) == opponent	|| state.checkUp(newCoord) == opponent))) {
						|| (v > 0 && (state.checkRight(newCoord) == opponent	|| state.checkUp(newCoord) == opponent))) {
						// non so che valore dare: per ora aumento di 1 per dire che è pericoloso
						result += 1;
					}
				}
			}*/
		}
		return result;
	}

	// dare un valore al peso molto alto, qui restituiamo semplicemente il
	// moltiplicatore
	protected static int winWithAMove(State state, Coord kingCoord) {
		int result = 0;
		int count = 0;
		if (state.getTurn() == State.Turn.WHITE) { // turno del bianco
			if (state.checkRight(kingCoord) == State.Pawn.EMPTY || state.checkLeft(kingCoord) == State.Pawn.EMPTY
					|| state.checkUp(kingCoord) == State.Pawn.EMPTY
					|| state.checkBottom(kingCoord) == State.Pawn.EMPTY) {
				return 1;
			} else
				return 0;
		} else { // turno del nero (?)
			/*if (state.checkRight(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkLeft(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkUp(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkBottom(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (count > 1)
				return 1;
			else*/
			return 0;
		}
	}

	protected static int numberOfBlackPawn(State state) {
		return state.getNumberOf(State.Pawn.BLACK);
	}
	protected static int numberOfWhitePawn(State state) {
		return state.getNumberOf(State.Pawn.WHITE);
	}




	protected static double calculateKingDistance(State state, State.Pawn color) {
		double kingDistance = 0;

		// Find the King. Should there be a function for this?
		int kingRow = -1;
		int kingCol = -1;

		for (int r = 0; r < state.getBoard().length && kingRow < 0; r++) {
			for (int c = 0; c < state.getBoard().length && kingCol < 0; c++) {
				if (state.getPawn(r, c).equalsPawn(State.Pawn.KING.toString())) {
					kingRow = r;
					kingCol = c;
				}
			}
		}
		for (int r = 0; r < state.getBoard().length; r++) {
			for (int c = 0; c < state.getBoard().length; c++) {
				if (state.getPawn(r, c).equalsPawn(color.toString())) {
					kingDistance += Math.abs(r - kingRow) + Math.abs(c - kingCol); // Manhattan distance
				}
			}
		}

		return kingDistance;

	}



	protected static double calculateScatter(State state) {
		return 0;
	}


	/*protected static double getKingScore(State state) {
		Coord kingCoor = state.getKingPos();
		return kingScoreM[kingCoor.getRow()][kingCoor.getColumn()];
	}*/
	protected static boolean isCitadel(int row, int col) {//castle  too
		return citadels[row][col];
	}

	public static void setWeightsAfterGenetic(){
		try {
			File myObj = new File(weightFilePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println("LETTA RIGA: " + data);
				System.out.println(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
}
