package ADAS.Data.Test;
import ADAS.Modules.DataAccessModule;
import main.Start;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class Parameter {

	public Parameter() {/******/}
	
	public JSONObject getParameter() throws ClassNotFoundException, SQLException, JSONException {
		
			DataAccessModule dam = new DataAccessModule();
			String downloadQuery = "select " 
								+ "roaddetection," 
								+ "forwardobjectdetection," 
								+ "intelligentheadlight," 
								+ "lanedeparturedetection " 
								+ "from test.parameter " 
								+ "where eventid = '" + Start.eventid + "';";
		
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult();
		
		// System.out.println(database_info.getJSONObject("Result_1"));
		
		return (database_info.getJSONObject("Result_1"));
		
	}
}
