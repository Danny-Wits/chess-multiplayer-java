package server;

import java.net.Socket;

public class Player {
    public boolean color;
    public Socket socket;
    Player(boolean color, Socket socket) {
        this.color = color;
        this.socket = socket;
    } 
}

