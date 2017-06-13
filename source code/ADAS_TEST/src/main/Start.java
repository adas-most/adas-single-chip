package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Modules.WebServerConnectModule;
import server.tcp.TCPserver;

// node
public class Start
{

	public static WebServerConnectModule WSCM = null;
	
	public static TCPserver TCPS = null;
	

	public static ExecutorService service = Executors.newCachedThreadPool();
	
	
	public static boolean logFlag = ini.set.LOG_FLAG_02;
	
	public static boolean check = false;
	
	
	public static int eventid = 1;
	
	public static boolean LoginMessageTransmitter_StopFlag = true;
	
	public static void main(String[] args) throws Exception
	{

		Start.WSCM = new WebServerConnectModule();

		// 開始監聽 HTTP 訊息 
		Start.WSCM.SetHTTPMonitorPort(ini.set.HTTPPort);
		Start.WSCM.HTTPServerOn();
		
		

		// 開始監聽 TCP 訊息 
		Start.TCPS = new TCPserver(ini.set.TCPPort);
		TCPS.start();


		//移除線程池 
		ScheduledExecutorService RemoveService = Executors.newScheduledThreadPool(1);
		
		RemoveService.scheduleAtFixedRate(new Runnable() 
		{
	        public void run() 
	        {


	        }
	    }, 0, 5, TimeUnit.SECONDS);
		
	}

}
