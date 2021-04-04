package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class TablutActionsFunction {

	Game rules = new GameTablut();

	private boolean moveIsValid(State state, Action action) {
		State newState;
		try {
			newState = rules.checkMove(state, action);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	private boolean posIsValid(State state, int row, int col) {
		if (row < 0 || row >= state.getBoard().length) return false;
		if (col < 0 || col >= state.getBoard().length) return false;
		return true;
	}

	private List<Action> getMovesForColor(State state, State.Pawn color) throws IOException {
		int drow[] = {1, 0, -1, 0};
		int dcol[] = {0, 1, 0, -1};
		var possibleMoves = new ArrayList<Action>();
		int row = 0, col = 0;
		for (int c = 0; c < state.getNumberOf(color); c++) {
			// FIXME: Bugged while loop to find pawn position
			while (!state.getPawn(row, col).equalsPawn(color.toString())) {
				System.out.println("Row " + row + ", col " + col + " = " + state.getPawn(row, col));
				if (state.getPawn(row, col).equalsPawn(color.toString())) {
					System.out.println(state.getPawn(row, col) + " == " + color.toString());
				} else {
					System.out.println(state.getPawn(row, col) + " != " + color.toString());
				}
				col++;
				if (col == state.getBoard().length) { col = 0; row++; }
				if (row >= state.getBoard().length) throw new ArrayIndexOutOfBoundsException();
			}
			System.out.println(color + " pawn n. " + c + " is in pos. (" + row + ", " + col + ")");
			for (int i = 0; i < drow.length; i++) {
				for (int distance = 1; posIsValid(state, row + drow[i] * distance, col + dcol[i] * distance); distance++) {
					// (0, 0) = top left

					String from = state.getBox(row, col);
					String to = state.getBox(row + drow[i] * distance, col + dcol[i] * distance);
					var curAction = new Action(from, to, state.getTurn());
					if (moveIsValid(state, curAction)) {
						possibleMoves.add(curAction);
					} else {
						continue; // FIXME: Chesani will fail us if he sees this
					}
				}
			}
		}
		return possibleMoves;
	}

	public List<Action> getPossibleMoves(State state) throws IOException {
		System.out.println("getPossibleMoves() called on " + state.getTurn() + "'s turn");

		var possibleMoves = new ArrayList<Action>();

		if (state.getTurn() == State.Turn.WHITE) {
			// Generate moves for White
			possibleMoves.addAll(getMovesForColor(state, State.Pawn.WHITE));
			// Generate moves for King
			possibleMoves.addAll(getMovesForColor(state, State.Pawn.KING));
		} else {
			// Generate moves for Black
			possibleMoves.addAll(getMovesForColor(state, State.Pawn.BLACK));
		}

		return possibleMoves;
	}

}
