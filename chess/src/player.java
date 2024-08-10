import java.net.Socket;

public class player  {
    public boolean color;
    public Socket socket;
    public player(boolean color, Socket socket) {
        this.color = color;
        this.socket = socket;
    } 
}
