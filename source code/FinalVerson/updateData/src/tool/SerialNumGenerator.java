package tool;

public class SerialNumGenerator 
{
	int strLen = 6; // default length:6

	int num = 0; // 隨機字符碼

	String outStr = ""; // 產生的密碼

	public String generator(int strLen) 
	{
		int num = 0;
		while (this.outStr.length() < strLen) 
		{
			num = (int) (Math.random() * (126 - 48 + 1)) + 48; // 可見字符
			
			if (num > 57 && num < 65) continue; // 排除非數字非字母

			this.outStr += (char) num;
		}

		return this.outStr.toLowerCase();

	}// end of generator
}
