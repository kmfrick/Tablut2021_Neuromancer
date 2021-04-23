package it.unibo.ai.didattica.competition.tablut.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.MinimaxSearch;

import it.unibo.ai.didattica.competition.tablut.domain.*;

public class TablutMinimax extends MinimaxSearch<State, Action, String> {

	Game g;

	public TablutMinimax(Game<State, Action, String> game) {
		super(game);
		g = game;
	}

	@Override
	public Action makeDecision(State state) {
		System.out.println("[Neuromancer] makeDecision() called");
	//	for (var a : g.getActions(state)) {
	//		System.out.println("[NeuroMinimax]" + a.toString());
	//	}
		return super.makeDecision(state);
	}
}
