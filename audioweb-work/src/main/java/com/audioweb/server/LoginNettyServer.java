package com.audioweb.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.Threads;
import com.audioweb.server.handler.LoginServerHandler;
import com.audioweb.system.service.ISysConfigService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LoginNettyServer extends NettyBase{
	
	private Channel channel;
	
	/*
	 * io线程池
	 */
    @Autowired
    @Qualifier("IoServiceExecutor")
	private ExecutorService io;
    
    @Autowired
    private ISysConfigService configService;
    
    private EventLoopGroup loginWorkerGroup =	new NioEventLoopGroup(NUMBER_OF_CORES,io);
	
	@Override
	public Channel getChannel() {
		return channel;
	}
	
	@Override
	public void startServer() {
	   /**初始化netty服务器绑定IP地址*/
	   if(StringUtils.isEmpty(NettyConfig.getServerIp())) {
		   NettyConfig.setServerIp(configService.selectConfigByKey(Constants.IP_CONFIG));
	   }
       ChannelFuture l = null;
        /*
         * login udp server 配置
         */
        try {
            Bootstrap c = new Bootstrap();
            c.group(loginWorkerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 100)
                    .localAddress(new InetSocketAddress(NettyConfig.getServerIp(),NettyConfig.getLoginPort()))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LoginServerHandler());
                        }
                    });
            l = c.bind().sync();
            channel = l.channel();
            log.info("======loginServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (l != null && l.isSuccess()) {
                log.info("Netty server listening  on port " + NettyConfig.getLoginPort() + " and ready for connections...");
            } else {
                log.error(NettyConfig.getLoginPort()+":Netty server start up Error!");
            }
        }
	}
	
	@Override
	public void destory() {
		log.info("Shutdown LoginNetty Server...");
		if(channel != null) { channel.close();}
		loginWorkerGroup.shutdownGracefully();
        Threads.shutdownAndAwaitTermination(io);
        log.info("Shutdown LoginNetty Server Success!");
	}
}
