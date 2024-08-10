import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class game extends JFrame {
    // CONSTANTS
    final int WINDOW_WIDTH = 1080;
    final int WINDOW_HEIGHT = 720;
    final int WINDOW_X = 100;
    final int WINDOW_Y = 10;
    


    final String HOST="localhost";
    final String DEFAULT_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    player cPlayer;
    // VARIABLES

    // IMPORT
    
    game(boolean color){
        try {
            this.cPlayer =new player(color,new Socket(HOST,8001));
            PrintWriter p =  new PrintWriter(cPlayer.socket.getOutputStream(),true);
            p.println(color);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameBoard board = new gameBoard(cPlayer,640, 640, DEFAULT_FEN);
        // Game Frame
        this.setTitle("CHESS");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setFocusable(true);
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(true);
        this.add(board);
    }
}