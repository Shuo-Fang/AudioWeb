package com.audioweb.framework.config;

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
import com.audioweb.system.service.ISysConfigService;

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
    }

    /**
     * 自定义拦截规则 及 各类资源和定时任务初始化管理
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }
}