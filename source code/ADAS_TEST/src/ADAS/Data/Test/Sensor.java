package ADAS.Data.Test;
import ADAS.Modules.DataAccessModule;
import main.Start;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class Sensor {
	
	public Sensor() {/*****/}
	
	
	public JSONObject getSensor() throws ClassNotFoundException, SQLException, JSONException {
			
			DataAccessModule dam = new DataAccessModule();
			String downloadQuery = "select " 
								+ "radiatortemperature," 
								+ "enginerpm," 
								+ "speed," 
								+ "o2sensor," 
								+ "airflowmeter " 
								+ "from test.sensor " 
								+ "where eventid = '" + Start.eventid + "';";
		
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult();
		
		System.out.println(database_info.getJSONObject("Result_1"));
		
		return (database_info.getJSONObject("Result_1"));
		
	}
}
