package tool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPGet
{
	public IPGet() { /****/ }

	public String WindowsIPGet() throws UnknownHostException
	{
		return InetAddress.getLocalHost().getHostAddress();
	}

	public String AllSystemIPGet() throws UnknownHostException
	{
		try
		{
			for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI.hasMoreElements();)
			{
				NetworkInterface netI = enNetI.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public String getMAC() throws UnknownHostException, SocketException
	{
		InetAddress address = this.getSystemInetAddress();
		NetworkInterface ni = NetworkInterface.getByInetAddress(address);
		byte[] mac = ni.getHardwareAddress(); //取出網卡mac
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) { sb.append(String.format("%s%02X", (i > 0 ? "-" : ""), mac[i])); }
					
		return sb.toString();
	}
	
	public InetAddress getSystemInetAddress() throws UnknownHostException
	{
		try
		{
			for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI.hasMoreElements();)
			{
				NetworkInterface netI = enNetI.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress())
					{
						return inetAddress;
					}
				}
			}
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
//	public InetAddress getFirstNonLoopbackAddress() throws SocketException
//	{
//		Enumeration en = NetworkInterface.getNetworkInterfaces();
//		while (en.hasMoreElements())
//		{
//			NetworkInterface i = (NetworkInterface) en.nextElement();
//			for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();)
//			{
//				InetAddress addr = (InetAddress) en2.nextElement();
//				if (!addr.isLoopbackAddress())
//				{
//					if (addr instanceof Inet4Address)
//					{
//						return addr;
//					}
//				}
//			}
//		}
//		return null;
//	}
}