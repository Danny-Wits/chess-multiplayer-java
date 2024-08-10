import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.swing.JPanel;

public class gameBoard extends JPanel implements MouseListener {
    public int BOARD_X;
    public int BOARD_Y;
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    public String FEN = "";
    public Color DARK = Color.GRAY;
    public Color LIGHT = Color.WHITE;
    public Square[][] boardMap = new Square[8][8];
    public boolean isBoardSet = false;
    public Square selectedSquare = null;
    public String lastMove = "";
    public boolean turn ;
    public boolean listening=false;
    public player cPlayer;
   
    gameBoard(player p, int w, int h, String FEN) {
        this.cPlayer = p;
        this.turn=cPlayer.color;
        System.out.println(p.color);
        this.BOARD_X = 10;
        this.BOARD_Y = 10;
        this.BOARD_WIDTH = w;
        this.BOARD_HEIGHT = h;
        this.FEN = FEN;
        // Setting Panel
        this.setBounds(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
        this.setVisible(true);
        this.setDoubleBuffered(true);
        this.addMouseListener(this);
        this.setFocusable(true);
    }

    // !Setting Board
    void setBoard() {
        // Setting Squares
        if (this.cPlayer.color) {
            for (int rank = 0; rank < boardMap.length; rank++) {
                for (int file = 0; file < boardMap.length; file++) {
                    boardMap[rank][file] = new Square(null, 8 - rank, 1 + file, file, rank,
                            ((file + rank) % 2 == 0) ? LIGHT : DARK);
                }
            }
        } else {
            for (int rank = 0; rank < boardMap.length; rank++) {
                for (int file = 0; file < boardMap.length; file++) {
                    boardMap[rank][file] = new Square(null, 1 + rank, 8 - file, file, rank,
                            ((file + rank) % 2 == 0) ? LIGHT : DARK);
                }
            }
        }
        //setting network thread
        final Thread network = new Thread(new networkThread());
        network.start();
    }

    // !Renderer
    void render(Graphics2D canvas) {
        for (Square[] boxes : boardMap) {
            for (Square box : boxes) {
                box.draw(canvas);
            }
        }
    }

    // !Setting pieces
    void setPieces(String Fen) {
        int i = 0;
        int j = 0;
        char[] list = Fen.toCharArray();
        //System.out.print(Arrays.toString(list));
        if (!cPlayer.color) {
            char[] list1 = new char[list.length];
            int l = 0;
            for (int k = list.length - 1; k >= 0; k--) {
                list1[l] = list[k];
                l++;
            }
            list = list1;
        }
        System.out.print(Arrays.toString(list));

        for (char c : list) {
            if (Character.isAlphabetic(c)) {
                boardMap[i][j].cPiece = new piece(c);
                j++;
            } else if (Character.isDigit(c)) {
                int add = (int) c - 48;
                // System.out.println(add);
                j += add;
            } else if (c == '/') {
                j = 0;
                i++;
            } else if (c == ' ') {
                break;
            }
        }
    }
   
    // !Drawing the board
    @Override
    public void paint(Graphics g) {
        Graphics2D canvas = (Graphics2D) g;
        canvas.setColor(Color.BLACK);
        canvas.fillRect(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
        if (!isBoardSet) {
            setBoard();
            isBoardSet = true;
            setPieces(this.FEN);
        }
        render(canvas);
       
    }
   
    // !Square
    public class Square {
        public piece cPiece = null;
        public char File;
        public int Rank;
        public int x;
        public int y;
        public boolean isEmpty = true;
        public Color color;
        public int size = BOARD_WIDTH / 8;
        public boolean selected = false;

        Square(piece cPiece, int rank, int file, int x, int y, Color color) {
            this.cPiece = cPiece;
            this.Rank = rank;
            this.File = intToFile(file);
            this.x = x;
            this.y = y;
            this.color = color;
            // System.out.println(this.getLocation());
        }

        // ! Rendering Box
        void draw(Graphics2D canvas) {
            int x = size * this.x;
            int y = size * this.y;
            // filling square
            canvas.setColor(color);
            if (selected) {
                canvas.setColor(color.darker());
            }
            canvas.fillRect(x, y, size, size);
            // numbering the square
            canvas.setColor(Color.BLACK);
            canvas.setFont(new Font("DIGIFACE", 0, 12));
            canvas.drawString(getLocation(), x, y + 75);
            // !drawing pieces
            if (cPiece != null) {
                canvas.drawImage(cPiece.img, x, y, size - 10, size - 10, null);
            }
            // debug

        }

        // helpful functions
        int FileInInt() {
            return (int) File - 96;
        }

        char intToFile(int x) {
            return (char) (x + 96);
        }

        String getLocation() {
            return Character.toString(File).toLowerCase() + Integer.toString(Rank);
        }
    }

    //!move piece
    void move(String m){
        char []mArray=m.toCharArray();
        String from =((Character)mArray[0]).toString()+((Character)mArray[1]).toString();
        System.out.println(from);
        String to= ((Character)mArray[3]).toString()+((Character)mArray[4]).toString();
        System.out.println(to);
        piece temp=null;
        int x=0,y=0;
        for (int rank = 0; rank < boardMap.length; rank++) {
            for (int file = 0; file < boardMap.length; file++) {
                if(from.equals(boardMap[rank][file].getLocation())){
                    System.out.println("found from");
                    temp=boardMap[rank][file].cPiece;
                    boardMap[rank][file].cPiece=null;
                }
                else if(to.equals(boardMap[rank][file].getLocation())){
                   System.out.println("found to");
                   x=rank;
                   y=file;
                }
            }
        }
        boardMap[x][y].cPiece=temp;
        repaint();
    }

    // !INPUT HANDLING
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.turn) {
            int i = e.getY() / 80;
            int j = e.getX() / 80;
            if (i > 7 || j > 7) {
                return;
            }
            selectedSquare = boardMap[i][j];
            selectedSquare.selected = true;
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.turn) {
            int i = e.getY() / 80;
            int j = e.getX() / 80;
            if (i > 7 || j > 7) {
                return;
            }
            if (selectedSquare != null) {
                selectedSquare.selected = false;
                if (selectedSquare.cPiece != null && cPlayer.color == selectedSquare.cPiece.color
                        && selectedSquare != boardMap[i][j]) {
                    boardMap[i][j].cPiece = selectedSquare.cPiece;
                    lastMove = selectedSquare.cPiece.getName() + selectedSquare.getLocation() + " --> "
                            + boardMap[i][j].getLocation();
                    sendMove( selectedSquare.getLocation() + ":"+ boardMap[i][j].getLocation());
                    selectedSquare.cPiece = null;

                }
                selectedSquare = null;
            }
            selectedSquare = null;
            System.out.println(lastMove);
            repaint();
            if (!listening){
                final Thread network = new Thread(new networkThread());
                network.start();
            }  
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // Online
    class networkThread implements Runnable{
        public void run(){
            listen();
        }
    }
   
    void sendMove(String x){
        try{
            System.out.println("sent"+x);
            PrintWriter pw=new PrintWriter( this.cPlayer.socket.getOutputStream(),true);
            pw.println(x);
        }catch(IOException e){
            e.printStackTrace();
        }
        this.turn=false;
    }


    void listen(){
        listening=true;
         try {
            while(true){
                BufferedReader br = new BufferedReader(new InputStreamReader(this.cPlayer.socket.getInputStream()));
                String msg=br.readLine();
                if(msg.startsWith("turn")){
                    System.out.println(msg); 
                    msg=msg.replace("turn","");
                    System.out.println(msg);
                    move(msg);
                    turn=true;
                    break;
                }
                br.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        listening =false;
    }

}
