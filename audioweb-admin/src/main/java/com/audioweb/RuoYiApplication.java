package com.audioweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import com.audioweb.framework.websocket.BlobSocket;
import com.audioweb.framework.websocket.MessageSocket;
import com.audioweb.framework.websocket.WebSocketConfig;

/**
 * 启动程序
 * 
 * @author ruoyi,shuofang
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");//禁用热部署
	   SpringApplication.run(RuoYiApplication.class, args);
	  /* SpringApplication springApplication = new SpringApplication(RuoYiApplication.class);
	   ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
	   WebSocketConfig.setApplicationContext(configurableApplicationContext);//解决WebSocket不能注入的问题
*/       System.out.println("系统启动成功");
    }
}