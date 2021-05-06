package it.unibo.ai.didattica.competition.tablut.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

import it.unibo.ai.didattica.competition.tablut.domain.*;

public class TablutMinimax extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {
	

	public TablutMinimax(Game<State, Action, State.Turn> game) {
		super(game, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 9);
	}

	@Override
	public Action makeDecision(State state) {
		System.out.println("[Neuromancer] makeDecision() called");
		var ret = super.makeDecision(state);
		System.out.println("[Neuromancer] makeDecision() expanded " + getMetrics().get(METRICS_NODES_EXPANDED) + " nodes");
		System.out.println("[Neuromancer] makeDecision() went " + getMetrics().get(METRICS_MAX_DEPTH) + " levels deep");
		return ret;
	}
	@Override
	protected double eval(State state, State.Turn player) {
       if (game.isTerminal(state)) {
           return game.getUtility(state, player);
         } else {
        	 super.eval(state, player);
             return game.getUtility(state, player);
         }
	}
}
