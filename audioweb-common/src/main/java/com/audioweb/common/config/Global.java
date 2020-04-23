package com.audioweb.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置类
 * 
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "ruoyi")
public class Global
{
    /** 项目名称 */
    private static String name;

    /** 版本 */
    private static String version;

    /** 版权年份 */
    private static String copyrightYear;

    /** 实例演示开关 */
    private static boolean demoEnabled;

    /** 上传路径 */
    private static String profile;
    
    /** 音频文件上传路径 */
    private static String filePath;
    
    /** 点播上传路径 */
    private static String pointPath;
    
    /** 文本音频上传路径 */
    private static String wordPath;

    /** 获取地址开关 */
    private static boolean addressEnabled;
    
    /** 删除或丢失音频文件保存记录天数 */
    private static Long lostFile;
   
    public static String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        Global.name = name;
    }

    public static String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        Global.version = version;
    }

    public static String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        Global.copyrightYear = copyrightYear;
    }

    public static boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        Global.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }
    
    /**默认文件上传地址由数据库获取，不再从配置中获取*/
    public static void setProfile(String profile)
    {
        Global.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        Global.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }
    
    /**
     * 获取音频图片存储路径
     */
    public static String getImagePath()
    {
    	return getProfile() + "/image";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		Global.filePath = filePath;
	}

	public static String getPointPath() {
		return pointPath;
	}

	public static void setPointPath(String pointPath) {
		Global.pointPath = pointPath;
	}

	public static String getWordPath() {
		return wordPath;
	}

	public static void setWordPath(String wordPath) {
		Global.wordPath = wordPath;
	}

	public static Long getLostFile() {
		return lostFile;
	}

	public void setLostFile(Long lostFile) {
		Global.lostFile = lostFile;
	}
}
