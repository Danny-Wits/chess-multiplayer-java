package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class serverGame implements Runnable{
    Player playerW;
    Player playerB;
    String msg="";
    PrintWriter pW;
    PrintWriter pB;
    BufferedReader bW ;
    BufferedReader bB ;
    @Override
    public void run() {
        communicate();
    }
    
    public serverGame(Player playerW, Player playerB) {
        this.playerW = playerW;
        this.playerB = playerB;
        System.out.println("clients connected");
        try{
            pW= new PrintWriter(playerW.socket.getOutputStream(),true);
            pB= new PrintWriter(playerB.socket.getOutputStream(),true);
            bW = new BufferedReader(new InputStreamReader(playerW.socket.getInputStream()));
            bB = new BufferedReader(new InputStreamReader(playerB.socket.getInputStream()));
        }catch(Exception e){
            System.err.println("what");
        }
            
     // sendMessage(playerW, "GameStarted" );
      //  sendMessage(playerB, "GameStarted" );  
    }
    private void communicate(){
        
        while (true) {
            try {
                    msg=readMessage(true);
                    if (msg!=null){
                        System.out.println(msg); 
                        sendMessage(false, "turn"+msg);
                    }
                    msg=readMessage(false);
                    if (msg!=null){
                        System.out.println(msg); 
                        sendMessage(true, "turn"+msg);
                    }
                    System.err.print(".");
                
            } catch (Exception e) {
             System.out.println(e.getMessage());
             gameOver();
             pW.close();
             pB.close();
             try{
                bB.close();
                bW.close();
             }catch(Exception c){
                System.out.println(c.getMessage());
             }   
             return;
            }       
        }   
    }    
    void sendMessage(boolean color,String msg)throws Exception{
            if (color){
             pW.println(msg);     
            }else{
                pB.println(msg);    
            }
    }
    String readMessage(boolean color)throws Exception{
           String x; 
            if (color){
           x= bW.readLine();     
           }else{
            x= bB.readLine();    
           }  
            return x;
    }
    void gameOver(){
      System.err.println("GameOver");
    }
    
    
}
