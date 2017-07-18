package ADAS.Data.Test;

import java.sql.SQLException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import ADAS.Modules.DataAccessModule;
import main.Start;

public class Type {
	
	private JSONObject Eventtype = new JSONObject();
	
	public Type() throws ClassNotFoundException, SQLException, JSONException{
		
		DataAccessModule dam = new DataAccessModule();
		String downloadQuery = "select eventtype from " + 
								"test.treatment " + 
								"where eventid = " + Start.eventid ;
		
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult().getJSONObject("Result_1");
		
		this.Eventtype = database_info.getJSONObject("eventtype");
		
		
	}
	
	public JSONObject gettype(){return this.Eventtype;}
}
