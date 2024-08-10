import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class memu extends JFrame implements ActionListener{
    // CONSTANTS
    final int WINDOW_WIDTH = 1080;
    final int WINDOW_HEIGHT = 720;
    final int WINDOW_X = 100;
    final int WINDOW_Y = 10;

    public memu() {

        // Menu Frame
        this.setTitle("CHESS");
        this.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setFocusable(true);
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(true);
        JButton buttonB = new JButton();
        buttonB.setBounds(10,10,100,100);
        buttonB.setText("Black");
        buttonB.addActionListener(this);
        this.add(buttonB);

        JButton buttonW = new JButton();
        buttonW.setBounds(10,150,100,100);
        buttonW.setText("White");
        buttonW.addActionListener(this);
        this.add(buttonW);
    }
    //start game
    void startGame(boolean color){
        new game(color);
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean color = e.getActionCommand()=="White";
        startGame(color);
    }
}
