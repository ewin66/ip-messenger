package ip;

import java.net.*;

public class ServerRequestCheck implements Runnable{
        public void run(){
//            ServerFile sf;
            Socket conn;
            try
            {
                ServerSocket welcome = new ServerSocket(6789);
            while(true)
            {   conn = welcome.accept();
                
                new Thread(new ServerFile(conn)).start();
                conn=null;
            }


            }
            catch(Exception e)
            {
                System.out.println("ServerRequestCheck   "+String.valueOf(e));
            }
    }
}
