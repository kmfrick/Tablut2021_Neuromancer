package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class WhiteHeuristic {

	//new weights
	static double weightVictory = 5000;
	static double weightKingPosition = 200;
	static double weightDistanceFromCentre = 190;
	static double weightNumberOfWhites = 170;
	static double weightSurroundingBlackPawn = -100;
	static double weightNumberOfBlacks = -170;
	static double weightThreat = -190;



	static int[][] kingScoreM = new int[9][9];
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
	}

	private static double calculateKingCentreDistance(Coord kingPos) {
		double result = 0.0;
		result += Math.abs(kingPos.getRow() - 4);
		result += Math.abs(kingPos.getColumn() - 4);
		return result;
	}

	private static int calculateSurroundingBlackPawn(State state, Coord kingPos) {
		int result = 0;
		State.Pawn pawn = state.getPawn(kingPos.getRow() + 1, kingPos.getColumn());
		if (pawn == State.Pawn.BLACK)
			result++;
		pawn = state.getPawn(kingPos.getRow() - 1, kingPos.getColumn());
		if (pawn == State.Pawn.BLACK)
			result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() + 1);
		if (pawn == State.Pawn.BLACK)
			result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() - 1);
		if (pawn == State.Pawn.BLACK)
			result++;
		// TODO: considerare il trono come casella "speciale", ovvero la casella (4,4)
		return result;
	}

	// metodo che valuta se l'avversario puÃ² mangiare il pezzo appena mosso con una
	// sola mossa successiva
	// newCoord = nuova coordinata del pezzo mosso
	private static int threat(State state, Coord newCoord) {
		int result = 0;
		// State.Pawn pawnColumnBotton, pawnColumnUp, pawnRowRight, pawnRowLeft;
		if (state.getPawn(newCoord.getRow(), newCoord.getColumn()) == State.Pawn.WHITE) { // il pezzo mosso Ã¨ bianco
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
			State.Pawn opponent = State.Pawn.BLACK;
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
							// non so che valore dare: per ora segno 100 per dire che è grande
							result += 100;
						}
						Coord neighborColumn = new Coord(newCoord.getColumn() - v, newCoord.getRow());
						if (state.checkUp(neighborColumn) == opponent || state.checkBottom(neighborColumn) == opponent)
							result += 100;
					}
				}
				if (newCoord.getRow() + v >= 0 && newCoord.getRow() + v <= 8) {
					if (state.getPawn(newCoord.getColumn(), newCoord.getRow() + v) == opponent) {
						if ((v < 0 && (state.checkRight(newCoord) == opponent || state.checkBottom(newCoord) == opponent))
							|| (v > 0 && (state.checkLeft(newCoord) == opponent	|| state.checkUp(newCoord) == opponent))) {
							// non so che valore dare: per ora aumento di 100 per dire che è pericoloso
							result += 100;
						}
					}
				}
			}
		}
		return result;
	}

	// dare un valore al peso molto alto, qui restituiamo semplicemente il
	// moltiplicatore
	private static int winWithAMove(State state, Coord kingCoord) {
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
			if (state.checkRight(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkLeft(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkUp(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (state.checkBottom(kingCoord) == State.Pawn.EMPTY)
				count++;
			if (count > 1)
				return 1;
			else
				return 0;
		}
	}

	private static int numberOfBlackPawn(State state) {
		return state.getNumberOf(State.Pawn.BLACK);
	}
	private static int numberOfWhitePawn(State state) {
		return state.getNumberOf(State.Pawn.WHITE);
	}




	private static double calculateKingDistance(State state, State.Pawn color) {
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

	private static double calculateScatter(State state) {
		// TODO: Implement a penalty for moves which involve the last moved piece
		return 0.0;
	}

	private static double getKingScore(State state) {
		Coord kingCoor = state.getKingPos();
		return kingScoreM[kingCoor.getRow()][kingCoor.getColumn()];
	}

	public static double eval(State state){
		double result = 0.0;
		Coord newCoord = state.getNewCoord();
		if (newCoord == null) {
			System.err.println("[Neuromancer] newCoord = null :(");
			throw new NullPointerException();
		}
		Coord kingPos = state.getKingPos();

		result = weightVictory * winWithAMove(state, kingPos) +
				 weightKingPosition * getKingScore(state) +
				 weightDistanceFromCentre * calculateKingCentreDistance(kingPos) +
				 weightSurroundingBlackPawn * calculateSurroundingBlackPawn(state, kingPos) +
				 weightNumberOfBlacks * numberOfBlackPawn(state) +
				 weightNumberOfWhites * numberOfWhitePawn(state) +
				 weightThreat * threat(state, newCoord);


		return result;

	}

}
