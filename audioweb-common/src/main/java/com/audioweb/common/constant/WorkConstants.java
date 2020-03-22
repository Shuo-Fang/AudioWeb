/**   
 * @Title: TerminalConstants.java 
 * @Package com.audioweb.common.constant 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月2日 上午11:01:41 
 * @version V1.0   
 */ 
package com.audioweb.common.constant;

/** 
 * @ClassName: TerminalConstants 
 * @Description: 终端常量信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月2日 上午11:01:41  
 */
public class WorkConstants {

    /** 终端IP地址是否唯一的返回结果码 */
    public final static String TERMINAL_IP_UNIQUE = "0";
    public final static String TERMINAL_IP_NOT_UNIQUE = "1";
    
    /** 终端ID是否唯一的返回结果码 */
    public final static String TERMINAL_ID_UNIQUE = "0";
    public final static String TERMINAL_ID_NOT_UNIQUE = "1";
    
    /** 文件广播路径配置信息KEY */
    public final static String FILECASTPATH = "work.file";
    /** 终端点播路径配置信息KEY */
    public final static String POINTCASTPATH = "work.point";
    /** 文字转音频路径配置信息KEY */
    public final static String WORDPATH = "work.word";
    
    /** 音频文件的状态码*/
    public final static String AUDIOFILENORMAL = "0";
    public final static String AUDIOFILENOTFOND = "1";
    public final static String AUDIOFILEDESTOY = "2";
    
    /** 丢失文件的最长存留信息 默认为7天 */
    public final static Long AUDIOFILENOTFONDDATE = (long) (7*24*60*60*1000);
    
    /** 音频文件所属类型的状态码 0为文件广播,1为终端点播,2为文字转语音*/
    public final static String AUDIOFILETYPE = "0";
    public final static String AUDIOPOINTTYPE = "1";
    public final static String AUDIOWORDTYPE = "2";
    /** 通用状态 0为正常，1为异常或停用*/
    public final static String NORMAL = "0";
    public final static String DESTOY = "1";
}
