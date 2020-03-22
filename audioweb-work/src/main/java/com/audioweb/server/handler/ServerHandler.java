package com.audioweb.server.handler;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.enums.ClientCommand;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.spring.SpringUtils;

import io.netty.buffer.ByteBuf;
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
		ByteBuf content = msg.content();
		AsyncManager.me().ioExecute(new Runnable() {
			@Override
			public void run() {
				String ip = msg.sender().getAddress().getHostAddress();
				Byte audio = content.readByte();
				Byte command = content.readByte();
				/**为数据音频包*/
				if(ClientCommand.CMD_PACKAGE.getCmd().equals(audio)) {
					/**若为音频包，则直接进行转发操作*/
					/**后续补充测试*/
				}else {
					ClientCommand cmd = ClientCommand.valueOf(command);
					byte[] req = new byte[msg.content().readableBytes()];
					msg.content().readBytes(req);
					/**为命令包，进行判断**/
					switch(cmd){
					/**终端心跳包*/
					case CMD_NETHEART:
						
						break;
					default:
						
						break;
					}
				}
			}
			
		});
	}
}

