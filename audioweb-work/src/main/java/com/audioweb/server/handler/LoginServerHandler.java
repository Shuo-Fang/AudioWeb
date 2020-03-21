/**   
 * @Title: LoginServerHandler.java 
 * @Package com.audioweb.serverPool.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年1月20日 下午3:36:20 
 * @version V1.0   
 */ 
package com.audioweb.server.handler;


import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.enums.ClientCommand;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.server.protocol.InterCMDProcess;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/** 终端登录信息处理
 * @ClassName: LoginServerHandler 
 * @Description: 终端登录信息处理
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年1月20日 下午3:36:20  
 */
public class LoginServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger  log = LoggerFactory.getLogger(TcpServerHandler.class);
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
		/**终端登录实现*/
		byte[] req = new byte[msg.content().readableBytes()];
		msg.content().readBytes(req);
		AsyncManager.me().ioExecute(new Runnable() {
			@Override
			public void run() {
				InetSocketAddress sAddress = (InetSocketAddress)(ctx.channel().localAddress());
				String ip = sAddress.getAddress().getHostAddress();
				/**判断是否为登陆命令*/
				if(ClientCommand.CMD_LOGIN.getCmd().equals(req[1]) && req.length > 9) {
					String terid = InterCMDProcess.getTeridFromLogin(req);
				}
				//log.info(msg.sender()+":"+req);
				//ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(req,CharsetUtil.UTF_8), msg.sender()));
			}
		});
	}
}
