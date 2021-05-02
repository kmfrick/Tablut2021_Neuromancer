package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class WhiteHeuristic extends Heuristic {

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
				 weightScatter * calculateScatter(state) + 
				 weightNumberOfWhites * numberOfWhitePawn(state) +
				 weightThreat * threat(state, newCoord);


		return result;

	}

}
