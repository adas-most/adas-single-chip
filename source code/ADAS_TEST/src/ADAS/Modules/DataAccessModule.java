package ADAS.Modules;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONObject;

import ADAS.Database.Connectivity.impalaConnectivity;

public class DataAccessModule
{
	 private String host = "163.18.49.36";
    private String port = "21050";
	 private String user = "admin";
	
	 private JSONObject sqlCommandResult;
	
    /** @param  host                host
    /** @param  port                port
    /** @param  user                �w�]���n�J�ϥΪ�
    /** @return sqlCommandResult    ���^ impala �d�ߧ������G

    /***** using default parameter constructor *****/
    public DataAccessModule() { /***/ }

    /***** using custom parameter constructor *****/
    public DataAccessModule(String _host, String _port, String _user)
    {
    	this.host = _host;
    	this.port = _port;
    	this.user = _user;
    }

    /***** start to impala sql command *****/
	public void impalaSqlCommand(String sqlCommand, int sqlMode) throws ClassNotFoundException, SQLException
	{
		impalaConnectivity instance = new impalaConnectivity(this.host, this.port, this.user);	
		this.sqlCommandResult = instance.setQuery(sqlCommand, sqlMode);
		instance.close();
	}

    public JSONObject getSqlCommandResult() { return this.sqlCommandResult; }

}