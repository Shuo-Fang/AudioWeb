/**   
 * @Title: NettyServer.java 
 * @Package com.audioweb.serverPool 
 * @Description: 配置netty UDP服务端监听
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年1月20日 下午1:51:38 
 * @version V1.0   
 */ 
package com.audioweb.serverPool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.audioweb.common.config.datasource.DynamicDataSourceContextHolder;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.serverPool.service.LoginServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
	@Value("netty.islinux")
	private String islinux;
	/**
     *  获取活跃的 cpu数量
     */
    private final static int NUMBER_OF_CORES = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors()));
    public static final Logger  log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
	//netty主从模式,考虑多终端登陆与通信
	private final EventLoopGroup loginBossGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES,(Executor)SpringUtils.getBean("BossServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES,(Executor)SpringUtils.getBean("BossServiceExecutor"));
	private final EventLoopGroup udpBossGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES,(Executor)SpringUtils.getBean("BossServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES,(Executor)SpringUtils.getBean("BossServiceExecutor"));
	private final EventLoopGroup tcpBossGroup = islinux != null && islinux.equals("1")?
			new EpollEventLoopGroup(NUMBER_OF_CORES/2>0?NUMBER_OF_CORES/2:1,(Executor)SpringUtils.getBean("BossServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES/2>0?NUMBER_OF_CORES/2:1,(Executor)SpringUtils.getBean("BossServiceExecutor"));
	private final EventLoopGroup loginWorkerGroup = islinux != null && islinux.equals("1")?
			new NioEventLoopGroup(NUMBER_OF_CORES*2,(Executor)SpringUtils.getBean("IoServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES*2,(Executor)SpringUtils.getBean("IoServiceExecutor"));
	private final EventLoopGroup udpWorkerGroup = islinux != null && islinux.equals("1")?
			new NioEventLoopGroup(NUMBER_OF_CORES*4,(Executor)SpringUtils.getBean("IoServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES*4,(Executor)SpringUtils.getBean("IoServiceExecutor"));
	private final EventLoopGroup tcpWorkerGroup = islinux != null && islinux.equals("1")?
			new NioEventLoopGroup(NUMBER_OF_CORES/2>0?NUMBER_OF_CORES/2:1,(Executor)SpringUtils.getBean("ServerServiceExecutor")):
			new NioEventLoopGroup(NUMBER_OF_CORES/2>0?NUMBER_OF_CORES/2:1,(Executor)SpringUtils.getBean("ServerServiceExecutor"));
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
	public ChannelFuture  startLoginServer(String hostname,int port) throws Exception {
        ChannelFuture f = null;
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap b = new ServerBootstrap();
            b.group(loginBossGroup, loginWorkerGroup)
                    .channel(islinux != null && islinux.equals("1")?EpollServerSocketChannel.class:NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(hostname,port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(new LoginServerHandler());
                        }
                    });

            f = b.bind().sync();
            channel = f.channel();
            log.info("======LoginServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return f;
    }
	/**
     * 停止服务
     */
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        loginBossGroup.shutdownGracefully();
        loginWorkerGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
