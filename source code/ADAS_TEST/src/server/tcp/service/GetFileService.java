package server.tcp.service;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class GetFileService
{
	private String fileDist = ini.set.tempLocation;

	Map<String, Object> request = null;
	Map<String, Object> response = new HashMap<String, Object>();
	
	public GetFileService(Map<String, Object> _request) 
	{
		this.request = _request;
	}

	public boolean implement()
    {
		String fileName = (String) this.request.get("fileName");
        try
        {
            Path path = Paths.get(this.fileDist + fileName);
            byte[] data = Files.readAllBytes(path);
            this.response.put("fileName", fileName);
            this.response.put("fileData", data);
        }

        catch(IOException e)
        {
            System.err.println("FileDispatcher transmitter => check file path/name : " + this.fileDist + fileName);
            e.printStackTrace();

            this.response.put("fileName", fileName);
            this.response.put("error", "FileDispatcher transmitter => check file npath/name : " + this.fileDist + fileName);

            return false;
        }

        return true;
    }
	
	public Map<String, Object> getResponse()
	{
		return this.response;
	}
	
}
