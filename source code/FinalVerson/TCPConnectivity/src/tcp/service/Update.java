package tcp.service;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class Update {
	
	Map<String, Object> request = null;
	Map<String, Object> response = new HashMap<String, Object>();
	private boolean log = tcp.configuration.Settings.LOG_FLAG;
	
	public Update (Map<String, Object> _request) 
	{
		this.request = _request;
	}
	
	public boolean implement() throws ClassNotFoundException, SQLException
   {
		try {
			int eventid = (int) this.request.get("eventid");
			DataAccessModule dam = new DataAccessModule();
			JSONObject database_info = new JSONObject();
			File shell = new File(tcp.configuration.Settings.FileDist + "Parameter.sh");
			
			String cmd = new String();
			String downloadQuery = "select " 
					+ "roaddetection," 
					+ "forwardobjectdetection," 
					+ "intelligentheadlight," 
					+ "lanedeparturedetection " 
					+ "from test.parameter " 
					+ "where eventid = '" + eventid + "';";
			
			dam.impalaSqlCommand(downloadQuery, 1);
			database_info = dam.getSqlCommandResult().getJSONObject("Result_1");
			
			if(shell.exists()){
				shell.delete();
			}
			
			FileWriter fw = new FileWriter(shell);
			String Header = "#! /bin/bash \n";
			fw.write(Header);
			if (log) System.out.println("=====  Wirting =====");
			if (log) System.out.println(Header);
			
			cmd = "python case_all.py";
			cmd += " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("feature").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("machinelearning").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("featuresource").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("weather").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("daynight").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("vehicledensity").getString("value")
					+  " " + database_info.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("location").getString("value");
			
			
			fw.write(cmd);
			fw.flush();
			fw.close();
			if (log) System.out.println(cmd);
			
			this.response.put("OK","Update Success");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			 return false;
		}		
		return true;
   }
	
	
	public Map<String, Object> getResponse()
	{
		return this.response;
	}
}
