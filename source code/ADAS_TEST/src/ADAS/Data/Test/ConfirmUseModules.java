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
		
		updateQuery = "insert overwrite " + "test.parameter " +  
							"select " + 
								"eventid," + 
								"RoadDetection," + 
								"ForwardObjectDetection," + 
								"IntelligentHeadlight," + 
								"LaneDepartureDetection," + 
								"case when eventid = '" + Start.eventid + 
									"' then '" + statement.toString() + 
								"' else statement " +
								"end " +
							"from " + "test.parameter;";
	
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
					"end " +
				"from " + "test.eventlist;";

		dam.impalaSqlCommand(updateQuery, 2);
		System.out.println(updateQuery);
		System.out.println("*********************************************************");
		
		
		return (new JSONObject("{'OK':87}"));
	}

}
