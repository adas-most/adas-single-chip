package main;


import tcp.TCPserver;
import tcp.configuration.Settings;

public class Start
{
    public static void main(String[] args)
    {
        int    connectPort = Settings.CONNECT_PORT;

        /***  server ***/
        TCPserver server = new TCPserver(connectPort);
        server.start();

        try 
        {
      	  
			Thread.sleep(500);
			
        }
        
        catch (InterruptedException e) 
        {
			e.printStackTrace();
        }
    }
}
