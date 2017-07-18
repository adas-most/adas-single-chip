package tool;

import java.io.File;

public class ForceDeleteDir 
{
	public ForceDeleteDir(File file)
	{
		boolean check = false;

		if (file.isFile())
		{
			check = deleteFileSafely(file);

			System.out.println("移除檔案 " + file.getName() + " :" + check);
			return;
		}

		if (file.isDirectory())
		{
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0)
			{
				check = deleteFileSafely(file);

				System.out.println("移除目錄 " + file.getName() + " :" + check);
				return;
			}

			for (int i = 0; i < childFiles.length; i++)
			{
				new ForceDeleteDir(childFiles[i]);
			}
			check = deleteFileSafely(file);

			System.out.println("移除目錄 " + file.getName() + " :" + check);
		}
	}

	/**
	 * 安全刪除文件.
	 * @param file
	 * @return
	 */
	public static boolean deleteFileSafely(File file) 
	{
		if (file != null)
		{
			String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
			File tmp = new File(tmpPath);
			file.renameTo(tmp);
			return tmp.delete();
		}
		return false;
	}
}
