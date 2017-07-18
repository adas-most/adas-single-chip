package tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

public class ImageTranscoder
{
	private String format = new String("utf-8");

    private Encoder en = null;
    private Decoder de = null;

    /** @param format        file contents format
    /** @param en            base64 encoder
    /** @param de            base64 decoder
    /** @param iFilePath     file input path
    /** @param oFilePath     file output path
    /** @param base64Code    base64 code

    /***** using default parameter constructor *****/
    public ImageTranscoder() { /***/ }

    /***** encode the image into base64 code *****/
    public String encoder(String iFilePath)
    {
        String base64Code = new String();

        try
        {
            this.en = Base64.getEncoder();

            FileInputStream iFileStream = new FileInputStream(iFilePath);
            byte[] ImageByte = new byte[iFileStream.available()];

            iFileStream.read(ImageByte);
            iFileStream.close();

            base64Code = new String(this.en.encode(ImageByte), this.format);
        }

        catch(Exception e)
        {
            System.out.println("[ERROR] Image encoder fail. " + e);
            e.printStackTrace();

            return "false"; // return false
        }

        return base64Code; // return true
    }

    /***** decode the base64 code into an image *****/
    public boolean decoder(String oFilePath, String base64Code)
    {
        try
        {
            this.de = Base64.getDecoder();

            FileOutputStream oFileStream = new FileOutputStream(oFilePath);
            byte[] ImageByte = this.de.decode(base64Code);

            oFileStream.write(ImageByte);
            oFileStream.close();
        }

        catch(Exception e)
        {
            System.out.println("[ERROR] Image decoder fail. " + e);
            e.printStackTrace();

            return false; // return false
        }

        return true; // return true
    }

    public void setFormat(String _format) { this.format = _format; }

}