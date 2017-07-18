package tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MapJsonConverter
{
	public MapJsonConverter() { /****/ }

	public Map<String, Object> JSONObjectToMap(JSONObject JO) throws JSONException
	{
		Map<String, Object> M = new HashMap<String, Object>();
		Iterator<?> it = JO.keys();  
		String key = null;
		
        while(it.hasNext())
        {  
        	key = (String) it.next();
        	
        	if(JO.get(key) instanceof JSONObject) 
        	{
        		M.put(key, JSONObjectToMap(JO.getJSONObject(key)));
            }
        	
        	else if(JO.get(key) instanceof JSONArray) 
        	{
        		M.put(key, JSONOArrayToArrayList(JO.getJSONArray(key)));
        	}
        	
        	else
        	{
        		M.put(key, JO.getString(key));
        	}
        }  
		
		return M;
	}
	
	public ArrayList<String> JSONOArrayToArrayList(JSONArray JA) throws JSONException
	{
		ArrayList<String> AL = new ArrayList<String>();
		
		for(int i = 0; i < JA.length(); i++)
		{
			AL.add(JA.getString(i));
		}
		
		return AL;
	}
	
	public JSONArray ArrayListToJSONOArray(ArrayList<String> AL) throws JSONException
	{
		JSONArray JA = new JSONArray();
		
		for(int i = 0; i < AL.size(); i++)
		{
			JA.put((String) AL.get(i));
		}
		
		return JA;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject MapToJSONObject(Map<String, Object> M) throws JSONException
	{
		JSONObject JO = new JSONObject();

		for (String key : M.keySet()) 
		{
			if(M.get(key) instanceof Map<?, ?>) 
        	{
				JO.put(key, MapToJSONObject((Map<String, Object>) M.get(key)));
            }
        	
        	else if(M.get(key) instanceof ArrayList<?>) 
        	{
        		JO.put(key, ArrayListToJSONOArray((ArrayList<String>) M.get(key)));
        	}
        	
        	else if(M.get(key) instanceof byte[]) 
        	{
        		JO.put(key, ((byte[]) M.get(key)).toString());
        	}
			
        	else
        	{
        		JO.put(key, (String) M.get(key));
        	}
		}
		
		return JO;
	}
	
}
