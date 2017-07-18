package server.web;


//********************************************************
//**  Version  : 1.01                                   **
//**  detail   : UI 溝通介面						    	**
//**  Engineer : 曾勖華	                                **
//**  Release Date  : 2017/02/09                        **
//**  Generate Date : 2017/02/09                        **
//********************************************************

import org.codehaus.jettison.json.JSONObject;

import ADAS.Data.Test.ConfirmUseModules;
import ADAS.Data.Test.Event;
import ADAS.Data.Test.ModifyModulesParameter;
import ADAS.Data.Test.Parameter;
import ADAS.Data.Test.Relate;
import ADAS.Data.Test.Sensor;
import ADAS.Data.Test.Treatment;
import ADAS.Data.Test.Type;
import ADAS.Modules.DataAccessModule;
import main.Start;
import server.tcp.TCPServerConnectivity;
import tool.ImageTranscoder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Node
public class HTTPServerConnectivity extends Thread 
{
	private boolean logFlag = ini.set.LOG_FLAG_01;
	
	private int port = ini.set.HTTPPort;
	
	private Socket serverSocket = null;
	private ServerSocket server = null;
		
	public static int ErrorCounter = 0;
	
	
	private ExecutorService WEBservice = Executors.newCachedThreadPool();
	
	public HTTPServerConnectivity(int _port)
	{
		this.port = _port;
	}
	
	public void run()
	{
		try
		{			
	        this.server = new ServerSocket(this.port);
		    
	        if(this.logFlag) System.out.println("WEB : 網頁伺服器開始於  Port " + this.port);
	        if(this.logFlag) System.out.println("WEB : 等待連接中...");
	        
	        while(true) 
	        {
	        	try
	        	{
		            this.serverSocket = this.server.accept();
		            if(this.logFlag) System.out.println("WEB : 連接成功, 開始接收數據...");
		            
		            Handler h = new Handler(this.serverSocket);
		            
		            this.WEBservice.execute(h);
	        	}
	        	
	        	catch (Exception e)
	    		{
	    			System.out.println("WEB : 執行序錯誤！！！");
	    			System.out.println("WEB : error :" + e);
	    			e.printStackTrace();
	    		}
	        	
	        	Thread.sleep(1);
	        }	
	        
		}
		catch (Exception e)
		{
			System.out.println("WEB : 執行序初始錯誤！！！");
			System.out.println("WEB : error :" + e);
			e.printStackTrace();
		}
	}

}

class Handler extends Thread 
{
	private boolean logFlag = ini.set.LOG_FLAG_01;
	
	private Socket serverSocket = null;
	
	private TCPServerConnectivity TCPSC = new TCPServerConnectivity();
	private String Filelocal = ini.set.FileLocation;
	private String tempLocal = ini.set.tempLocation;
	private byte[] FileData = null;
	private int FileLen = 0;
	private String Response = "";
	private File ResponseFile = null;
	
	public Handler(Socket _socket)
	{
		this.serverSocket = _socket;
	}
	
	public void run()
	{
		try
		{
			JSONObject Request = null;
		    String request_URL = "";
		    String videoName = "";
		    String ResponseHeaders = "";
		    byte[] file = null;
		    
		    BufferedReader in = null;
		    DataOutputStream outToClient = null;
		    			
			// 透過 socket 接收數據(接收網路通訊封包header + request)()()
            in = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
            
            // 透過 socket 產生回應數據
            outToClient = new DataOutputStream(this.serverSocket.getOutputStream());
            
            // TODO 封包解析器
            Request = this.HttpPacketParser(in);

            if(Request != null)
            {
            	//要求為 GET 時
	            if(Request.getString("method").equals("GET"))
	            {
	            	request_URL = Request.getString("request-URL");
	            	
	            	//清除多餘輸入數值
	            	
	    			if(request_URL.indexOf('?') > 0) 
	    			{
	    				Start.eventid = Integer.parseInt(request_URL.split("\\?")[1]);
	    				
    					System.out.println("	Eventid : " + Start.eventid);
    					System.out.println("	requestid : " + Start.requestid);
    					
	    				request_URL = request_URL.split("\\?")[0];
	    			}
	            	
	    			boolean check = false;
	    			if(request_URL.equals("/video"))
	    			{
	    				videoName = Start.eventid + ".mp4";
	    				ImageTranscoder it = new ImageTranscoder();
    					DataAccessModule dam = new DataAccessModule();
    					String downloadQuery = "select " 
								+ "video "  
								+ "from test.eventlist where eventid = '" + Start.eventid + "' limit 1";
    					dam.impalaSqlCommand(downloadQuery, 1);
    					JSONObject database_info = dam.getSqlCommandResult();
    					
    					it.decoder(this.tempLocal + Start.eventid + ".mp4", database_info.getJSONObject("Result_" + 1).getString("video"));
	    				
    					check = this.HttpFilePacketMaker(this.tempLocal + videoName);
	    			}
	    			else
	    			{
	            		// TODO 檔案封包製作器		            	
	            		check = this.HttpFilePacketMaker(this.Filelocal + request_URL);
	    			}
	            	
	            			            	
                	//取得封包
                	ResponseHeaders = this.Response;
                	
                	//送出回應
                    outToClient.writeBytes(ResponseHeaders);

                    
                    //檔案存在
                    if(check)
                    {	
	                    //取得檔案
	                    file = this.FileData;
	                    
	                	//送出檔案
	                    outToClient.write(file, 0, file.length);
	            		
                    }
                    
                    else
                    {
                    	if(this.logFlag) System.out.println("WEB : 檔案不存在！！！");
                    }
	            }
	            
	            else if(Request.getString("method").equals("POST") ||
	            		Request.getString("method").equals("OPTIONS"))
	            {
	            	request_URL = Request.getString("request-URL");
	            	
//	            	TODO 登入
	            	if(request_URL.equals("/login"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		if (Request.getJSONObject("RequestMassage").getString("id").equals("admin"))
	            		{
	            			if(Request.getJSONObject("RequestMassage").getString("pw").equals("admin"))
	            				{ 
	            					ResponseMassage.put("authority", "root"); 
	            					ResponseMassage.put("state", "success"); 
	            					Start.groupid = 1;
	            				}
	            			else
	            			{
	            				ResponseMassage.put("authority", "root"); 
	            				ResponseMassage.put("state", "fail"); 
	            			}
	            		} 
	            		else if (Request.getJSONObject("RequestMassage").getString("id").equals("user"))
	            		{
	            			if(Request.getJSONObject("RequestMassage").getString("pw").equals("user"))
	            			{ 
            					ResponseMassage.put("authority", "user"); 
            					ResponseMassage.put("state", "success"); 
            					Start.groupid = 0;
            				}
	            			else
	            			{
	            				ResponseMassage.put("authority", "user"); 
	            				ResponseMassage.put("state", "fail"); 
	            			}
	            		}
	            		else
            			{
            				ResponseMassage.put("authority", "none"); 
            				ResponseMassage.put("state", "fail"); 
            			}
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
	            	
//	            	TODO 取得事件
	            	if(request_URL.equals("/eventlist_getform"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		Event evn = new Event();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = evn.getEvent();
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 取得辨識參數
	            	if(request_URL.equals("/IPM_getModuleParam"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		Parameter par = new Parameter();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = par.getParameter();
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
	            	//TODO 載入資料(感測資料)
	            	if(request_URL.equals("/OBD2_getSensorData"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		
	            		Sensor sen = new Sensor();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = sen.getSensor();
	            	
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 取得事件類型
	            	if(request_URL.equals("/eventtype_getoption"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		Treatment treat = new Treatment(0);
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = treat.getEventtype();
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 取得 處理方法陳述
	            	if(request_URL.equals("/treatmentstatement_getform"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		
	            		Treatment treat = new Treatment(0);
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = treat.getTreatmenet();
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
	            	
//	            	TODO 修正影像處理模組參數
	            	if(request_URL.equals("/IPM_modifyModulesParameter"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		ModifyModulesParameter modifyModulesParameter = new ModifyModulesParameter();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = modifyModulesParameter.Modify(Request.getJSONObject("RequestMassage"));
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 選擇影像處理模組
	            	if(request_URL.equals("/IPM_confirmUseModules"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		ConfirmUseModules ConfirmModule = new ConfirmUseModules();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = ConfirmModule.updateConfirmUseModules(Request.getJSONObject("RequestMassage"));
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 取得相似事件列表
	            	if(request_URL.equals("/relate"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
	            		Relate relate = new Relate();
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = relate.getrelate();
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
//	            	TODO 取得 處理方法陳述
	            	if(request_URL.equals("/treatmentstatement"))
	            	{
	            		// 封包信息
	            		ResponseHeaders = "HTTP/1.0 200 OK\r\n"
	            				 + "Connection close\r\n"
	            				 + "Content-Type: application/json; charset=utf-8\r\n"
	            				 + "\r\n" ;
//	            		System.out.println("a " + Start.eventid);
	            		Treatment treat = new Treatment(Start.eventid);
	            		
	            		JSONObject ResponseMassage = new JSONObject();
	            		
	            		ResponseMassage = treat.getTreatmenet();
	            		
	            		
	            		outToClient.writeBytes(ResponseHeaders); // 送出標題 (headers)

	            		outToClient.writeBytes(new String(ResponseMassage.toString().getBytes("utf-8"),"iso8859-1")); // 送出訊息
	            	}
	            	
	            }
            }
            
            else
            {
            	System.out.println("WEB : 出現 null 訊息");
            }
            
            
            outToClient.flush();
            outToClient.close();
            this.serverSocket.close();
            
            if(this.logFlag) System.out.println("WEB : 送出訊息完畢！！！");
            if(this.logFlag) System.out.println("WEB : ********** end **********");
		}
		
		catch (Exception e)
		{
			System.out.println("error :" + e);
			e.printStackTrace();
		}
	}
	
	// TODO 封包解析器
		private JSONObject HttpPacketParser(BufferedReader in) 
		{
			StringTokenizer tokenizedLine = null;
			StringBuffer data = null;
			
			String line = "";
			String title = null;
			String parameter = "";
			
			int Content_Length = 0;
			
			JSONObject Request = new JSONObject();
			JSONObject Headers = new JSONObject();
			
			try
	    	{
				while(true) // 循環直到訊息結尾
		        {
		        	line = in.readLine();
		        	if(this.logFlag) System.out.println(line);

		        	if(line == null)
		        	{
		        		Request = null;
		        		break;
		        	}
		        	
		        	if(line.equals("")) // 出現訊息結尾
	            	{
	            		break; // 跳出迴圈
	            	}
		        	
		        	if(line.toUpperCase().startsWith("GET")   ||
		 	           line.toUpperCase().startsWith("POST")   ||
		 	           line.toUpperCase().startsWith("OPTIONS")) 
	            	{
		        		tokenizedLine = new StringTokenizer(line);
		        		Request.put("method", tokenizedLine.nextToken());
		        		Request.put("request-URL", tokenizedLine.nextToken());
		        		Request.put("version", tokenizedLine.nextToken());
	            	}
		        	
		        	else
		        	{
		        		tokenizedLine = new StringTokenizer(line);
		        		title = tokenizedLine.nextToken().replace(":","");
		        		while(tokenizedLine.hasMoreTokens())
		        		{
		        			parameter += tokenizedLine.nextToken();
		        		}
		        		
		        		Headers.put(title, parameter);
		        		parameter = "";
		        	}
		        }
				
				if(Request != null)
	        	{
					// 加入 headers 至要求中
					Request.put("Headers", Headers);
					
					// 如果是 POST 需要繼續接收傳遞的資料
					if(Request.getString("method").equals("POST"))
					{
						data = new StringBuffer();
						
						// 取出資料長度
						Content_Length = Request.getJSONObject("Headers").getInt("Content-Length");
						
						// 開始取得資料
						for (int i = 0; i < Content_Length; i++)
						{
							int d = in.read();

							if ((char) d == '$')
							{
								break;
							}

							data.append((char) d);
						}
			            
			        	if(this.logFlag) System.out.println("收到的訊息 = \n" + data.toString());
			            
						Request.put("RequestMassage", new JSONObject(data.toString()));
					}
	        	}
				
	    	}
	    	
	    	catch (Exception e)
			{
				System.out.println("解析器錯誤！！！");
				e.printStackTrace();
			}
			
			return Request;
		}
	
	// TODO 檔案封包產生器
	public boolean HttpFilePacketMaker(String Message) 
	{
		try
		{			
			if(this.logFlag) System.out.println("收到訊息 ：" + Message);

			this.ResponseFile = new File(Message); // 於指定目錄取得檔案資訊
					
			// 判斷檔案存不存在
            if(this.ResponseFile.exists())
            {
            	FileInputStream inFile = new FileInputStream(Message); // 透過 buffer 取得檔案
            	this.FileLen = (int) this.ResponseFile.length(); // 取出檔案長度(byte)
				this.FileData = new byte[this.FileLen]; // 製作文件資料存放陣列

				if(this.logFlag) System.out.println("取得檔案 ： " + Message);
				if(this.logFlag) System.out.println("檔案長度 ： " + this.FileLen + " byte");
						
				// 封包信息
				this.Response = "HTTP/1.0 200 OK\r\n"
							  + "Content-Type: " + this.ChangeContentType(Message) + "; charset=utf-8\r\n"
							  + "Content-Length: " + this.FileLen + "\r\n"
							  + "Connection close\r\n"
							  + "\r\n";
						
				inFile.read(this.FileData); // 讀取檔案

				inFile.close(); // 關閉讀檔串流
				
            }
            
            else
            {
				if(this.logFlag) System.out.println("非規範訊息 ：" + Message);
				if(this.logFlag) System.out.println("錯誤的檔案要求，該檔案並不存在");
				
				// 封包信息
				this.Response = "HTTP/1.0 404 Not Found\r\n"
						 	  + "Connection close\r\n"
						      + "\r\n"; // 送出 關閉此次連線
								
				return false;
            }
		}
		
		catch (Exception e)
		{
			System.out.println("訊息製作錯誤！！！");
			System.out.println("error :" + e);
			e.printStackTrace();
		}
		
		return true;
	}
	
	// TODO 檔案名稱類型轉換器
	private String ChangeContentType(String fileName)  
	{
		if(fileName.endsWith(".webm")) // webm影片檔
            return "video/webm";
    	else if(fileName.endsWith(".html") || fileName.endsWith(".htm")) 
            return "text/html";   // HTML網頁 
        else if(fileName.endsWith(".js")) 
            return "application/x-javascript"; // JavaScript程式碼檔
        else if(fileName.endsWith(".txt") || fileName.endsWith(".java")) 
            return "text/plain";  // 文字型態 
        else if(fileName.endsWith(".gif")) 
            return "image/gif";   // GIF圖檔 
        else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) 
            return "image/jpeg";  // JPEG圖檔 
        else if(fileName.endsWith(".ico")) 
            return "image/x-icon";  // JPEG圖檔 
        else if(fileName.endsWith(".css")) 
            return "text/css";  // CCS格式 
        else if(fileName.endsWith(".mp4")) 
           return "video/mp4";  // CCS格式 
        else // 其它不可判別的檔案一律視為文字檔型態 
            return "text/plain"; 
	}
}