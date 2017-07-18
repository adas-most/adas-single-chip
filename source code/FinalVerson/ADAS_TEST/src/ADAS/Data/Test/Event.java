package ADAS.Data.Test;
import ADAS.Modules.DataAccessModule;
import main.Start;
import tool.ImageTranscoder;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

// TODO 事件清單
public class Event {
	
	public Event() {/******/}

	public JSONObject getEvent() throws ClassNotFoundException, SQLException, JSONException {
		
		JSONObject List = new JSONObject();
		DataAccessModule dam = new DataAccessModule();
		
		String downloadQuery = "select " 
								+ "eventid,time,position,status "  
								+ "from test.eventlist " 
								+ "where status <= '"
								+ Start.groupid + "' "
								+ "order by time" ;
		System.out.println(downloadQuery);
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult();
		
		for (int i = 0; i < database_info.length() ; i++)
		{			
			List.put("Event_" + (i+1), database_info.getJSONObject("Result_" + (i+1)));
		}
		
		System.out.println(List);
		
		return List;
		
	}
}
