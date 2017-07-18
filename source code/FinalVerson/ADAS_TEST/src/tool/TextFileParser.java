package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFileParser
{
    private String format = new String("utf-8");

    /** @param format       file contents format
    /** @param iFilePath    file input path
    /** @param oFilePath    file output path
    /** @param contents     file contents
    /** @param append       file contents append

    /***** using default parameter constructor *****/
    public TextFileParser() { /***/ }

    /***** read the contents from a text file *****/
    public String read(String iFilePath)
    {
        String contents = new String();

        try
        {
            FileInputStream iFileStream = new FileInputStream(iFilePath);
            InputStreamReader r = new InputStreamReader(iFileStream, this.format);
            BufferedReader reader = new BufferedReader(r);

            while(reader.ready()) { contents += reader.readLine(); }

            // close all stream
            reader.close();
            r.close();
            iFileStream.close();
        }

        catch(IOException ioe)
        {
        	System.out.println("[ERROR] The text file read fail. " + ioe);
            ioe.printStackTrace();

            return contents; // return false;
        }

        return contents; // return true;
    }

    /***** write the contents to a text file *****/
    public boolean write(String oFilePath, String contents, boolean append)
    {
        File file = new File(oFilePath); // create a new file

        try
        {
            FileOutputStream oFileStream = new FileOutputStream(file, append);
            OutputStreamWriter w = new OutputStreamWriter(oFileStream, this.format);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(contents);

            // close all stream
            writer.close();
            w.close();
            oFileStream.close();
        }

        catch(IOException ioe)
        {
        	System.out.println("[ERROR] The text file write fail. " + ioe);
            ioe.printStackTrace();

            return false; // return false;
        }

        return true; // return true;
    }

    public void setFormat(String _format) { this.format = _format; }

}