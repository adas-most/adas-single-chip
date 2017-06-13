package Modules;


import server.web.HTTPServerConnectivity;

public class WebServerConnectModule 
{
	private int HTTPServerPort = ini.set.HTTPPort;
	
	private HTTPServerConnectivity HTTPSC = null;
	
	public WebServerConnectModule() { /****/ }

	
	public void SetHTTPMonitorPort(int port) 
	{
		this.HTTPServerPort = port;
	}
	

	public void HTTPServerOn() 
	{
		this.HTTPSC = new HTTPServerConnectivity(this.HTTPServerPort);
		this.HTTPSC.start();
	}


}
