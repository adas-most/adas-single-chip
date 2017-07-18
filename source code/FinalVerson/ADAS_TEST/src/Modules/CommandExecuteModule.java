package Modules;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecuteModule
{
    public CommandExecuteModule() {}
    
    public void execute(String command) throws IOException, InterruptedException
    {
//    	Runtime.getRuntime ().exec(command);
        Runtime rt = Runtime.getRuntime ();
        Process proc = rt.exec (command);
        String line = null;
        
        InputStream stderr = proc.getErrorStream ();
        InputStreamReader esr = new InputStreamReader (stderr);
        BufferedReader ebr = new BufferedReader (esr);
        System.out.println ("<error>");
        while ( (line = ebr.readLine ()) != null)
            System.out.println(line);
        System.out.println ("</error>");
         
        InputStream stdout = proc.getInputStream ();
        InputStreamReader osr = new InputStreamReader (stdout);
        BufferedReader obr = new BufferedReader (osr);
        System.out.println ("<output>");
        while ( (line = obr.readLine ()) != null)
            System.out.println(line);
        System.out.println ("</output>");
    
        int exitVal = proc.waitFor ();
        System.out.println ("Process exitValue: " + exitVal);
    }
}