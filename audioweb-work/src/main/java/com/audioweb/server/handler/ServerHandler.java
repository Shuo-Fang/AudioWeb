package com.audioweb.server.handler;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.utils.spring.SpringUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
/**
 * 终端通信处理
 * @ClassName: ServerHandler 
 * @Description: 终端通信处理
 * @author 10155 hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月1日 下午2:13:57
 */
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger  log = LoggerFactory.getLogger(ServerHandler.class);
	private Executor io = (Executor)SpringUtils.getBean("IoServiceExecutor");
	/**
     * 在读取操作期间，有异常抛出时会调用。
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


	/* (non-Javadoc) 
	 * <p>Title: channelRead0</p> 
	 * <p>Description: </p> 
	 * @author ShuoFang 
	 * @date 2020年2月28日 下午3:39:43
	 * @param ctx
	 * @param msg
	 * @throws Exception 
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object) 
	 */ 
	
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx,final DatagramPacket msg) throws Exception {
		// TODO Auto-generated method stub
        //System.out.println(req);
        //Threads.sleep(1000);
		String req = msg.content().toString(CharsetUtil.UTF_8);
		io.execute(new Runnable() {
			@Override
			public void run() {
				log.info(msg.sender()+":"+req);
				// TODO Auto-generated method stub
				ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(req,CharsetUtil.UTF_8), msg.sender()));
			}
		});
		msg.release();
	}
}

