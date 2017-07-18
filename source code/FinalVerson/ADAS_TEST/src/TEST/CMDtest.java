package TEST;

import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class CMDtest {

	public static void main(String[] args) throws JSONException, IOException {
		
		
		JSONObject modifyModulesParameter = new JSONObject("{'roaddetection':{'param':{'feature':{'value':'222'},'machinelearning':{'value':'132'},'featuresource':{'value':'2'},'weather':{'value':'0'},'daynight':{'value':'0'},'vehicledensity':{'value':'1'},'location':{'value':'3'}}},'forwardobjectdetection':{'param':{'feature':{'value':'254'},'machinelearning':{'value':'122'},'featuresource':{'value':'5'},'weather':{'value':'0'},'daynight':{'value':'0'},'vehicledensity':{'value':'1'},'location':{'value':'3'}}},'intelligentheadlight':{'param':{'feature':{'value':'213'},'machinelearning':{'value':'177'},'featuresource':{'value':'8'}}},'lanedeparturedetection':{'param':{'feature':{'value':'195'},'machinelearning':{'value':'145'},'featuresource':{'value':'3'},'weather':{'value':'0'},'daynight':{'value':'0'},'vehicledensity':{'value':'1'}}}}");
		
		String command = "";
		FileWriter fw = new FileWriter("test.bat");
		
		fw.write("cd python\n");
		
		command = "python case_all.py";
		
		command += " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("feature").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("machinelearning").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("featuresource").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("weather").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("daynight").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("vehicledensity").getString("value")
				+  " " + modifyModulesParameter.getJSONObject("roaddetection").getJSONObject("param").getJSONObject("location").getString("value");
		
		command +="\nexit";
		
		System.out.println(command);
		fw.write(command);
        fw.flush();
        fw.close();
        System.out.println("OK");
	}

}
