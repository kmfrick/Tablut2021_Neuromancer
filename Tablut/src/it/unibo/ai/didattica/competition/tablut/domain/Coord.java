package it.unibo.ai.didattica.competition.tablut.domain;

public class Coord {

	private int row;
	private int column;

	public Coord(int rowVal, int columnVal){
		row = rowVal;
		column = columnVal;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}