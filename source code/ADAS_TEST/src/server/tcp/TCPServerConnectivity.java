package server.tcp;

import java.util.HashMap;
import java.util.Map;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import server.tcp.TCPclient;

public class TCPServerConnectivity
{
	private String connectHost = "192.168.43.37";
	private int connectPort = ini.set.TCPPort;
	
	private TCPclient TCPC = null;
	private Map<String, Object> request = null;
	private Map<String, Object> response = null;
	private boolean receiveState  = false;
	private int requestActionCode = 0;
    
    public void getFile(String fileName)
    {

        /*** client ***/
        
        //--------------- 示範要求檔案 -----------------------
        System.out.println("--------------- 示範要求檔案 -----------------------");

        request = new HashMap<String, Object>();
        
        requestActionCode = ini.set.ActionCode.getFile;
        
        request.put("action", requestActionCode);
        request.put("fileName", fileName);
            
        TCPC = new TCPclient(connectHost, connectPort);
        receiveState = TCPC.handleRequest(request);
        response = TCPC.getResponse();
        
        System.out.println("是否接收成功 : " + receiveState);
        System.out.println("收到的值 fileName : " + response.get("fileName"));
        System.out.println("收到的值 fileData : " + response.get("fileData"));

        //寫出檔案
        byte[] data = (byte[]) response.get("fileData");
		
        FileOutputStream OutStream = null;
        try 
		{
			OutStream = new FileOutputStream(ini.set.tempLocation + response.get("fileName"));
			OutStream.write(data);
		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
        
		finally
        {
			try
            {
				if(OutStream != null) { OutStream.close(); }
            }
			
			catch (IOException e)
	        {
	            e.printStackTrace();
	        }
        }
    }
        public void getDeviceState()
        {
        
        //--------------- 示範要求裝置狀態 -----------------------
        System.out.println("--------------- 示範要求裝置狀態 -----------------------");
        request = new HashMap<String, Object>();
        
        requestActionCode = ini.set.ActionCode.getDeviceState;
        request.put("action", requestActionCode);
        
        TCPC = new TCPclient(connectHost, connectPort);
        receiveState = TCPC.handleRequest(request);
        response = TCPC.getResponse();
        
        System.out.println("是否接收成功 : " + receiveState);
        System.out.println("收到的值  DeviceState : " + response.get("DeviceState"));
        
    }
}
