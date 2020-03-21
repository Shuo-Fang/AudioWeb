package com.audioweb.common.constant;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
    /**
     * 文件广播音频资源映射路径前缀
     */
    public static final String AUDIO_FILE_PREFIX = "/audio/file";
    /**
     * 终端点播音频资源映射路径前缀
     */
    public static final String AUDIO_POINT_PREFIX = "/audio/point";
    /**
     * 文件转语音音频资源映射路径前缀
     */
    public static final String AUDIO_WORD_PREFIX = "/audio/word";

    /** 文件路径配置信息KEY */
    public final static String PREFIX_PATH = "sys.profile";
    
    /** netty服务绑定ip地址 */
    public final static String IP_CONFIG = "sys.config.ip";
}
