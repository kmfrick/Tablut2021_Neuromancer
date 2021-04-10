package it.unibo.ai.didattica.competition.tablut.domain;

public class WhiteHeuristic {
	double weightPieces = 1.0; 			// Number of white pieces on the board
	double weightRhombus = 1.0; 	// Avoid rhombus being blocked
	double weightKingDistance = 1.0; 				// Distance from the King
	double weightKingExit = 1.0; 		// How far the King is from the closest exit
	double weightKingThreat = -1.0; 	// How threatening is a Black piece for the King
	double weightScatter = 1.0; 		// Try not to move pieces that have just been moved

	private static double calculateKingCentreDistance(Coord kingPos){
		double result = 0.0;
		result += Math.abs(kingPos.getRow() - 4);
		result += Math.abs(kingPos.getColumn() - 4);
		return result;
	}

	private static int calculateSurroundingBlackPawn(State state, Coord kingPos){
		int result = 0;
		State.Pawn pawn = state.getPawn(kingPos.getRow() + 1, kingPos.getColumn());
		if(pawn == State.Pawn.BLACK) result++;
		pawn = state.getPawn(kingPos.getRow() - 1, kingPos.getColumn());
		if(pawn == State.Pawn.BLACK) result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() + 1);
		if(pawn == State.Pawn.BLACK) result++;
		pawn = state.getPawn(kingPos.getRow(), kingPos.getColumn() - 1);
		if(pawn == State.Pawn.BLACK) result++;
		//TODO: considerare il trono come casella "speciale", ovvero la casella (4,4)
		return result;
	}

	//metodo che valuta se l'avversario può mangiare il pezzo appena mosso con una sola mossa successiva
	//newCoord = nuova coordinata del pezzo mosso
	private static int threat(State state, Coord newCoord){
		int result = 0;
		State.Pawn pawnColumnBotton, pawnColumnUp, pawnRowRight, pawnRowLeft;
		if(state.getPawn(newCoord.getRow(), newCoord.getColumn()) == State.Pawn.WHITE){ // il pezzo mosso è bianco
			pawnColumnBotton = state.getPawn(newCoord.getRow() + 1, newCoord.getColumn());
			pawnColumnUp = state.getPawn(newCoord.getRow() + -1, newCoord.getColumn());
			pawnRowRight = state.getPawn(newCoord.getRow(), newCoord.getColumn() + 1);
			pawnRowRight = state.getPawn(newCoord.getRow(), newCoord.getColumn() - 1);
			if(pawnColumnBotton == State.Pawn.BLACK){ //controllo se nella stessa colonna e di una riga in basso c'è adiacente un pezzo nero
				//TODO: implementare il controllo, a questo punto, di tutta la colonna
			}
		}
		return result;
	}

	//dare un valore al peso molto alto, qui restituiamo semplicemente il moltiplicatore
	private static int winWithAMove(State state, Coord kingCoord){
		int result = 0;
		int count = 0;
		if(state.getTurn() == State.Turn.WHITE){ //turno del bianco
			if(state.checkRight(kingCoord) == State.Pawn.EMPTY ||
					state.checkLeft(kingCoord) == State.Pawn.EMPTY ||
					state.checkUp(kingCoord) == State.Pawn.EMPTY ||
					state.checkBottom(kingCoord) == State.Pawn.EMPTY){
				return 1;
			} else return 0;
		} else{ //turno del nero (?)

		}


	}

	private static int numberOfBlackPawn(State state){
		return state.getNumberOf(State.Pawn.BLACK);
	}

	private static int numberOfWhitePawn(State state){
		return state.getNumberOf(State.Pawn.WHITE);
	}


		
	private static double calculateRhombusVal(State state) {

		final int rhombusRow[] = {}; // TODO: Fill
		final int rhombusCol[] = {};
		int blackPiecesOnRhombus = 0;
		for (int i = 0; i < rhombusRow.length; i++) {
			if (state.getPawn(rhombusRow[i], rhombusCol[i]).equalsPawn(State.Pawn.BLACK)) {
				blackPiecesOnRhombus++;
			}
		}

		// TODO: It should be *even worse* if there's a whole side blocked
		
		return blackPiecesOnRhombus;
	}

	private static double calculateKingDistance(State state, State.Pawn color) {
		double kingDistance = 0;

		// Find the King. Should there be a function for this?
		int kingRow = -1;
		int kingCol = -1;

		for (int r = 0; r < state.getBoard().length && kingRow < 0; r++) {
			for (int c = 0; c < state.getBoard().length && kingCol < 0; c++) {
				if (state.getPawn(r, c).equalsPawn(State.Pawn.KING)) {
					kingRow = r;
					kingCol = c;
				}
			}
		}
		for (int r = 0; r < state.getBoard().length; r++) {
			for (int c = 0; c < state.getBoard().length; c++) {
				if (state.getPawn(r, c).equalsPawn(color.toString())) {
					kingDistance += Math.abs(r - kingRow) + Math.abs(c - kingCol);    // Manhattan distance
				}
			}
		}

		return kingDistance;

	}

	private static double calculateKingExit(State state) {
		/*
		final int exitRow[] = {};
		final int exitColumn[] = {};
		// Find the King. Should there be a function for this?
				//yes, in the state class
		int kingRow = -1;
		int kingCol = -1;
		for (int r = 0; r < state.getBoard().length && kingRow < 0; r++) {
			for (int c = 0; c < state.getBoard().length && kingCol < 0; c++) {
				if (state.getPawn(r, c).equalsPawn(State.Pawn.KING)) {
					kingRow = r;
					kingCol = c;
				}
			}
		}*/ //implementata in classi Coord e State

		// If there are two free exits, and the King can reach the row or column of either in one move, White wins because Black can't block both
		// TODO: Implement this, and return an infinitely high heuristic for this.

		int closestExit = 0;
		double closestDist = 81;

		for (int i = 0; i < exitRow.length; i++) {
			double curDist = Math.abs(kingRow - exitRow[i]) + Math.abs(kingCol - exitCol[i]);
			if (curDist < closestDist) {
				closestExit = i;
				closestDist = curDist;
			}
		}
		return 1.0f / closestDist;
	}


	private static double calculateScatter(State state) {
		// TODO: Implement a penalty for moves which involve the last moved piece
	}

	public static double calculateHeuristic(State state) {
		double h = 0;

		int pieces = state.getNumberOf(State.Pawn.WHITE);
		double rhombus = calculateRhombusVal(state);
		double kingExit = calculateKingExit(state);
		double kingDistance = calculateKingDistance(state, State.Pawn.WHITE);
		double kingThreat = calculateKingThreat(state, State.Pawn.BLACK);
		double scatter = calculateScatter(state);

		h += weightPieces * pieces;
		h += weightRhombus * rhombus;
		h += weightKingExit * kingExit;
		h += weightKingDistance * kingDistance;
		h += weightKingThreat * kingThreat;
		h += weightScatter * scatter;

		return h;
	}

	public static double eval(State state, int depth){
		double result = 0.0;
		Coord kingPos = state.getKingPos();
		//other computations for other weights

		result += calculateKingCentreDistance(kingPos);

	}


}
