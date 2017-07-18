package ADAS.Data.Test;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;
import main.Start;

public class ConfirmUseModules {
	
	public ConfirmUseModules(){/*****/}

	public JSONObject updateConfirmUseModules(JSONObject statement) throws JSONException, ClassNotFoundException, SQLException{
		
		
		DataAccessModule dam = new DataAccessModule();
		String updateQuery = new String();
		
		updateQuery = "insert into " + "test.treatment " + 
					  "values('" +
					  	Start.eventid + "','" + 
					  	statement.getString("EventType") + "','" +
					  "');";
	
		dam.impalaSqlCommand(updateQuery, 2);
		System.out.println(updateQuery);
		System.out.println("*********************************************************");
		
		updateQuery = new String();
		
		updateQuery = "insert overwrite " + "test.eventlist " +  
				"select " + 
					"eventid," + 
					"time," + 
					"position," + 
					"case when eventid = '" + Start.eventid + 
						"' then '" + 1 + 
					"' else status " +
					"end , video " +
				"from " + "test.eventlist;";

		dam.impalaSqlCommand(updateQuery, 2);
		System.out.println(updateQuery);
		
		updateQuery = "CREATE TABLE test.re(f STRING,eventid STRING);"; dam.impalaSqlCommand(updateQuery, 2);
		updateQuery = "insert overwrite test.re values('0','" + Start.eventid + "');"; dam.impalaSqlCommand(updateQuery, 2);

		
		
		
		System.out.println("*********************************************************");
		
		
		return (new JSONObject("{'OK':87}"));
	}

}
