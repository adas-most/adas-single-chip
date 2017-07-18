package tool;

import java.io.BufferedReader;
import java.util.StringTokenizer;

import org.codehaus.jettison.json.JSONObject;

public class HttpPacketParser
{
	BufferedReader in = null;
	public HttpPacketParser(BufferedReader _in)
	{
		this.in = _in;
	}
	
	public JSONObject parser()
	{
		StringTokenizer tokenizedLine = null;
		StringBuffer data = null;

		String line = "";
		String title = null;
		String parameter = "";

		int Content_Length = 0;

		JSONObject Request = new JSONObject();
		JSONObject Headers = new JSONObject();

		try
		{
			while (true) // 循環直到訊息結尾
			{
				line = this.in.readLine();

				if (line == null)
				{
					Request = null;
					break;
				}

				if (line.equals("")) // 出現訊息結尾
				{
					break; // 跳出迴圈
				}

				if (line.toUpperCase().startsWith("GET") || line.toUpperCase().startsWith("POST")
						|| line.toUpperCase().startsWith("OPTIONS"))
				{
					tokenizedLine = new StringTokenizer(line);
					Request.put("method", tokenizedLine.nextToken());
					Request.put("request-URL", tokenizedLine.nextToken());
					Request.put("version", tokenizedLine.nextToken());
				}

				else
				{
					tokenizedLine = new StringTokenizer(line);
					title = tokenizedLine.nextToken().replace(":", "");
					while (tokenizedLine.hasMoreTokens())
					{
						parameter += tokenizedLine.nextToken();
					}

					Headers.put(title, parameter);
					parameter = "";
				}
			}

			if (Request != null)
			{
				// 加入 headers 至要求中
				Request.put("Headers", Headers);

				// 如果是 POST 需要繼續接收傳遞的資料
				if (Request.getString("method").equals("POST"))
				{
					data = new StringBuffer();

					// 取出資料長度
					Content_Length = Request.getJSONObject("Headers").getInt("Content-Length");

					// 開始取得資料
					for (int i = 0; i < Content_Length; i++)
					{
						int d = in.read();

						if ((char) d == '$')
						{
							break;
						}

						data.append((char) d);
					}

					Request.put("RequestMassage", new JSONObject(data.toString()));
				}
			}
		}

		catch (Exception e)
		{
			System.out.println("解析器錯誤！！！");
			e.printStackTrace();
		}

		return Request;
	}
}
