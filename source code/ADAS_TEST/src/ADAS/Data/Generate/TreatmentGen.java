package ADAS.Data.Generate;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ADAS.Modules.DataAccessModule;

public class TreatmentGen {
	
	public static void main(String[ ] args) throws JSONException, ClassNotFoundException, SQLException {
		DataAccessModule dam = new DataAccessModule();
		String updateQuery = new String();
		int Eventtypequantity = 5;
		
		JSONObject Eventtype = new JSONObject();
		JSONObject Treatmenet = new JSONObject();
		
		for (int i = 0; i < Eventtypequantity ; i++)
		{
			Eventtype.put(i + "","類型" + i);
		}
		//*** Set updateQuery **//
		
		Treatmenet.put("1","輪胎需要打氣");
		Treatmenet.put("2","後視鏡需要更換");
		Treatmenet.put("3","煞車片需要換新");
		
		updateQuery = "insert into test.treatment values('0','" 
							+ Eventtype.toString() + "','"
							+ Treatmenet.toString() + "')";
		
		
		dam.impalaSqlCommand(updateQuery.toString(), 2);
		System.out.println("UpdateQuery : " + updateQuery);
		
		System.out.println("*********************************************************");
		}
	}