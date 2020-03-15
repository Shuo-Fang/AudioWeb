package com.audioweb.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件处理工具类
 * 
 * @author ruoyi
 */
public class FileUtils
{
	/**只能为英文,中文和 _ - | . */
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    public static String AUDIONAME_PATTERN = "^[^?\\ * | \" < > : /]{1,256}$";

    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @param type 校验正则
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename,String type)
    {
        return filename.matches(type);
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
    
    /**
	 * 创建目录
	 * @param destDirName目标目录名
	 * @return 
	 */
	public static Boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if(!dir.exists()){				//判断文件整个路径是否存在
			System.out.println("创建路径");
			return dir.mkdirs();		//不存在就全部创建
		}
		return false;
	}
	/**
	 * 判断目录是否存在
	 * @param destDirName目标目录名
	 * @return 
	 */
	public static Boolean findDir(String destDirName) {
		File dir = new File(destDirName);
		if(dir.exists() && dir.isDirectory()){				//判断是否为目录
			System.out.println("存在路径");
			return true;		//存在
		}
		return false;
	}
	
	/**
	 * 读取路径下所有文件，包括子文件夹下文件
	 * 
	 * @param filePath
	 * @return listFiles
	 * @throws 
	 */
	public static List<String> getCurFilesList(String filePath) {
		List<String> files = new ArrayList<String>();
        File file = new File(filePath);
        if(findDir(filePath)) {
	        File[] tempList = file.listFiles();
	        for (int i = 0; i < tempList.length; i++) {
	            if (tempList[i].isFile()) {
	                files.add(tempList[i].getPath());
	                //文件名，不包含路径
	                //String fileName = tempList[i].getName();
	            }
	            if (tempList[i].isDirectory()) {
	                //这里递归
	            	files.addAll(getCurFilesList(tempList[i].getPath()));
	            }
	        }
        }else {//不存在就创建
        	createDir(filePath);
        }
        return files;
	}
	/**
	 * 将路径分隔符改为适应linux系统下路径(或为http虚拟路径)(因为win系统能兼容此路径,将不再转化回win路径分隔符)
	 * @Title: formatToWin 
	 * @Description: 将路径分隔符改为适应linux系统下路径(或为http虚拟路径)(因为win系统能兼容此路径,将不再转化回win路径分隔符)
	 * @param path
	 * @return String 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月15日 下午2:42:07
	 */
	public static String formatToLin(String path) {
		return path.replaceAll("\\\\", "/");
	}
	/**
	 * 将路径分隔符改为自动适应系统下路径
	 * @Title: formatToSys 
	 * @Description: 将路径分隔符改为自动适应系统下路径
	 * @param path
	 * @return String 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月15日 下午2:43:58
	 */
	public static String formatToSys(String path) {
		if(File.pathSeparator.equals("\\")) {
			path = path.replaceAll("/", "\\\\");
		}else {
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}
	/**
	 * 检查路径最后一个字符是否为分隔符，是则去掉返回
	 * @Title: formatPath 
	 * @Description: 检查路径最后一个字符是否为分隔符，是则去掉返回
	 * @param path
	 * @return String 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月15日 下午3:08:20
	 */
	public static String formatPath(String path) {
		int i = path.lastIndexOf("\\");
		if(i>= 0 && i == path.length()-1) {
			return path.substring(0,path.length()-1);
		}else {
			i = path.lastIndexOf("/");
			if(i>= 0 && i == path.length()-1) {
				return path.substring(0,path.length()-1);
			}
		}
		return path;
	}
}
