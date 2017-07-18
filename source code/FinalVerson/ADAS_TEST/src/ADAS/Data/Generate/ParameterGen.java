package ADAS.Data.Generate;

import java.sql.SQLException;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class ParameterGen {

	public static void main(String[] args) throws JSONException, ClassNotFoundException, SQLException {
		
		Random ran = new Random();
		DataAccessModule dam = new DataAccessModule();
		int Eventquantity = 2;
		String updateQuery = "";
		
		JSONObject RoadDetection = new JSONObject();
		JSONObject ForwardObjectDetection = new JSONObject();
		JSONObject IntelligentHeadlight = new JSONObject();
		JSONObject LaneDepartureDetection = new JSONObject();
		JSONObject temp = new JSONObject();
		JSONObject temp2 = new JSONObject();
		
		for (int j = 0; j < Eventquantity ; j++)
		{
			//*** Set Data **//
			temp = new JSONObject();
			String[] Roadparam = {"feature","machinelearning","featuresource","weather","daynight","vehicledensity","location"}; 
			for (int i = 0; i < Roadparam.length ; i++)
			{
				temp2 = new JSONObject();
				temp2.put("value", ran.nextInt(255));
				temp.put(Roadparam[i],temp2);
			}
			
			RoadDetection.put("param", temp);
			System.out.println("RoadDetection : " + RoadDetection.toString());
			
			
			temp = new JSONObject();
			String[] Forwardparam = {"feature","machinelearning","featuresource","weather","daynight","vehicledensity","location"};
			for (int i = 0; i < Forwardparam.length ; i++)
			{
				temp2 = new JSONObject();
				temp2.put("value", ran.nextInt(255));
				temp.put(Roadparam[i],temp2);
			}
			
			ForwardObjectDetection.put("param", temp);
			System.out.println("ForwardObjectDetection : " + ForwardObjectDetection.toString());
			
			
			temp = new JSONObject();
			String[]Lightparam = {"brightness","spectrumsensitivity","lampshockdelay"}; 
			for (int i = 0; i < Lightparam.length ; i++)
			{
				temp2 = new JSONObject();
				temp2.put("value", ran.nextInt(255));
				temp.put(Roadparam[i],temp2);
			}
			
			IntelligentHeadlight.put("param", temp);
			System.out.println("IntelligentHeadlight : " + IntelligentHeadlight.toString());
			
			
			temp = new JSONObject();
			String[]Laneparam = {"rightsensitivity","leftsensitivity","minwidth ","maxwidth","warningsensitivity","backtrackingtime"}; 
			for (int i = 0; i < Laneparam.length ; i++)
			{
				temp2 = new JSONObject();
				temp2.put("value", ran.nextInt(255));
				temp.put(Roadparam[i],temp2);
			}
			
			LaneDepartureDetection.put("param", temp);
			System.out.println("LaneDepartureDetection : " + LaneDepartureDetection.toString());
		
		
			//*** Set updateQuery **//
			updateQuery = "Insert into test.parameter values('" + (j+1) + "','"
					+ RoadDetection.toString() + "','"
					+ ForwardObjectDetection.toString() + "','"
					+ IntelligentHeadlight.toString() + "','"
					+ LaneDepartureDetection.toString() 
					+ "');";

//			dam.impalaSqlCommand(updateQuery, 2);
			System.out.println("	" + updateQuery.toString());
			System.out.println("*********************************************************");
		}

	}

}
