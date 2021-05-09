package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import java.util.StringTokenizer;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

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

	//Weights indexes
	final static int WEIGHT_VICTORY = 0;
	final static int KING_POSITION = 1;
	final static int DISTANCE_CENTRE = 2;
	final static int NUMBER_WHITES = 3;
	final static int SURROUNDING_BLACKS = 4;
	final static int NUMBER_BLACKS = 5;
	final static int THREAT = 6;
	final static int SCATTER = 7;
	final static int NEAR_KING = 8;

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
		//+ weightThreat * threat(state, newCoord); PERCHÈ È COMMENTATA QUESTA?

		;

		return result;

	}

	public static double[] getWhiteWeights() {
		double[] weights = {weightVictory, weightKingPosition, weightDistanceFromCentre, weightNumberOfBlacks, weightNumberOfWhites, weightSurroundingBlackPawn, weightThreat, weightScatter, weightNearKing};
		return weights;
	}

	public static void setWeightsAfterGenetic(){
		String data = "";
		try {
			File myObj = new File("/Users/antonyzappacosta/Desktop/filesForGenetic/evolution.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data = myReader.nextLine();
				System.out.println("LETTA RIGA: " + data);
				System.out.println(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		StringTokenizer st = new StringTokenizer(data, ";");
		int currentIndexOfWeightInEvolutionFile = 0;
		while (st.hasMoreTokens()) {
			if(currentIndexOfWeightInEvolutionFile == WEIGHT_VICTORY)
				weightVictory = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == KING_POSITION)
				weightKingPosition = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == DISTANCE_CENTRE)
				weightDistanceFromCentre = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == NUMBER_WHITES)
				weightNumberOfWhites = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == SURROUNDING_BLACKS)
				weightSurroundingBlackPawn = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == NUMBER_BLACKS)
				weightNumberOfBlacks = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == THREAT)
				weightThreat = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == SCATTER)
				weightScatter = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == NEAR_KING)
				weightNearKing = Double.parseDouble(st.nextToken().trim());
			currentIndexOfWeightInEvolutionFile++;
		}
	}

}
