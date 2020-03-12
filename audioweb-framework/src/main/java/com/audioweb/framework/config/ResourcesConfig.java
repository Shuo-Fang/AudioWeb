package com.audioweb.framework.config;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.framework.interceptor.RepeatSubmitInterceptor;
import com.audioweb.framework.manager.AsyncManager;
import com.audioweb.system.service.ISysConfigService;
import com.audioweb.work.service.IWorkFileService;

/**
 * 通用配置
 * 
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private ISysConfigService configService;
	
	@Autowired
	private IWorkFileService workFileService;

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
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + configService.selectConfigByKey(Constants.PREFIX_PATH) + "/");
		 /** 文件广播路径 */
        String filePath = configService.selectConfigByKey(WorkConstants.FILECASTPATH);
		registry.addResourceHandler(Constants.AUDIO_FILE_PREFIX + "/**").addResourceLocations("file:" + filePath + "/");
		/** 终端点播路径 */
		String pointPath = configService.selectConfigByKey(WorkConstants.POINTCASTPATH);
		registry.addResourceHandler(Constants.AUDIO_POINT_PREFIX + "/**").addResourceLocations("file:" + pointPath + "/");
		/** 文字转音频路径 */
		String wordPath = configService.selectConfigByKey(WorkConstants.WORDPATH);
		registry.addResourceHandler(Constants.AUDIO_WORD_PREFIX + "/**").addResourceLocations("file:" + wordPath + "/");
        /** 初始化路径文件信息 */
        AsyncManager.me().execute(new TimerTask() {
			
			@Override
			public void run() {
				Map<String, String> paths = new HashMap<String, String>();
				paths.put(WorkConstants.AUDIOFILETYPE, filePath);
				paths.put(WorkConstants.AUDIOPOINTTYPE, pointPath);
				paths.put(WorkConstants.AUDIOWORDTYPE, wordPath);
				/** 启动时初始化一次文件信息*/
				workFileService.initWorkFiles(paths);
			}
		},10000);
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }
}