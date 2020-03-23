package com.audioweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.audioweb.server.ServerManager;

/**
 * 启动程序
 * 
 * @author ruoyi,shuofang
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication// implements CommandLineRunner 
{
	@Autowired
	ServerManager server;
	
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");//禁用热部署
	   SpringApplication.run(RuoYiApplication.class, args);
	  /* SpringApplication springApplication = new SpringApplication(RuoYiApplication.class);
	   ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
	   WebSocketConfig.setApplicationContext(configurableApplicationContext);//解决WebSocket不能注入的问题
*/       System.out.println("系统启动成功");
    }

	/* (non-Javadoc) 
	 * <p>Title: run</p> 
	 * <p>Description: </p> 
	 * @author ShuoFang 
	 * @date 2020年1月20日 下午3:42:25
	 * @param args
	 * @throws Exception 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[]) 
	 */ 
	
/*	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		server.startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
            	server.destory();
            }
        });
	}*/
}