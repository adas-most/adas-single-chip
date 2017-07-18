package ADAS.Data.Test;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import ADAS.Modules.DataAccessModule;
import main.Start;

public class Relate {
	
	private JSONObject relate = new JSONObject();
	private String Eventtype = new String();
	
	public Relate() throws ClassNotFoundException, SQLException, JSONException{
		
		DataAccessModule dam = new DataAccessModule();
		String downloadQuery = "select eventtype from " + 
				"test.treatment " + 
				"where eventid = '" + Start.eventid + "'";

		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult().getJSONObject("Result_1");
		System.out.println("1 " + database_info.toString()); 
		this.Eventtype = database_info.getString("eventtype");
		
		downloadQuery = "select eventid from " + 
								"test.treatment " + 
								"where eventtype = '" + Eventtype + "'";
		
		dam.impalaSqlCommand(downloadQuery, 1);
		
		database_info = dam.getSqlCommandResult();
		
		System.out.println("2 " + database_info.toString()); 
		
		for (int i = 0; i < database_info.length(); i++)
		{ 
			downloadQuery = "select eventid,time,position from " + 
					"test.eventlist " + 
					"where status = '2' AND eventid = '" + 
					database_info.getJSONObject("Result_" + (i+1)).getString("eventid") + "'";
			dam.impalaSqlCommand(downloadQuery, 1);
			JSONObject database_temp = dam.getSqlCommandResult();
			System.out.println("3 " + database_temp.toString()); 
			if(database_temp.length() != 0)
				{ this.relate.put("Event_" + (i+1), database_temp.getJSONObject("Result_1")); }
		}
		this.relate.put("eventtype", this.Eventtype);
	}
	
	public JSONObject getrelate(){return this.relate;}
}
