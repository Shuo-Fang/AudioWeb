/**   
 * @Title: NettyServer.java 
 * @Package com.audioweb.serverPool 
 * @Description: 配置netty TCP UDP服务端监听
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年1月20日 下午1:51:38 
 * @version V1.0   
 */ 
package com.audioweb.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.audioweb.common.config.datasource.DynamicDataSourceContextHolder;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.server.handler.LoginServerHandler;
import com.audioweb.server.handler.TcpServerHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/** 
 * @ClassName: NettyServer 
 * @Description: 配置netty 服务端监听
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年1月20日 下午1:51:38  
 */
@Component
public class NettyServer {
	
	@Value("${netty.islinux}")
	private String islinux;
	
	@Value("${netty.serverPort}")
	private String serverPort;
	
	@Value("${netty.loginPort}")
	private String loginPort;
	
	@Value("${netty.qtClientPort}")
	private String qtClientPort;
	/*
	 * boss线程池
	 */
	//private Executor boss = (Executor)SpringUtils.getBean("BossServiceExecutor");
	/*
	 * io线程池
	 */
	private Executor io = (Executor)SpringUtils.getBean("IoServiceExecutor");
	/*
	 * tcp线程池
	 */
	private Executor tcp = (Executor)SpringUtils.getBean("TcpServiceExecutor");
	/*
     *  获取活跃的 cpu数量
     */
    private final static int NUMBER_OF_CORES = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors()));
    public static final Logger  log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
	private final EventLoopGroup loginWorkerGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES,io):
			new NioEventLoopGroup(NUMBER_OF_CORES,io);
	private final EventLoopGroup udpWorkerGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES*3,io):
			new NioEventLoopGroup(NUMBER_OF_CORES*3,io);
	private final EventLoopGroup tcpWorkerGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES*2,tcp):
			new NioEventLoopGroup(NUMBER_OF_CORES*2,tcp);
	private Channel channel;
	/**
	 * 启动loginServer
	 * @Title: startLoginServer 
	 * @Description: 启动loginServer
	 * @param hostname
	 * @param port
	 * @return
	 * @throws Exception ChannelFuture 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年1月20日 下午3:38:40
	 */
	public List<ChannelFuture> startServer() throws Exception {
		List<ChannelFuture> list= new ArrayList<>();
        ChannelFuture f = null;
        ChannelFuture l = null;
        ChannelFuture m = null;
        /*
         * tcp server 配置
         */
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap b = new ServerBootstrap();
            EventLoopGroup tcpBossGroup = new NioEventLoopGroup(1);
            b.group(tcpBossGroup, tcpWorkerGroup)
                    .channel(islinux != null && islinux.equals("1")?EpollServerSocketChannel.class:NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG,64)
                    // 第2次握手服务端向客户端发送请求确认，同时把此连接放入队列A中，
                    // 然后客户端接受到服务端返回的请求后，再次向服务端发送请求，表示准备完毕，此时服务端收到请求，把这个连接从队列A移动到队列B中，
                    // 此时A+B的总数，不能超过SO_BACKLOG的数值，满了之后无法建立新的TCP连接,2次握手后和3次握手后的总数
                    // 当服务端从队列B中按照FIFO的原则获取到连接并且建立连接[ServerSocket.accept()]后，B中对应的连接会被移除，这样A+B的数值就会变小
                    //此参数对于程序的连接数没影响，会影响正在准备建立连接的握手。
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,30000)//连接超时30000毫秒
                    .option(ChannelOption.SO_TIMEOUT,5000)//输入流的read方法被阻塞时，接受数据的等待超时时间5000毫秒，抛出SocketException
                    .localAddress(new InetSocketAddress(Integer.parseInt(qtClientPort)))
                    //child是在客户端连接connect之后处理的handler，不带child的是在客户端初始化时需要进行处理的
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//缓冲池
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(new TcpServerHandler());
                        }
                    });

            f = b.bind().sync();
            channel = f.channel();
            log.info("======TcpServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("Netty server listening  on port " + qtClientPort + " and ready for connections...");
                list.add(f);
            } else {
                log.error(qtClientPort+":Netty server start up Error!");
            }
        }
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
                    .localAddress(new InetSocketAddress(Integer.parseInt(loginPort)))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LoginServerHandler());
                        }
                    });
            l = c.bind().sync();
            log.info("======loginServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (l != null && l.isSuccess()) {
                log.info("Netty server listening  on port " + loginPort + " and ready for connections...");
                list.add(l);
            } else {
                log.error(loginPort+":Netty server start up Error!");
            }
        }
        /*
         * io udp server 配置
         */
        try {
            Bootstrap d = new Bootstrap();
            d.group(udpWorkerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 100)
                    .localAddress(new InetSocketAddress(Integer.parseInt(serverPort)))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LoginServerHandler());
                        }
                    });
            m = d.bind().sync();
            log.info("======IOServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null && m.isSuccess()) {
                log.info("Netty server listening  on port " + serverPort + " and ready for connections...");
                list.add(m);
            } else {
                log.error(serverPort+":Netty server start up Error!");
            }
        }
        return list;
    }
	/**
     * 停止服务
     */
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        tcpWorkerGroup.shutdownGracefully();
        udpWorkerGroup.shutdownGracefully();
        loginWorkerGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
