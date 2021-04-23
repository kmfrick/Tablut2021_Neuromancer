package it.unibo.ai.didattica.competition.tablut.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

import it.unibo.ai.didattica.competition.tablut.domain.*;

public class TablutMinimax extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {


	public TablutMinimax(Game<State, Action, State.Turn> game) {
		super(game, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 5);
	}

	@Override
	public Action makeDecision(State state) {
		System.out.println("[Neuromancer] makeDecision() called");
		return super.makeDecision(state);
	}
}
