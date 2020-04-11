/**   
 * @Title: IWorkServerService.java 
 * @Package com.audioweb.server.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月10日 上午11:31:26 
 * @version V1.0   
 */ 
package com.audioweb.server.service;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.server.IoNettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

/** 业务层管理终端方
 * @ClassName: IWorkServerService 
 * @Description: 业务层管理终端方实现
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月10日 上午11:31:26  
 */
public class WorkServerService {
	private IoNettyServer ioNettyServer = SpringUtils.getBean(IoNettyServer.class);
	private WorkServerService() {}
	private static WorkServerService service = new WorkServerService();
	
	public static WorkServerService getService() {
		return service;
	}
	/**
	 * 发送命令
	 * @Title: sendCommand 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param buffer
	 * @param address void 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年4月11日 下午12:26:48
	 */
	public void sendCommand(ByteBuffer buffer,InetSocketAddress address) {
		/*AsyncManager.me().ioExecute(new Runnable() {
			@Override
			public void run() {
				// IO线程池中运行
				if(timeout > 0) {
					try {
						Thread.sleep(timeout < 500 ? timeout:500);
					} catch (InterruptedException e) {
						// 延时长度，保证某些有序发送段能做到,防止线程池堵塞，最多延时500毫秒
						e.printStackTrace();
					}
				}*/
			try {
				ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(buffer), address));
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*}
		});*/
	}
	/**
	 * 发送命令
	 * @Title: sendCommand 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param buffer
	 * @param address
	 * @param timeout void 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年4月11日 下午12:26:48
	 */
	public void sendCommand(byte[] buffer, InetSocketAddress address, Long timeout) {
		AsyncManager.me().ioExecute(new Runnable() {
			@Override
			public void run() {
				// IO线程池中运行
				if(timeout > 0) {
					try {
						Thread.sleep(timeout < 500 ? timeout:500);
					} catch (InterruptedException e) {
						// 延时长度，保证某些有序发送段能做到,防止线程池堵塞，最多延时500毫秒
						e.printStackTrace();
					}
				}
				try {
					ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(buffer), address));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 发送命令
	 * @Title: sendCommand 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param buffer
	 * @param address
	 * @param timeout void 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年4月11日 下午12:26:48
	 */
	public void sendCommand(ByteBuf buffer, InetSocketAddress address, Long timeout) {
		AsyncManager.me().ioExecute(new Runnable() {
			@Override
			public void run() {
				// IO线程池中运行
				if(timeout > 0) {
					try {
						Thread.sleep(timeout < 500 ? timeout:500);
					} catch (InterruptedException e) {
						// 延时长度，保证某些有序发送段能做到,防止线程池堵塞，最多延时500毫秒
						e.printStackTrace();
					}
				}
				try {
					ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(buffer), address));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
