package ADAS.Data.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;
import Modules.CommandExecuteModule;
import main.Start;

public class ModifyModulesParameter
{
   public ModifyModulesParameter() {/******/}
	
	public JSONObject Modify(JSONObject modifyModulesParameter) throws ClassNotFoundException, SQLException, JSONException, IOException, InterruptedException
	{
        String command = "";
		DataAccessModule dam = new DataAccessModule();
		FileWriter fw = new FileWriter("start.bat");
		String updateQuery = new String();
		
		fw.write("cd python\n");
		
		if(Start.eventid == 2)
		{
			if (Integer.parseInt(modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("featuresource").getString("value")) >= 5)
			{
				command = "python case_sky.py";
			}
			else
			{
				command = "python case_sky_no.py";
			}
		}
		else if(Start.eventid == 1)
		{
			if (Integer.parseInt(modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("featuresource").getString("value")) >= 5)
			{
				command = "python case_rain.py";
			}
			else
			{
				command = "python case_rain_no.py";
			}
		}
		
		command += " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("feature").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("machinelearning").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("featuresource").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("weather").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("daynight").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("vehicledensity").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("location").getString("value");
		
//		command ="python list.py";
		command +="\nexit";
        
		fw.write(command);
        fw.flush();
        fw.close();
        
		CommandExecuteModule CommandExecuteModule = new CommandExecuteModule();
		CommandExecuteModule.execute("start.bat");
		
		
		updateQuery = "insert overwrite " + "test.parameter " +  
				"select " + 
					"eventid," + 
					"case when eventid = '" + Start.eventid + 
						"' then '" + modifyModulesParameter.getJSONObject("roaddetection").toString() + 
					"' else roaddetection " +
					"end " + "," +
					"case when eventid = '" + Start.eventid + 
						"' then '" + modifyModulesParameter.getJSONObject("forwardobjectdetection").toString() + 
					"' else forwardobjectdetection " +
					"end " + "," +
					"case when eventid = '" + Start.eventid + 
						"' then '" + modifyModulesParameter.getJSONObject("intelligentheadlight").toString() + 
					"' else intelligentheadlight " +
					"end " + "," +
					"case when eventid = '" + Start.eventid + 
						"' then '" + modifyModulesParameter.getJSONObject("lanedeparturedetection").toString() + 
					"' else lanedeparturedetection " +
					"end " + "," +
					"statement " +
				"from " + "test.parameter;";
		
		dam.impalaSqlCommand(updateQuery, 2);
		
		System.out.println(updateQuery);
		System.out.println("*********************************************************");
		
		return (new JSONObject("{'OK':87}"));
	}
}
