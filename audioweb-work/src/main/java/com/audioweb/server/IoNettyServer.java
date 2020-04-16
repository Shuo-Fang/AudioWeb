package com.audioweb.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.Threads;
import com.audioweb.server.handler.ServerHandler;
import com.audioweb.system.service.ISysConfigService;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
/***
 * 终端交互监听服务
 * @ClassName: IoNettyServer 
 * @Description: 终端交互监听服务
 * @author 10155 hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月16日 下午7:38:47
 */
public class IoNettyServer extends BaseNetty{
	
	private Channel channel;
	
	/**
	 * io线程池
	 */
    @Autowired
    @Qualifier("IoServiceExecutor")
    private ExecutorService io;

    @Autowired
    private ISysConfigService configService;
    
    private EventLoopGroup udpWorkerGroup = new NioEventLoopGroup(NUMBER_OF_CORES,io);
	
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
		ChannelFuture m = null;
        /*
         * io udp server 配置
         */
        try {
        	System.out.println("是否可用使用："+Epoll.isAvailable());
            Bootstrap d = new Bootstrap();
            d.group(udpWorkerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 100)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .localAddress(new InetSocketAddress(NettyConfig.getServerIp(),NettyConfig.getServerPort()))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            m = d.bind().sync();
            channel = m.channel();
            log.info("======IOServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null && m.isSuccess()) {
                log.info("Netty server listening  on port " + NettyConfig.getServerPort() + " and ready for connections...");
            } else {
                log.error(NettyConfig.getServerPort()+":Netty server start up Error!");
            }
        }
	}
	
	@Override
	public void destory() {
		log.info("Shutdown IoNetty Server...");
		if(channel != null) { channel.close();}
        udpWorkerGroup.shutdownGracefully();
        Threads.shutdownAndAwaitTermination(io);
        log.info("Shutdown IoNetty Server Success!");
	}
}
