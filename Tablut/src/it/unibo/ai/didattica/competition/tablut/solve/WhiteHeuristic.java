package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class WhiteHeuristic extends Heuristic {
	static double weightVictory = 5000;
	static double weightKingPosition = 200;
	static double weightDistanceFromCentre = 250;
	static double weightNumberOfWhites = 170;
	static double weightSurroundingBlackPawn = -100;
	static double weightNumberOfBlacks = -170;
	static double weightThreat = -190;
	static double weightScatter = 100;
	protected static double getKingScore(State state) {
		Coord kingCoor = state.getKingPos();
		return kingScoreM[kingCoor.getRow()][kingCoor.getColumn()];
	}
	public static double eval(State state){
		double result = 0.0;
		/*Coord newCoord = state.getNewCoord();
		if (newCoord == null) {
			System.err.println("[Neuromancer] newCoord = null :(");
			throw new NullPointerException();
		}*/
		Coord kingPos = state.getKingPos();

		result = weightVictory * winWithAMove(state, kingPos) +
				 weightKingPosition * getKingScore(state) +
				 weightDistanceFromCentre * calculateKingCentreDistance(kingPos) +
				 weightSurroundingBlackPawn * calculateSurroundingBlackPawn(state, kingPos) +
				 weightNumberOfBlacks * numberOfBlackPawn(state) +
				 weightScatter * calculateScatter(state) + 
				 weightNumberOfWhites * numberOfWhitePawn(state)
				 //+ weightThreat * threat(state, newCoord);
				 	;
				 
		return result;

	}

}
