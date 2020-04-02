package com.audioweb.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.audioweb.common.config.NettyConfig;
import com.audioweb.common.utils.Threads;
import com.audioweb.server.handler.TcpServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpNettyServer extends NettyBase{
	
	private Channel channel;
	
	/*
	 * tcp线程池
	 */
    @Autowired
    @Qualifier("TcpServiceExecutor")
	private ExecutorService tcp;
    
	private EventLoopGroup tcpWorkerGroup =	new NioEventLoopGroup(NUMBER_OF_CORES,tcp);
	
	@Override
	public Channel getChannel() {
		return channel;
	}
	
	@Override
	public void startServer() {
		ChannelFuture f = null;
		 /*
         * tcp server 配置
         */
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap b = new ServerBootstrap();
            EventLoopGroup tcpBossGroup = new NioEventLoopGroup(1);
            b.group(tcpBossGroup, tcpWorkerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG,64)
                    // 第2次握手服务端向客户端发送请求确认，同时把此连接放入队列A中，
                    // 然后客户端接受到服务端返回的请求后，再次向服务端发送请求，表示准备完毕，此时服务端收到请求，把这个连接从队列A移动到队列B中，
                    // 此时A+B的总数，不能超过SO_BACKLOG的数值，满了之后无法建立新的TCP连接,2次握手后和3次握手后的总数
                    // 当服务端从队列B中按照FIFO的原则获取到连接并且建立连接[ServerSocket.accept()]后，B中对应的连接会被移除，这样A+B的数值就会变小
                    //此参数对于程序的连接数没影响，会影响正在准备建立连接的握手。
                    //.option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,30000)//连接超时30000毫秒
                    .option(ChannelOption.SO_TIMEOUT,5000)//输入流的read方法被阻塞时，接受数据的等待超时时间5000毫秒，抛出SocketException
                    //child是在客户端连接connect之后处理的handler，不带child的是在客户端初始化时需要进行处理的
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//缓冲池
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(new TcpServerHandler());
                        }
                    });

            f = b.bind(new InetSocketAddress(NettyConfig.getQtClientPort())).sync();
            channel = f.channel();
            log.info("======TcpServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
            	log.info("Netty server listening  on port " + NettyConfig.getQtClientPort() + " and ready for connections...");
            } else {
                log.error(NettyConfig.getQtClientPort()+":Netty server start up Error!");
            }
        }
	}
	
	@Override
	public void destory() {
		log.info("Shutdown TcpNetty Server...");
		if(channel != null) { channel.close();}
        tcpWorkerGroup.shutdownGracefully();
        Threads.shutdownAndAwaitTermination(tcp);
        log.info("Shutdown TcpNetty Server Success!");
	}
}
