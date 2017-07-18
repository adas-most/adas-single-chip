package server.tcp;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * @author  James(YamiWaffle)
 * @version 1.02
 * @Reference MMCN TCPFileSenter Project.
 *
 * update date : 2017/05/26
 *
 * TCPclient.
 *
 * @param  {@link String} host : TCP host
 * @param  {@link int}    port : TCP port
 *
 * @param  {@link Map}    response : TCP client response
 */
public class TCPclient
{
    private String host = ini.set.localhost;
    private int    port = ini.set.TCPPort;

    private Map<String, Object> response = null;

    public TCPclient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    /**
     * TODO handleRequest
     *
     * Handle client request to client.
     *
     * @param  {@link Map} client request
     *
     * @throws ClassNotFoundException, IOException
     */
    @SuppressWarnings("unchecked")
    public boolean handleRequest(Map<String, Object> request)
    {
        Socket socket = null;

        ObjectInputStream  iStream = null;
        ObjectOutputStream oStream = null;

        try
        {
            InetSocketAddress isAddr = new InetSocketAddress(this.host, this.port);
            System.out.println("host : " + this.host);
            System.out.println("port : " + this.port);
            socket = new Socket();
            socket.connect(isAddr);

            try
            {
                oStream = new ObjectOutputStream( socket.getOutputStream() );
                oStream.writeObject(request);
                oStream.flush();

                iStream = new ObjectInputStream( socket.getInputStream() );

                this.response = new HashMap<String, Object>();
                this.response = (Map<String, Object>) iStream.readObject();

                return true;
            }

            catch (ClassNotFoundException e)
            {
                System.err.println("TCPclient => ObjectInputStream readObject error !!");
                e.printStackTrace();

                return false;
            }
        }

        catch(IOException e)
        {
            System.err.println("TCPclient => ObjectInputStream OR socket.connect error !!");
            e.printStackTrace();

            return false;
        }

        finally
        {
            try
            {
                if(iStream != null) { iStream.close(); }
                if(oStream != null) { oStream.close(); }
                if(socket  != null) { socket.close(); }
            }

            catch(IOException e)
            {
                System.err.println("TCPclient => socket close error !!");
                e.printStackTrace();

                return false;
            }
        }
    } // end handleRequest()

    /**
     * TODO getResponse
     *
     * Return the Server Response
     *
     * @return  {@link Map} Server Response
     */
    public Map<String, Object> getResponse() { return this.response; }
    
}// end client
