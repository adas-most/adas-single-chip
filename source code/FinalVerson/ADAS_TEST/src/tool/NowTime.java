package tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowTime 
{
	private Date now = null;
	
	private SimpleDateFormat sdFormat = null;

	public NowTime() { /****/ }
	
	public String getNowTime()
	{
		this.now = new Date();
		
		sdFormat = new SimpleDateFormat("HH:mm:ss");
		
		return this.sdFormat.format(this.now);
	}
	
	public long getTime()
	{
		this.now = new Date();
		return this.now.getTime();
	}
	
}
