package main;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class Update 
{

	public static void main(String[] args) throws Exception
	{
		DataAccessModule dam = new DataAccessModule();
		JSONObject database_info = new JSONObject();
		
		String downloadQuery = "select eventid from test.re";
		dam.impalaSqlCommand(downloadQuery, 1);
		database_info = dam.getSqlCommandResult().getJSONObject("Result_1");
		
		
		int eventid = Integer.parseInt(database_info.getString("eventid"));
		File shell = new File("Parameter.sh");
		
		String cmd = new String();
		downloadQuery = "select " 
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
		System.out.println("=====  Wirting =====");
		System.out.println(Header);
		
		cmd = "python case_all.py";
		cmd += "";
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
		
	
	}

}
