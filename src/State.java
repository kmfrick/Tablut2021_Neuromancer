public class State {

    public enum Turn{
        WHITE,
        BLACK
    }

    private char board[][];
    private Turn turn;
    private int xKing, yKing;

    public State(){
        board = new char[9][];
        turn = Turn.WHITE;
        board[0] = new char[]{'E','E','E','B','B','B','E','E','E'};
        board[1] = new char[]{'E','E','E','E','B','E','E','E','E'};
        board[2] = new char[]{'E','E','E','E','W','E','E','E','E'};
        board[3] = new char[]{'B','E','E','E','W','E','E','E','B'};
        board[4] = new char[]{'B','B','W','W','K','W','W','B','B'};
        board[5] = new char[]{'B','E','E','E','W','E','E','E','B'};
        board[6] = new char[]{'E','E','E','E','W','E','E','E','E'};
        board[7] = new char[]{'E','E','E','E','B','E','E','E','E'};
        board[8] = new char[]{'E','E','E','B','B','B','E','E','E'};
        xKing = 4; yKing = 4;
    }

    public State(char newBoard[][], Turn newTurn, int xNewKing, int yNewKing){
        board = newBoard;
        turn = newTurn;
        xKing = xNewKing;
        yKing = yNewKing;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public int getxKing() {
        return xKing;
    }

    public void setxKing(int xKing) {
        this.xKing = xKing;
    }

    public int getyKing() {
        return yKing;
    }

    public void setyKing(int yKing) {
        this.yKing = yKing;
    }

    public char getCell(int x, int y){
        return board[x][y];
    }

}
