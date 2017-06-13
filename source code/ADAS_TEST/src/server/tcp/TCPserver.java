package server.tcp;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import server.tcp.service.GetFileService;
import server.tcp.service.GetDeviceStateService;

/**
 * @author  James(YamiWaffle)
 * @version 1.02
 * @Reference MMCN TCPFileSenter Project.
 *
 * update date : 2017/05/26
 *
 * TCPserver.
 *
 * @param  {@link int}             port            : TCP port
 * @param  {@link ServerSocket}    serverSocket    : Server Socket
 * @param  {@link ExecutorService} ExecutorService : ExecutorService(Thread)
 */
public class TCPserver extends Thread
{
    private int port = ini.set.TCPPort;

    private ServerSocket serverSocket = null;

    private ExecutorService threadExecutor = Executors.newCachedThreadPool();

    public TCPserver() {}

    public TCPserver(int port)
    { this.port = port; }

    /**
     * TODO TCPserver run
     *
     * Listen client request.
     *
     * @throws IOException, InterruptedException
     */
    public void run()
    {
        try
        {
            this.serverSocket = new ServerSocket(this.port);

            while(true)
            {
                try
                {
                    Socket socket = this.serverSocket.accept();
                    this.threadExecutor.execute(new RequestThread(socket));

                    Thread.sleep(1);
                }

                catch (IOException e)
                {
                    System.err.println("TCPserver => TCPserver execute error !!");
                    e.printStackTrace();
                }

                catch (InterruptedException e)
                {
                    System.err.println("TCPserver => Thread sleep error !!");
                    e.printStackTrace();
                }
            }
        }

        catch (IOException e)
        {
            System.err.println("TCPserver => TCPserver initial error !!");
            e.printStackTrace();
        }
    } // end listenRequest()
} // end class TCPserver



/**
 * TODO RequestThread
 *
 * @implements Runnable
 * @see java.lang.Runnable#run()
 *
 * Handle client request.
 *
 * @param  {@link Socket} clientSocket : Socket
 */
class RequestThread implements Runnable
{
    private Socket clientSocket = null;

    public RequestThread(Socket clientSocket)
    { this.clientSocket = clientSocket; }

    /**
     * TODO RequestThread run
     *
     * request thread.
     *
     * @throws NumberFormatException, IOException
     */
    @SuppressWarnings("unchecked")
    public void run()
    {
        ObjectInputStream  iStream = null;
        ObjectOutputStream oStream = null;

        Map<String, Object> request  = null;
        Map<String, Object> response = null;

        try
        {
            iStream = new ObjectInputStream(this.clientSocket.getInputStream());
            oStream = new ObjectOutputStream(this.clientSocket.getOutputStream());

            request  = (Map<String, Object>) iStream.readObject();
            response = new HashMap<String, Object>();

            // TODO 操作服務介面
            response = handleAction(request);

            oStream.writeObject(response);
            oStream.flush();
        }

        catch (IOException e)
        {
            System.err.println("TCPserver => ObjectInputStream or ObjectOutputStream get stream error !!");
            e.printStackTrace();
        }

        catch (ClassNotFoundException e)
        {
            System.err.println("TCPserver => ObjectInputStream read object error !!");
            e.printStackTrace();
        }

        finally
        {
            try
            {
                if (iStream != null)
                { iStream.close(); }

                if (oStream != null)
                { oStream.close(); }

                if (this.clientSocket != null && !this.clientSocket.isClosed())
                { this.clientSocket.close(); }

                Thread.sleep(1);
            }

            catch (IOException e)
            {
                System.err.println("TCPserver => ObjectInputStream or ObjectOutputStream close error !!");
                e.printStackTrace();
            }

            catch (InterruptedException e)
            {
                System.err.println("TCPserver => Thread sleep error !!");
                e.printStackTrace();
            }
        }
    } // end run()

    /**
     * TODO handleAction
     *
     * Handle Client request action.
     *
     * @param  {@link Map} client request
     * @return {@link Map} server response
     *
     * @throws NumberFormatException, IOException
     */
    private Map<String, Object> handleAction(Map<String, Object> request)
    {
        Map<String, Object> response = new HashMap<String, Object>();

        boolean ServiceState = false;
        int requestActionCode = 0;

        try { requestActionCode = Integer.parseInt( request.get("action").toString() ); }

        catch(NumberFormatException e)
        {
            System.err.println("TCPserver => This Action Code :" + request.get("action") + "not define !!");
            e.printStackTrace();
        }

        switch(requestActionCode)
        {
            // client request the server send the file
            case ini.set.ActionCode.getFile:
            {
            	GetFileService getFileService = new GetFileService(request);

            	ServiceState = getFileService.implement();

                if(ServiceState) 
                {
                	response = getFileService.getResponse(); 
                }
                else
                {
                    response.put("error", "Server receive fail !!");
                }

                break;
            }
            
            // client request server device status
            case ini.set.ActionCode.getDeviceState:
            {
            	GetDeviceStateService getDeviceStateService = new GetDeviceStateService(request);
            	ServiceState = getDeviceStateService.implement();

                if(ServiceState) 
                {
                	response = getDeviceStateService.getResponse(); 
                }
                
                else
                {
                    response.put("error", "Server transmit fail !!");
                }

                break;
            }
            
            default:
            {
                response.put("error", "This Action Code : " + request.get("action") + " not define !!");

                break;
            }

        } // end switch

        return response;
    } // end handleAction()

} // end class RequestThread
