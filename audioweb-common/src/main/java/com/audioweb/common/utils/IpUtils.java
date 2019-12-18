package com.audioweb.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP方法
 * 
 * @author ruoyi,shuofang
 */
public class IpUtils
{
	/**
	 * 
	 * @Title: getIpAddr 
	 * @Description: TODO(通过连接获取客户端IP) 
	 * @param @param request
	 * @return String 返回类型 
	 * @author ShuoFang 
	 * @date 2019年12月18日 上午9:01:24
	 */
    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
    /**
     * 
     * @Title: internalIp 
     * @Description: TODO(判断IP地址是否为内网地址) 
     * @param @param ip
     * @return boolean 返回类型 
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:02:10
     */
    public static boolean internalIp(String ip)
    {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] addr)
    {
        if (StringUtils.isNull(addr) || addr.length < 2)
        {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0)
        {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4)
                {
                    return true;
                }
            case SECTION_5:
                switch (b1)
                {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将IPv4地址转换成字节
     * 
     * @param text IPv4地址字符串或者IP地址转long
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text)
    {
        if (text.length() == 0)
        {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try
        {
            long l;
            int i;
            switch (elements.length)
            {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L))
                        return null;
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L))
                        return null;
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L))
                        return null;
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i)
                    {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L))
                        return null;
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i)
                    {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        return bytes;
    }
    /**
     * 获取本地地址IP
     * @Title: getHostIp 
     * @Description: TODO(获取本地地址IP) 
     * @return String 返回类型 
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:08:12
     * @throws UnknownHostException 无法确定主机的IP地址
     */
    public static String getHostIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return "127.0.0.1";
    }
    /**
     * 获取本机地址名字
     * @Title: getHostName 
     * @Description: TODO(获取本机地址名字) 
     * @return String 返回类型 
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:08:30
     * @throws UnknownHostException 无法确定主机的IP地址
     */
    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
        }
        return "未知";
    }
    /**
     * 将IP转为long数字存储
     * @Title: ip2Long 
     * @Description: TODO(将IP转为long数字存储) 
     * @param IP IP地址
     * @return Long 返回类型 -1为转换出错
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:08:49
     */
    public static Long ip2Long(String IP) {
    	Long LongIp = -1L;
    	IP = judgeIP(IP);
    	if(IP !=null) {
    		LongIp = 0L;
    		String[] ipStrings = IP.split("\\.");
    	    for (int i = 0;i<ipStrings.length;i++){
    	    	LongIp =  Long.parseLong(ipStrings[i]) << 8*(ipStrings.length-1-i) | LongIp;
    		}
    	}
	    return LongIp;
	}
    /**
     * 验证IP地址是否正确
     * @Title: judgeIP 
     * @Description: TODO(验证IP地址是否正确) 
     * @param @param ip
     * @param @return   
     * @return String 返回类型  null为出错
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:09:16
     * @throws UnknownHostException 无法确定主机的IP地址
     */
    public static String judgeIP(String ip) {
    	if(ip != null) {
    		String[] ipStrings = ip.split("\\.");
    		if(ipStrings.length == 4) {
    			try {
					return InetAddress.getByName(ip).getHostAddress();
				} catch (UnknownHostException e) {
					// TODO: handle exception
				}
    		}
    	}
		return null;
	}
    /**
     * 将long数据转为对应IP地址
     * @Title: long2IP 
     * @Description: TODO(将long数据转为对应IP地址) 
     * @param @param lip
     * @param @return   
     * @return String 返回类型 null为转换出错
     * @author ShuoFang 
     * @date 2019年12月18日 上午9:09:32
     * @throws Exception 转换出错
     */
    public static String long2IP(Long lip) {
		if(lip >= 0) {
			try {
				StringBuffer sb = new StringBuffer("");
				// 直接右移24位
				sb.append(String.valueOf((lip >> 24)));
				sb.append(".");
				// 将高8位置0，然后右移16位
				sb.append(String.valueOf((lip & 0x00FFFFFF) >> 16));
				sb.append(".");
				// 将高16位置0，然后右移8位
				sb.append(String.valueOf((lip & 0x0000FFFF) >> 8));
				sb.append(".");
				// 将高24位置0
				sb.append(String.valueOf((lip & 0x000000FF)));
				return sb.toString();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return null;
	}
    /**
     * byte[]数组转long型IP
     * @Title: bytes2Long 
     * @Description: TODO(byte[]数组转long型IP) 
     * @param bytes IP数组
     * @return long 返回类型  IP地址 -1为转换出错
     * @author ShuoFang 
     * @date 2019年12月18日 上午10:14:11
     */
    public static long bytes2Long(byte[] bytes) {
    	long ip = -1L;
        if(bytes.length > 0 && bytes.length < 5) {
        	ip = 0;
        	for(int i = 0;i<bytes.length;i++) {
        		ip = ip << 8 | (bytes[i] & 0xFF);
        	}
        }
        return ip;
    }
    public static void main(String[] args) {
		System.out.println(long2IP(bytes2Long(textToNumericFormatV4(String.valueOf(ip2Long("0.0.0.0"))))));
	}
}