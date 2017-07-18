package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import ADAS.Modules.DataAccessModule;
import tool.ImageTranscoder;




public class _Reset {

	public static DataAccessModule dam = new DataAccessModule();;
	
	public static void main(String[] args){
		try 
		{
			// TODO Auto-generated method stub
			ImageTranscoder it = new ImageTranscoder();
			//System.out.println(it.encoder("FileDist/sample_fix_no_rain.mp4"));
			
			String Query = "";
			
			Query = "Insert overwrite test.eventlist values('1','2017/05/1','國道1號南下128公里','0', '" + it.encoder("FileDist/sample_fix_no_rain.mp4") + "');"; dam.impalaSqlCommand(Query, 2);Thread.sleep(1000);System.gc();
			Query = "Insert into test.eventlist values('2','2017/05/2','國道3號北上18公里','1', '" + it.encoder("FileDist/sample_fix.mp4") + "');"; dam.impalaSqlCommand(Query, 2);Thread.sleep(1000);System.gc();
			Query = "Insert into test.eventlist values('10','2017/04/2','高雄第一科技大學','2', '" + it.encoder("FileDist/Event10.mp4") + "');"; dam.impalaSqlCommand(Query, 2);Thread.sleep(1000);System.gc();
			Query = "Insert into test.eventlist values('11','2017/04/1','高雄第一科技大學','2', '" + it.encoder("FileDist/Event11.mp4") + "');"; dam.impalaSqlCommand(Query, 2);Thread.sleep(1000);System.gc();
	
			File reset = new File("reset2.txt");
			BufferedReader br = new BufferedReader(new FileReader(reset));
			String temp = new String();
			temp = br.readLine();
			System.out.println("temp : " + temp);
			while (temp != null) 
			{
				dam.impalaSqlCommand(temp.toString(), 2);Thread.sleep(1000);System.gc();
				System.out.println("UpdateQuery : " + temp);
				
				System.out.println("*********************************************************");
				temp = br.readLine();
			}
			br.close(); 
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

}
