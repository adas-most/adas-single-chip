package ADAS.Data.Generate;

import java.sql.SQLException;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class SensorGen {

	public static void main(String[] args) throws JSONException, ClassNotFoundException, SQLException {
		
		Random ran = new Random();
		DataAccessModule dam = new DataAccessModule();
		int Eventquantity = 4;
		StringBuffer updateQuery = new StringBuffer();
		
		JSONObject temp = new JSONObject();
		JSONObject Sensor = new JSONObject();
		
		
		String[] OBU = {"RadiatorTemperature","EngineRPM","Speed","O2Sensor","AirFlowMeter"}; 
		String[] Unit = {"(&degC)","(rpm)","(km/hr)","(V)","(kg/h)"}; 
		
		
		for (int j = 0; j < Eventquantity ; j++)
		{
			
			//*** Set Data **//
			for (int i = 0; i < OBU.length ; i++)
			{
				
				temp = new JSONObject();
				if (i == 1)
				{
					temp.put("Value",ran.nextInt(8)*1000 + 3000);
				}
				else if (i == 3)
				{
					temp.put("Value",ran.nextFloat());
				}
				else
				{
					temp.put("Value",ran.nextInt(70) + 30);
				}
				
				temp.put("Unit",Unit[i]);
				Sensor.put(OBU[i], temp);
			}
			System.out.println("Sensor : " + Sensor);
					
			
			//*** Set updateQuery **//
			updateQuery = new StringBuffer();
			
			updateQuery.append("Insert into test.sensor values('" + (j+1) + "','");
			for (int i = 0; i < OBU.length ; i++)
			{
				if (i == (OBU.length-1))
				{
					updateQuery.append(Sensor.getJSONObject(OBU[i]).toString());
				}
				else
				{
					updateQuery.append(Sensor.getJSONObject(OBU[i]).toString() + "','");
				}
			}
			updateQuery.append("');");
			
			
			
			dam.impalaSqlCommand(updateQuery.toString(), 2);
			System.out.println("UpdateQuery : " + updateQuery);
			
			System.out.println("*********************************************************");
		}

	}

}