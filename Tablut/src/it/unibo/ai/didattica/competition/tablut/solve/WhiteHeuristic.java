public class WhiteHeuristic {
	double weightPieces = 1.0; 			// Number of white pieces on the board
	double weightRhombus = 1.0; 	// Avoid rhombus being blocked
	double weightKingDistance = 1.0; 				// Distance from the King
	double weightKingExit = 1.0; 		// How far the King is from the closest exit
	double weightKingThreat = -1.0; 	// How threatening is a Black piece for the King
	double weightScatter = 1.0; 		// Try not to move pieces that have just been moved

		
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
		for (int r = 0; r < state.getBoard().length; r++) {
			for (int c = 0; c < state.getBoard().length; c++) {
				if (state.getPawn(r, c).equalsPawn(color.toString())) {
					kingDistance += Math.abs(r - kingRow) + Math.abs(c - kingCol);	// Manhattan distance
			}
		}

		return kingDistance;

	}

	private static double calculateKingExit(State state) {
		final int exitRow[] = {};
		final int exitColumn[] = {};
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
		
		return 
	}


}
