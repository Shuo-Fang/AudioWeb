package com.audioweb.framework.config;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.audioweb.common.config.Global;
import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.framework.interceptor.RepeatSubmitInterceptor;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.system.service.ISysConfigService;
import com.audioweb.work.service.IWorkCastTaskService;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.work.service.IWorkTerminalService;

/**
 * 通用配置
 * 
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
	/**定时刷新指定时间*/
	private static final String scheduleTime = "02:00:00";
    @Autowired
    private ISysConfigService configService;
	
	@Autowired
	private IWorkFileService workFileService;
	
	@Autowired
	private IWorkTerminalService workTerminalService;
	
	@Autowired
	private IWorkCastTaskService workCastTaskService;

    /**
     * 首页地址
     */
    @Value("${shiro.user.indexUrl}")
    private String indexUrl;

    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    /**
     * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("forward:" + indexUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        /**初始化netty服务器绑定IP地址*/
        NettyConfig.setServerIp(configService.selectConfigByKey(Constants.IP_CONFIG));
        /**初始化存储文本文件上传路径*/
        Global.setProfile(configService.selectConfigByKey(Constants.PREFIX_PATH));
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + Global.getProfile() + "/");
		 /** 文件广播路径 */
        Global.setFilePath(configService.selectConfigByKey(WorkConstants.FILECASTPATH));
		registry.addResourceHandler(Constants.AUDIO_FILE_PREFIX + "/**").addResourceLocations("file:" + Global.getFilePath() + "/");
		/** 终端点播路径 */
		Global.setPointPath(configService.selectConfigByKey(WorkConstants.POINTCASTPATH));
		registry.addResourceHandler(Constants.AUDIO_POINT_PREFIX + "/**").addResourceLocations("file:" + Global.getPointPath() + "/");
		/** 文字转音频路径 */
		Global.setWordPath((configService.selectConfigByKey(WorkConstants.WORDPATH)));
		registry.addResourceHandler(Constants.AUDIO_WORD_PREFIX + "/**").addResourceLocations("file:" + Global.getWordPath() + "/");
		 /**文件初始化刷新管理*/
        AsyncManager.me().execute(new TimerTask() {
			@Override
			public void run() {
		        /** 初始化路径信息 */
				Map<String, String> paths = new HashMap<String, String>();
				paths.put(WorkConstants.AUDIOFILETYPE, Global.getFilePath());
				paths.put(WorkConstants.AUDIOPOINTTYPE, Global.getPointPath());
				paths.put(WorkConstants.AUDIOWORDTYPE, Global.getWordPath());
				/** 启动时初始化一次文件信息*/
				workFileService.initWorkFiles(paths);
				/**启动时初始化一次广播终端信息*/
				workTerminalService.initWorkTerminals();
				/**启动时初始化一次广播终端信息*/
				workCastTaskService.deleteWorkCastTaskAll();
			}
		}, 10000);
    }

    /**
     * 自定义拦截规则 及 各类资源和定时任务初始化管理
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        /**定时每天刷新一次*/
        long oneDay = 24 * 60 * 60 * 1000;
        long initDelay  = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getDate()+" "+scheduleTime).getTime();
        AsyncManager.me().scheduleExecute(new TimerTask() {
			@Override
			public void run() {
				 /** 文件广播路径 */
		        String filePath = configService.selectConfigByKey(WorkConstants.FILECASTPATH);
		        /** 终端点播路径 */
				String pointPath = configService.selectConfigByKey(WorkConstants.POINTCASTPATH);
				/** 文字转音频路径 */
				String wordPath = configService.selectConfigByKey(WorkConstants.WORDPATH);
				Map<String, String> paths = new HashMap<String, String>();
				paths.put(WorkConstants.AUDIOFILETYPE, filePath);
				paths.put(WorkConstants.AUDIOPOINTTYPE, pointPath);
				paths.put(WorkConstants.AUDIOWORDTYPE, wordPath);
				workFileService.initWorkFiles(paths);
			}
		},initDelay,oneDay,TimeUnit.MILLISECONDS);
        
        /**端口监听服务器启动服务**/
    }
}