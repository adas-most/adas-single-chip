package ADAS.Data.Test;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class Treatment {
	
	private JSONObject Eventtype = new JSONObject();
	private JSONObject Treatmenet = new JSONObject();
	public Treatment() throws ClassNotFoundException, SQLException, JSONException {
		
		DataAccessModule dam = new DataAccessModule();
		String downloadQuery = "select " 
							+ "eventtype," 
							+ "treatment " 
							+ "from test.treatment " 
							+ "where eventid = '0';"; 
										//eventid = 0為預設方法
	
		dam.impalaSqlCommand(downloadQuery, 1);
		
		JSONObject database_info = dam.getSqlCommandResult().getJSONObject("Result_1");
		
		this.Eventtype = database_info.getJSONObject("eventtype");
		this.Treatmenet = database_info.getJSONObject("treatment");
	}
	
	public JSONObject getEventtype() {return this.Eventtype;}
	
	public JSONObject getTreatmenet() {return this.Treatmenet;}
	
}
