package ini;

public class set {
	
	public static final String FileLocation = "web";
	public static String tempLocation = "python/";

	//--------------------------------------------------------------------------------------------	
	
	public static final boolean LOG_FLAG_01 = true; // HTTPServerConnectivity.java
	
	public static final boolean LOG_FLAG_02 = false; // Start.java
	
	final public static boolean LOG_FLAG_03 = false; // TCPConnectivity.java

	
	
	//--------------------------------------------------------------------------------------------
	
	public static final String ServerIP = "163.18.49.36"; // "127.0.0.1" "163.18.49.34" 40.83.216.11
	public static final String localhost = "127.0.0.1";
	public static final int HTTPPort = 8001;
	public static final int TCPPort = 8010;
	
	//----------------------------- Action Number -----------------------------------
    public class ActionCode
    {
        // TCP server/client action code ------------
        final public static int getFile = 100;
        final public static int getDeviceState = 101;
    }
}
