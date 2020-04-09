package com.audioweb.server;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.work.domain.WorkCastTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.NetUtil;

public class GroupNettyServer extends NettyBase{
	
	private Channel channel;
	private WorkCastTask task;
	
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @author ShuoFang
	 * @date 2020年4月9日 下午4:26:20 
	 */
	public GroupNettyServer(WorkCastTask task) {
		this.task = task;
	}
	/*
	 * io线程池
	 */
    @Autowired
    @Qualifier("IoServiceExecutor")
	private ExecutorService io;
    
    private EventLoopGroup udpWorkerGroup = new NioEventLoopGroup(1,io);
	
	@Override
	public Channel getChannel() {
		return channel;
	}
	
	@Override
	public void startServer() {
		ChannelFuture m = null;
        /*
         * group udp server 配置
         */
        try {
			NetworkInterface ni = NetUtil.LOOPBACK_IF;
            Bootstrap d = new Bootstrap();
            d.group(udpWorkerGroup)
                    .channel(NioDatagramChannel.class)
        			.option(ChannelOption.IP_MULTICAST_IF, ni)
        			.option(ChannelOption.SO_REUSEADDR, true)
                    .localAddress(new InetSocketAddress(NettyConfig.getServerIp(),NettyConfig.getServerPort()))
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                        	channel.pipeline().addLast(new MulticastHandler());
                        }
                    });
            m = d.bind().sync();
            channel = m.channel();
            if (m.isSuccess()) {
            	log.debug("组播正常启动，任务编号："+task.getTaskId());
				task.setIsCast(true);
			}
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
		log.info("Shutdown GroupNetty Server...");
		if(channel != null) { channel.close();}
        udpWorkerGroup.shutdownGracefully();
        log.info("Shutdown GroupNetty Server Success!");
	}
}

class MulticastHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, DatagramPacket arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
