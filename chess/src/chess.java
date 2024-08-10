public class chess {
    Square[][]board=new Square[8][8];
    chess(){

    }
    void setBoard(gameBoard.Square[][] gBoard){
        for (int i = 0; i < gBoard.length; i++) {
            for (int j = 0; j < gBoard.length; j++) {
                board[i][j]=new Square(gBoard[i][j].File, gBoard[i][j].Rank, gBoard[i][j].cPiece);
            }
        }
    }
    
    class Square{
        public char file;
        public int rank;
        public piece cPiece;
        public Square(char file, int rank, piece p) {
            this.file = file;
            this.rank = rank;
            this.cPiece = p;
        }
    }

    boolean move(String start, String end){
    return false;
    }
}
