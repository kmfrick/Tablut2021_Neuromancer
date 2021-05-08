package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class WhiteHeuristic extends Heuristic {
	static double weightVictory = 5000;
	static double weightKingPosition = 100;
	static double weightDistanceFromCentre = 0;
	static double weightNumberOfWhites = 200;
	static double weightSurroundingBlackPawn = -100;
	static double weightNumberOfBlacks = -170;
	static double weightThreat = -190;
	static double weightScatter = 100;
	static double weightNearKing =50;

	protected static double getKingScore(Coord kingCoor) {
		return kingScoreM[kingCoor.getRow()][kingCoor.getColumn()];
	}
	protected static double calculateNearKing(State state, Coord kingPos) {
		int res=0;
		for(int i=0; i<9; i++) {
			if(state.getPawn(kingPos.getRow()+1, i).equals(State.Pawn.WHITE))
				res++;
			if(state.getPawn(kingPos.getRow()-1, i).equals(State.Pawn.WHITE))
				res++;
			if(state.getPawn(i, kingPos.getColumn()+1).equals(State.Pawn.WHITE))
				res++;
			if(state.getPawn(i, kingPos.getColumn()-1).equals(State.Pawn.WHITE))
				res++;
		}
		return res;
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
				weightKingPosition * getKingScore(kingPos) +
				weightDistanceFromCentre * calculateKingCentreDistance(kingPos) +
				weightSurroundingBlackPawn * calculateSurroundingBlackPawn(state, kingPos) +
				weightNumberOfBlacks * numberOfBlackPawn(state) +
				weightScatter * calculateScatter(state) +
				weightNumberOfWhites * numberOfWhitePawn(state)+
				weightNearKing * calculateNearKing(state, kingPos)
		//+ weightThreat * threat(state, newCoord);

		;

		return result;

	}

	public static double[] getWhiteWeights() {
		double[] weights = {weightVictory, weightKingPosition, weightDistanceFromCentre, weightNumberOfBlacks, weightNumberOfWhites, weightSurroundingBlackPawn, weightThreat, weightScatter, weightNearKing};
		return weights;
	}

}
