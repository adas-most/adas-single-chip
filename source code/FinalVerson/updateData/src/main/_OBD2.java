package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;
import tool.ImageTranscoder;

public class _OBD2 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException{

		// TODO Auto-generated method stub
		JSONObject data_temp = new JSONObject();
		File OBD2 = new File("OBD2.txt");
		
		int eventid = Integer.parseInt(args[0]);
		DataAccessModule dam = new DataAccessModule();
		StringBuffer updateQuery;
		String[] OBU = {"radiatortemperature","enginerpm","speed","o2sensor","airflowmeter"};
		String[] Unit = {"(&degC)","(rpm)","(km/hr)","(V)","(kg/h)"}; 
		
		updateQuery = new StringBuffer();
		// TODO new event
		ImageTranscoder it = new ImageTranscoder();
		String enc = it.encoder(eventid + ".mp4");
		updateQuery.append("Insert into test.eventlist values('" + eventid + "','2017/07/19','台灣科技大學','0','" +
				enc + "');");
		dam.impalaSqlCommand(updateQuery.toString(), 2);
		//System.out.println("UpdateQuery : " + updateQuery);
		//System.out.println("*********************************************************");
		
		
		updateQuery = new StringBuffer();
		updateQuery.append("Insert into test.sensor values('" + eventid + "','");
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(OBD2));
			String temp = new String();
			
			//*** Set updateQuery **//
			updateQuery = new StringBuffer();
			updateQuery.append("Insert into test.sensor values('" + eventid );
			
			temp = br.readLine();
			
			while (temp != null) {
				
				for (int i = 0; i < OBU.length ; i++)
				{
					data_temp = new JSONObject();
					if (temp.split(":")[0].equals(OBU[i]))
					{
						updateQuery.append("','");
						data_temp.put("Value", temp.split(":")[1]);
						data_temp.put("Unit", Unit[i]);
						updateQuery.append(data_temp.toString());
					}
				}
				temp = br.readLine();
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateQuery.append("');");
		
		dam.impalaSqlCommand(updateQuery.toString(), 2);
		System.out.println("UpdateQuery : " + updateQuery);
		System.out.println("*********************************************************");
	}

}
