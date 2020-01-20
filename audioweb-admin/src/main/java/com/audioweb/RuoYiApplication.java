package com.audioweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import com.audioweb.framework.websocket.BlobSocket;
import com.audioweb.framework.websocket.MessageSocket;
import com.audioweb.framework.websocket.WebSocketConfig;
import com.audioweb.serverPool.NettyServer;

import io.netty.channel.ChannelFuture;

/**
 * 启动程序
 * 
 * @author ruoyi,shuofang
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication implements CommandLineRunner 
{
	@Value("${netty.loginPort}")
    private int loginPort;

    @Value("${netty.serverIp}")
    private String serverIp;
    
	@Autowired
	NettyServer server;
	
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
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		ChannelFuture future = server.startLoginServer(serverIp,loginPort);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
            	server.destroy();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
	}
}