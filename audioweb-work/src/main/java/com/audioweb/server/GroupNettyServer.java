package com.audioweb.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.utils.IpUtils;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.work.domain.WorkCastTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
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
	private InetSocketAddress target;
	
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @author ShuoFang
	 * @date 2020年4月9日 下午4:26:20 
	 */
	public GroupNettyServer(WorkCastTask task) {
		this.task = task;
		startServer();
	}
	/*
	 * io线程池
	 */
	private ExecutorService io = SpringUtils.getBean("IoServiceExecutor");
    
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
            InetSocketAddress address = verifyBindHost();
            task.setCastPort(address.getPort());
            task.setCastAddress(NettyConfig.getAdress());
            target = new InetSocketAddress(task.getCastAddress(),task.getCastPort());
			NetworkInterface ni = NetUtil.LOOPBACK_IF;
            Bootstrap d = new Bootstrap();
            d.group(udpWorkerGroup)
                    .channel(NioDatagramChannel.class)
        			.option(ChannelOption.IP_MULTICAST_IF, ni)
        			.option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .localAddress(address)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                        	channel.pipeline().addLast(new MulticastHandler());
                        }
                    });
            m = d.bind().sync();
            channel = m.channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null && m.isSuccess()) {
            	log.info("组播正常启动，任务编号："+task.getTaskId()+"地址:"+target);
				task.setIsCast(true);
            } else {
                log.error("组播启动失败，任务编号："+task.getTaskId()+"	-Netty server start up Error!");
            }
        }
	}
	
	@Override
	public void destory() {
		log.info("Shutdown GroupNetty Server...");
		if(channel != null) { 
			channel.close();
		}
		udpWorkerGroup.shutdownGracefully();
        log.info("Shutdown GroupNetty Server Success!");
	}
	
	/**发送组播数据*/
	public void sendData(ByteBuffer buffer) {
		try {
			ByteBuf buf = channel.alloc().directBuffer(buffer.remaining());
			buf.writeBytes(buffer);
			channel.writeAndFlush(new DatagramPacket(buf,target)).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**发送组播数据*/
	public void sendData(ByteBuf... buffers) {
		try {
			CompositeByteBuf buf = channel.alloc().compositeBuffer();
			buf.addComponents(buffers);
			channel.writeAndFlush(new DatagramPacket(buf,target)).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**发送组播数据*/
	public void sendData(byte[] buffer) {
		try {
			ByteBuf buf = channel.alloc().directBuffer();
			buf.writeBytes(buffer);
			channel.writeAndFlush(new DatagramPacket(buf,target)).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**获取并校验端口可用性*/
	private InetSocketAddress verifyBindHost() {
		int multicastport = NettyConfig.getGroupPort();
		InetAddress localAddress = null;
		List<String> list = IpUtils.getLocalIPList();
    	String ip = NettyConfig.getServerIp();
		try {
	    	if(ip != null && list.contains(ip)) {
				localAddress = InetAddress.getByName(ip);
	    	}else {
				localAddress = IpUtils.getLocalHostLANAddress();
	    	}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	while(!bindPort(localAddress,multicastport)) {
    		multicastport = NettyConfig.getGroupPort();
    	}
		return new InetSocketAddress(localAddress,multicastport);
	}

	/**
	 * @Title: bindPort 
	 * @Description: 能否绑定端口测试
	 * @param localAddress
	 * @param multicastport
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年4月10日 上午10:06:19
	 */
	private boolean bindPort(InetAddress localAddress, int multicastport) {
		boolean flag=false;
		try {
			Socket s = new Socket(); 
			s.bind(new InetSocketAddress(localAddress, multicastport)); 
			s.close();
			flag=true;
		} catch (Exception e) {
			flag=false;
		} 
		return flag;
	}
}

class MulticastHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, DatagramPacket arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
