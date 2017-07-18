package main;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;
import tool.ImageTranscoder;

public class test 
{

	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException 
	{
		ImageTranscoder it = new ImageTranscoder();
		
		JSONObject List = new JSONObject();
		DataAccessModule dam = new DataAccessModule();
		
		String downloadQuery = "select " 
								+ "video "  
								+ "from test.eventlist limit 1";
		
		System.out.println(downloadQuery);
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult();
		
		it.decoder("test.mp4", database_info.getJSONObject("Result_" + 1).getString("video"));
		
//		for (int i = 0; i < database_info.length() ; i++)
//		{
//			List.put("Event_" + (i+1), database_info.getJSONObject("Result_" + (i+1)));
//		}
//		
//		System.out.println(List);
	}

}
