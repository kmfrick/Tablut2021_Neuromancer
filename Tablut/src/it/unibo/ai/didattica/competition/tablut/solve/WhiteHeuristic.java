package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.Coord;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import java.util.StringTokenizer;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class WhiteHeuristic extends Heuristic {
	//best weights after genetic: 5000.0;100.0;200.0;-174.0;-243.0;-54.0;-27.0;64.0;
	static double weightVictory = 5000;
	static double weightKingPosition = 100;
	static double weightNumberOfWhites = 200;
	static double weightSurroundingBlackPawn = -174;
	static double weightNumberOfBlacks = -243;
	static double weightThreat = -54;
	static double weightWhiteNearKing = -27;
	static double weightBlackNearKing = 64;

	//Weights indexes
	final static int WEIGHT_VICTORY = 0;
	final static int KING_POSITION = 1;
	final static int NUMBER_WHITES = 2;
	final static int SURROUNDING_BLACKS = 3;
	final static int NUMBER_BLACKS = 4;
	final static int THREAT = 5;
	final static int BLACK_NEAR_KING = 6;
	final static int WHITE_NEAR_KING=7;

	protected static int threat(State state, Coord newCoord) {
		return threat(state, newCoord, State.Pawn.WHITE);
	}

	protected static double getKingScore(Coord kingCoor) {
		return kingScoreM[kingCoor.getRow()][kingCoor.getColumn()];
	}
	protected static double calculateWhiteNearKing(State state, Coord kingPos) {
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
		Coord newCoord = state.getNewCoord();
		Coord kingPos = state.getKingPos();

		result = weightVictory * winWithAMove(state, kingPos) +
				weightKingPosition * getKingScore(kingPos) +
				weightSurroundingBlackPawn * calculateSurroundingBlackPawn(state, kingPos) +
				weightNumberOfBlacks * numberOfBlackPawn(state) +
				weightNumberOfWhites * numberOfWhitePawn(state)+
				weightWhiteNearKing * calculateWhiteNearKing(state, kingPos) + 
				weightBlackNearKing * calculateBlackNearKing(state, kingPos) +
				weightThreat * threat(state, newCoord); 

		return result;

	}

	public static double[] getWhiteWeights() {
		double[] weights = {weightVictory, weightKingPosition, weightNumberOfWhites, weightSurroundingBlackPawn, weightNumberOfBlacks,  weightThreat,  weightBlackNearKing, weightWhiteNearKing};
		return weights;
	}

	public static void setWeightsAfterGenetic(){
		String data = "";
		try {
			File myObj = new File(weightFilePath);
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
			if(currentIndexOfWeightInEvolutionFile == NUMBER_WHITES)
				weightNumberOfWhites = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == SURROUNDING_BLACKS)
				weightSurroundingBlackPawn = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == NUMBER_BLACKS)
				weightNumberOfBlacks = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == THREAT)
				weightThreat = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == BLACK_NEAR_KING)
				weightBlackNearKing = Double.parseDouble(st.nextToken().trim());
			if(currentIndexOfWeightInEvolutionFile == WHITE_NEAR_KING)
				weightWhiteNearKing = Double.parseDouble(st.nextToken().trim());
			currentIndexOfWeightInEvolutionFile++;
		}
	}

}
