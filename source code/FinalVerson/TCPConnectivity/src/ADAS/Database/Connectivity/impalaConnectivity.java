package ADAS.Database.Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class impalaConnectivity
{
    private static String JDBCDriver     = "com.cloudera.impala.jdbc4.Driver";
    private static String CONNECTION_URL = "jdbc:impala://127.0.0.1:21050/default;";

    private Connection CONNECTION = null;
    private Statement  STATEMENT  = null;
    private ResultSet  RESULTSET  = null;
    private ResultSetMetaData RESULTSETMETADATA = null;

    private String host     = "127.0.0.1";
    private String port     = "21050";
    private String groups   = "default";
    private String mode     = "noSasl";
    private String user     = "admin;";
    private String password = "admin;";
    private String SETTING  = "auth=noSasl;user=admin;password=admin;";

    private String query       = "";
    private JSONObject queryResult = null;
    
    /***** using default parameter constructor *****/
    public impalaConnectivity() { /***/ }

    /*
     * @param  host           host
     * @param  port           port
     * @param  groups         預設的默認群組
     * @param  mode           預設的登入模式
     * @param  user           預設的登入使用者
     * @param  password       預設密碼默認此項
     * @param  query          送入 impala 查詢的指令
     * @return queryResult    取回 impala 查詢完的結果
     */
    /***** using custom parameter constructor *****/
    public impalaConnectivity(String _host, String _port, String _user) throws ClassNotFoundException, SQLException
    {
        this.host = _host;
        this.port = _port;
        this.user = _user;

        this.LOAD_JDBCDriver();
    }

    /***** loading JDBCDriver *****/
    private boolean LOAD_JDBCDriver() throws SQLException, ClassNotFoundException
    {
        try
        {
	    	CONNECTION_URL = "jdbc:impala://" + this.host + ":" + this.port + "/" + this.groups + ";";
	        this.SETTING   = "auth=" + this.mode + ";//user=" + this.user + ";password=" + this.password + ";";
	
	        Class.forName(JDBCDriver);  
	
	        this.CONNECTION = DriverManager.getConnection(CONNECTION_URL + this.SETTING);        
	        this.STATEMENT  = this.CONNECTION.createStatement();
        }
        
        catch(ClassNotFoundException de) 
        {
        	de.printStackTrace();
        	System.err.println("error : 找不到 Driver 驅動程式 ");
        	System.err.println("檢查 jar 檔是否匯入");
        }  
        
        catch(SQLException se) 
        {
        	se.printStackTrace();
        	System.err.println("error : 連線失敗 ");
        	System.err.println("檢查  URL(ip,port,user) 是否有誤");
        }
        
        catch(Exception e) 
        {
        	e.printStackTrace();
        	System.err.println("error : 出現例外的錯誤 ");
        	System.err.println("檢查程式本身 , 是否有邏輯錯誤");
        }

        return true;
    }

    /*
     * @param  query          送入 impala 查詢的指令
     * @param  executeMode    送入 impala 的指令模式
     * @return queryResult    取回 impala 查詢完的結果
     */
    public JSONObject setQuery(String _query, int executeMode)
    {
        this.query = _query;
        this.queryResult = new JSONObject();
        
        try
        {
            switch(executeMode)
            {
            	// query method (有回傳結果)(查詢用)
                case 1: // executeQuery
                {
                    this.RESULTSET = this.STATEMENT.executeQuery(this.query);
                    this.RESULTSETMETADATA = this.RESULTSET.getMetaData();
                    int ColumnCount = this.RESULTSETMETADATA.getColumnCount(); // 取回結果集(RESULTSET)總共的行數
                    
                    int RowCount = 0;
                                        
                    JSONObject row = null; // 存放橫列資料
                    
                    String temp = ""; // 存放讀取的資料
                    
                    while(this.RESULTSET.next()) // 如果結果集(RESULTSET)下一列還有值則回傳 ture 
                    { 
                    	RowCount++;
                    	
            			row = new JSONObject("{}");

                    	for(int i = 1; i <= ColumnCount; i++)
                    	{
                    		temp = this.RESULTSET.getString(i); // 取得值並移動指針

                    		if(temp.startsWith("{")) // 判斷資料是否為 json 格式
                    		{	
                    			row.put(this.RESULTSETMETADATA.getColumnName(i), new JSONObject(temp)); // 放入json資料
                    		}
                    		
                    		else
                    		{
                    			row.put(this.RESULTSETMETADATA.getColumnName(i), temp); // 放入字串資料
                    		}

                    		if(ColumnCount == 1 || i == ColumnCount ) // 指針指到結尾
                    		{ 
                    			this.queryResult.put("Result_" + RowCount, row); // 寫入回傳值
                    		}
	                    }
                    }
                    
                    try { if(this.RESULTSET != null) { this.RESULTSET.close(); } }
                    catch(SQLException se1)          { se1.printStackTrace(); }
                    return this.queryResult;
                }

                // method (無回傳結果)(建立、移除、更新查詢表格用)
                case 2 : 
                { 
                	this.STATEMENT.executeUpdate(this.query);
                	
                	this.queryResult.put("Result", true);
                	
                	return this.queryResult; 
                }// executeUpdate
                
                
                // method (萬用版, 記憶體消耗較大)
                case 3 : 
                { 
                	this.STATEMENT.execute(this.query);
                	
                	this.queryResult.put("Result", true);
                	
                	return this.queryResult; 
                }// execute
                
                default:
                    return null;
            }
        }

        catch(SQLException se) 
        {
        	se.printStackTrace();
            System.err.println("The SQL ERROR : " + se);
        	return null; 
        }
        
        catch(JSONException je) 
        {
        	je.printStackTrace();
            System.err.println("The JSON ERROR : " + je);
        	return null; 
        }        
    }

    public void close()
    {
        try { if(this.RESULTSET != null) { this.RESULTSET.close(); } }
        catch(SQLException se1)          { se1.printStackTrace(); }
    	
        try { if(this.STATEMENT != null)  { this.STATEMENT.close();  } }
        catch(SQLException se2)           { se2.printStackTrace(); }

        try { if(this.CONNECTION != null) { this.CONNECTION.close(); } }
        catch(SQLException se3)           { se3.printStackTrace(); }
    }

}