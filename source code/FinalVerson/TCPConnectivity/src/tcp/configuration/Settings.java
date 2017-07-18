package tcp.configuration;

public class Settings
{
    //----------------------------- TCP Connectivity -----------------------------------
    final public static boolean LOG_FLAG = true;

    final public static String CONNECT_HOST = "127.0.0.1";
    final public static int    CONNECT_PORT = 8787;
    
    //----------------------------- Device Environment -----------------------------------
    final public static String FileDist  = "FileDist/";
    
    //----------------------------- Action Number -----------------------------------
    public class ActionCode
    {
        // TCP server/client action code ------------
        final public static int getFile = 100;
        final public static int getDeviceState = 101;
        final public static int update = 102;
    }
}