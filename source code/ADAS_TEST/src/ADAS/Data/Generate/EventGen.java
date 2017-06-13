package ADAS.Data.Generate;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;

import ADAS.Modules.DataAccessModule;


public class EventGen {

public static void main(String[] args) throws JSONException, ClassNotFoundException, SQLException {
		
		DataAccessModule dam = new DataAccessModule();
		String updateQuery = new String();
		
			
		//*** Set updateQuery **//
		
		updateQuery = new String();
		updateQuery = "Insert into test.eventlist values('1','2017/05/1','高雄市楠梓區卓越路2號','0');";
		dam.impalaSqlCommand(updateQuery, 2);
		System.out.println(updateQuery);
		System.out.println("*********************************************************");
		
		updateQuery = new String();
		updateQuery = "Insert into test.eventlist values('2','2017/05/2','高雄市楠梓區卓越路2號','1');";
		dam.impalaSqlCommand(updateQuery, 2);
		System.out.println(updateQuery);
		System.out.println("*********************************************************");
			
	}
}
