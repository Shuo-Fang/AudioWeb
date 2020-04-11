/**   
 * @Title: LoginServerHandler.java 
 * @Package com.audioweb.serverPool.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年1月20日 下午3:36:20 
 * @version V1.0   
 */ 
package com.audioweb.server.handler;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.audioweb.common.enums.ClientCommand;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.protocol.InterCMDProcess;
import com.audioweb.server.service.WorkServerService;
import com.audioweb.server.service.impl.SpringBeanServiceImpl;
import com.audioweb.work.domain.WorkTerminal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
		byte[] req = new byte[msg.content().readableBytes()];
		msg.content().readBytes(req);
		/**终端登录实现 逻辑任务量不大，不采用线程池处理*/
/*        AsyncManager.me().ioExecute(new Runnable() {
        	@Override
        	public void run() {*/
				String ip = msg.sender().getAddress().getHostAddress();
				/**判断是否为登录命令*/
				if(ClientCommand.CMD_LOGIN.getCmd().equals(req[1]) && req.length > 9) {
					/**获取登录的ID号以及对应的校验IP地址*/
					String terid = InterCMDProcess.getTeridFromLogin(req);
					/**进行登录校验*/
					WorkTerminal terminal = WorkTerminal.getTerByIp(ip);
					if(StringUtils.isNotNull(terminal)) {
						/**IP验证*/
						if(terminal.getTerminalId().equals(terid)) {
							terminal.setLoginTime(new Date());
							terminal.setIsOnline(0);//存储登录信息
							log.info("终端登录成功："+terid);
							ByteBuf buf = ctx.alloc().directBuffer();
							buf.writeBytes(InterCMDProcess.returnLoginBytes());
							//WorkServerService.getService().sendCommand(InterCMDProcess.returnLoginBytes(), msg.sender());
							InetSocketAddress address = null;
							try {
								address = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 10001);
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ctx.writeAndFlush(new DatagramPacket(buf, address));
							SpringBeanServiceImpl.loginTerminal(terminal);
						}else {
							log.info("登录终端IP配置有误："+terid);
						}
					}else {
						log.info("登录终端配置不存在");
					}
				}
/*        	}
        });*/
		//log.info(msg.sender()+":"+req);
		//ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(req,CharsetUtil.UTF_8), msg.sender()));
	}
}
