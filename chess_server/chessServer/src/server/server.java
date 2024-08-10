package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class server {
     Queue<Player> waitingPlayerW=new LinkedList<>();
     Queue<Player> waitingPlayerB=new LinkedList<>();
     ServerSocket sServer;
     server()throws IOException{
        sServer= new ServerSocket(8001);
        waitingForClient();
        sServer.close();
     }
     void waitingForClient()throws IOException{
        Socket player= sServer.accept();
        System.err.println(player.toString());
        BufferedReader b= new BufferedReader(new InputStreamReader(player.getInputStream()));
        String x = b.readLine();
        Player p = new Player(((x.equals("true"))?true:false), player);
        System.out.println(p.color);
        connected(p);
        waitingForClient();
     }
     void connected(Player player){
       if(player.color){
        if(waitingPlayerB.size()==0){
            waitingPlayerW.add(player);
            System.out.println(waitingPlayerW.size());
        }else{
            serverGame g= new serverGame(player,waitingPlayerB.remove()); 
            Thread gT= new Thread(g);
            gT.start();
        }
       }else if(!player.color){
        if(waitingPlayerW.size()==0){

            waitingPlayerB.add(player);
            System.out.println(waitingPlayerB.size());
        }else{
            serverGame g= new serverGame(waitingPlayerW.remove(),player); 
            Thread gT= new Thread(g);
            gT.start();
        }
       }
           
    }
        
}
