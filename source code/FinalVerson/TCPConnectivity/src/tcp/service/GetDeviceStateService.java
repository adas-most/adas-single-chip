package tcp.service;

import java.util.HashMap;
import java.util.Map;

public class GetDeviceStateService
{
	Map<String, Object> request = null;
	Map<String, Object> response = new HashMap<String, Object>();

	public GetDeviceStateService(Map<String, Object> _request) 
	{
		this.request = _request;
	}
	
	public boolean implement()
    {
		this.response.put("DeviceState", "我是裝置狀態");

		return true;
    }	
	
	public Map<String, Object> getResponse()
	{
		return this.response;
	}
}
