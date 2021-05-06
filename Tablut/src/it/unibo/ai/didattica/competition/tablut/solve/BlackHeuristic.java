package it.unibo.ai.didattica.competition.tablut.solve;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.Coord;

class Side {//inizio dei lati del rombo
	 private int innerRow;
	 private int innerCol;
	 private int outerRow;
	 private int outerCol;
	public Side(int innerRow, int innerCol, int outerRow, int outerCol) {
		super();
		this.innerRow = innerRow;
		this.innerCol = innerCol;
		this.outerRow = outerRow;
		this.outerCol = outerCol;
	}
	public int getInnerRow() {
		return innerRow;
	}
	public int getInnerCol() {
		return innerCol;
	}
	public int getOuterRow() {
		return outerRow;
	}
	public int getOuterCol() {
		return outerCol;
	}
	 
}

public class BlackHeuristic extends Heuristic {

	private static double weightRhombus = 600;
	private static double weightRowColCover = 450;
	static double weightVictory = -5000;
	static double weightNumberOfWhites = -170;
	static double weightSurroundingBlackPawn = +100;
	static double weightNumberOfBlacks = 170;
	static double weightThreat = -190;
	static double weightScatter = 100;
	static Side sides[] = new Side[4];
	static {
		sides[0]=new Side(3, 2, 2, 1);// up sx
		sides[1]=new Side(6, 5, 7, 6);//down dx
		sides[2]=new Side(2,5, 1, 6);//up dx
		sides[3]=new Side(5,2 ,6, 1);//down sx
	}
	static  private  double calculateRhombus (State state) {
		Double tot=12.0; // normalizzare
		
		int col, row;
		int inPawns, outPawns;
		int points=0;
		int inc=-1; //direzione diagonale incremento della colonna
		for (int i = 0; i < 4; i++) {//for every side
			
			inPawns=0;
			outPawns=0;
			if(i>=2)//vanno nell'altra direzione
				inc=1;
			col=sides[i].getInnerCol();
			row=sides[i].getInnerRow();
			for(int j=0; j<2; j++) {//leggendario ciclo di 2 iterazioni
				if(state.getPawn(row, col)==State.Pawn.BLACK)
					inPawns++;
				col++;
				row+=inc;
			}
			col=sides[i].getOuterCol();
			row=sides[i].getOuterRow();
			for(int j=0; j<2; j++) {//leggendario ciclo di 2 iterazioni
				if(state.getPawn(row, col)==State.Pawn.BLACK)
					outPawns++;
				col++;
				row+=inc;
			}
			if(outPawns==2||inPawns==2)//lato chiuso
				points+=3;
			else if(inPawns==1||outPawns==1)//meta
				points+=1;
			//to-do si potrebbe aggiungere un controllo per vedere che siano non ci siano bianchi al di fuori	
		 }
		return points/tot;
	}
	static  private  double calculateRowColCover (State state) {//numero di colonne e righe coperte
		int points=0, j;
		double tot=18;
		for(int i=0; i<9; i++) {
			for( j=0; j<9&&state.getPawn(i, j)!=State.Pawn.BLACK; j++);
			if(j==9)
				points++;
		}
		for(int i=0; i<9; i++) {
			for( j=0; j<9&&state.getPawn(j, i)!=State.Pawn.BLACK; j++);
			if(j==9)
				points++;
		}
		return points/tot;
	}

	public static double eval(State state) {
		double result = 0.0;
/*var newCoord = state.getNewCoord();
    if (newCoord == null) {
      System.err.println("[Neuromancer] newCoord = null :(");
      throw new NullPointerException();
    }*/
    Coord kingPos = state.getKingPos();

    result = weightVictory * winWithAMove(state, kingPos) +
         weightSurroundingBlackPawn * calculateSurroundingBlackPawn(state, kingPos) +
         weightNumberOfBlacks * numberOfBlackPawn(state) +
         weightNumberOfWhites * numberOfWhitePawn(state) +
         //weightThreat * threat(state, newCoord) +
				 weightRhombus * calculateRhombus(state) + 
				 weightScatter * calculateScatter(state) + 
				 weightRowColCover * calculateRowColCover(state);


    return result;

	}

}
			 
