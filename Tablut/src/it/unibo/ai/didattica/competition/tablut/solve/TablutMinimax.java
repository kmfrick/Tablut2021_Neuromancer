package it.unibo.ai.didattica.competition.tablut.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.MinimaxSearch;

import it.unibo.ai.didattica.competition.tablut.domain.*;

public class TablutMinimax extends MinimaxSearch<State, Action, String> {
	public TablutMinimax(Game<State, Action, String> game) {
		super(game);
	}
}
