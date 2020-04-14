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
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.server.IoNettyServer;
import com.audioweb.server.protocol.InterCMDProcess;
import com.audioweb.server.service.impl.WorkCastTaskServiceImpl;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkTerminal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

/** 业务层管理终端静态方法实现类
 * @ClassName: IWorkServerService 
 * @Description: 业务层管理终端静态方法实现类
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月10日 上午11:31:26  
 */
public class WorkServerService {
	private static final Logger  logger = LoggerFactory.getLogger(WorkCastTaskServiceImpl.class);
	private static IoNettyServer ioNettyServer = SpringUtils.getBean(IoNettyServer.class);
	private static AtomicInteger lock = new AtomicInteger(0);
	/**
	 * 发送命令 后续方法都是复写方法
	 * @Title: sendCommand 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param buffer
	 * @param address void 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年4月11日 下午12:26:48
	 */
	public static void sendCommand(InetSocketAddress address,ByteBuffer buffer) {
		try {
			ByteBuf buf = ioNettyServer.getChannel().alloc().directBuffer();
			buf.writeBytes(buffer);
			ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(buf, address));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendCommand(InetSocketAddress address, long timeout, ByteBuffer buffer) {
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
				sendCommand(address,buffer);
			}
		});
	}
	public static void sendCommand(InetSocketAddress address, byte[] buffer) {
		try {
			ByteBuf buf = ioNettyServer.getChannel().alloc().directBuffer();
			buf.writeBytes(buffer);
			ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(buf, address));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendCommand(InetSocketAddress address, long timeout,byte[] buffer) {
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
				sendCommand(address,buffer);
			}
		});
	}
	public static void sendCommand(InetSocketAddress address, ByteBuf... buffers) {
		try {
			CompositeByteBuf buf = ioNettyServer.getChannel().alloc().compositeBuffer();
			buf.addComponents(true,buffers);
			ioNettyServer.getChannel().writeAndFlush(new DatagramPacket(buf, address));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendCommand(InetSocketAddress address, long timeout,ByteBuf... buffers) {
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
				sendCommand(address,buffers);
			}
		});
	}
	
	/**
	 * 
	 * @param tInfo
	 * @param cInfo
	 * TODO 指定终端停止指定广播
	 * 时间：2019年1月2日
	 */
	public static void endCast(WorkTerminal tInfo) {
		if(tInfo.getIsOnline() < 2 && StringUtils.isNotNull(tInfo.getCastTask())) {//在线并且有任务
			ByteBuffer endbs =InterCMDProcess.sendCast(false,"", 0,0,tInfo.getCastTask().getCastType());
			sendCommand(tInfo.getAdress(),0,endbs);//发送停止广播命令
			try {
				if(tInfo.getCastTask().getCastType() == CastWorkType.POINT) {//如果为点播则直接停止点播
					//tInfo.getOrderCastInfo().get(0).getMct().close();
				}else if(tInfo.getCastTask().getCastType() == CastWorkType.CLIENT || tInfo.getCastTask().getCastType() == CastWorkType.PAGING) {
					
					//tInfo.getOrderCastInfo().get(0).getMct().close();//如果为被动终端采播则直接停止采播
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param tInfo
	 * TODO 终端与所给任务进行对比（若为第一任务）并开始广播
	 * 时间：2019年1月2日
	 */
	public static void startCast(WorkTerminal tInfo,WorkCastTask castTaskInfo) {
		if(tInfo.getIsOnline() < 2  && StringUtils.isNotNull(tInfo.getCastTask()) && tInfo.getCastTask().equals(castTaskInfo)) {
			if(castTaskInfo.getIsCast()) {
				sendCastCMD(tInfo,
					castTaskInfo);
			}else {
				delTerTask(tInfo,castTaskInfo);
			}
		}
	}
	/**
	 * 发送入组广播命令
	 * @param tInfo
	 * @param castTaskInfo
	 */
	private static void sendCastCMD(WorkTerminal tInfo,WorkCastTask castTaskInfo) {
		if(castTaskInfo.getCastType() != CastWorkType.CLIENT && castTaskInfo.getCastType() != CastWorkType.PAGING) {//是否包含源终端
			ByteBuffer senddata = InterCMDProcess.sendCast(true, castTaskInfo.getCastAddress(), castTaskInfo.getCastPort(),castTaskInfo.getVol(),castTaskInfo.getCastType());
			sendCastCommend(tInfo,senddata);
		}else {
			//带源终端的任务对源终端判断
			/*byte[] senddata ;
			if(!Const.CASTTYPE[3].equals(castTaskInfo.getMultiCastType())) {
				senddata = InterCMDProcess.sendMainTermCast(true, castTaskInfo.getMulticastaddress(), castTaskInfo.getMulticastport(),castTaskInfo.getMultiCastType());
			}else{
				senddata = InterCMDProcess.sendMainTermCast(true, castTaskInfo.getTypestr().get(2), Integer.parseInt(castTaskInfo.getTypestr().get(3)),castTaskInfo.getMultiCastType());
			}
			if(senddata != null && tInfo.getSession() != null) {
				sendCastCMD(tInfo, targetIP, senddata,castTaskInfo.getMultiCastType());
			} else {
				logger.error("发送源终端启用命令出错");
			}*/
		}
	}
	/**
	 * 发送入组广播命令
	 * @param tInfo
	 * @param senddata
	 */
	private static void sendCastCommend(WorkTerminal tInfo,ByteBuffer senddata) {
		logger.debug("第"+tInfo.addAndGetRetry()+"次发送"+tInfo.getCastTask().getCastType().getInfo()+"命令 -> 名称："+tInfo.getTerminalName()+"	地址："+tInfo.getAdress());
		sendCommand(tInfo.getAdress(),0,senddata);//发送入组广播命令
	}
	/**
	 * 
	 * @param tInfo castTaskInfo
	 * TODO 从单一终端删除指定任务信息
	 */
	public static void delTerTask(WorkTerminal tInfo,WorkCastTask castTaskInfo) {
		if(StringUtils.isNotNull(tInfo.getCastTask()) && tInfo.getCastTask().equals(castTaskInfo)) {
			endCast(tInfo);
			//后续补上
			if(tInfo.getCastTaskList().size() > 0) {//后续存在任务
				tInfo.setCastTask(tInfo.getCastTaskList().remove(0));
				startCast(tInfo,tInfo.getCastTask());//第一任务删除后第二任务自动开始广播
				new TimeReloady(tInfo);//检测是否入组
			}else {//后续没有任务
				tInfo.setCastTask(null);
			}
		}else {
			tInfo.getCastTaskList().remove(castTaskInfo);
		}
		/*List<CastTaskInfo> castTaskInfos = tInfo.getOrderCastInfo();
		if(castTaskInfos.size() == 1) {
			endCast(tInfo,castTaskInfo);
			tInfo.getOrderCastInfo().remove(0);
			RefreshTerData(tInfo);
		}else if(castTaskInfos.size() > 0) {
			int i = 0;
			synchronized (tInfo) {
				for(;i<castTaskInfos.size();i++) {
					if(castTaskInfos.get(i).equals(castTaskInfo)) {
						//EndCast(tInfo,castTaskInfo);
						tInfo.getOrderCastInfo().remove(i);
						break;
					}
				}
			}
			if(i == 0) {
				//Thread.sleep(5);//等待终端确定接收停止命令后
				StartCast(tInfo,castTaskInfos.get(0));//第一任务删除后第二任务自动开始广播
				new TimeReloady(tInfo);
			}
			RefreshTerData(tInfo);
		}*/
	}

	/**
	 * 开始任务时批量结束并更新指定广播任务所管辖的终端任务信息
	 * @param workCastTask
	 */
	private static void startEndTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal ter:workCastTask.getCastTeridlist()) {
			try {
				if(StringUtils.isNotNull(ter.getCastTask())) {
					/**若该终端有任务*/
					if(workCastTask.getCastLevel() <= ter.getCastTask().getCastLevel()) {//若此任务优先级比上一个任务优先级高
						endCast(ter);//发送停止广播命令
						ter.getCastTaskList().add(0,ter.getCastTask());//任务放入备选
						ter.setCastTask(workCastTask);//新任务放这
					}else {//新任务优先级不够
						ter.getCastTaskList().add(0,workCastTask);//任务放入备选
						Collections.sort(ter.getCastTaskList());//排序
					}
				}else {
					/**该终端不存在任务，直接新增即可*/
					ter.setCastTask(workCastTask);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    /**
     *  结束任务时批量结束并更新指定广播任务所管辖的终端任务信息
     */
	public static void endTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal tInfo:workCastTask.getCastTeridlist()) {
			try {
				if(workCastTask.equals(tInfo.getCastTask())) {//正在广播此任务的终端
					//结束正在广播的任务
					endCast(tInfo);
					//后续补上
					if(tInfo.getCastTaskList().size() > 0) {//后续存在任务
						tInfo.setCastTask(tInfo.getCastTaskList().remove(0));
						
					}else {//后续没有任务
						tInfo.setCastTask(null);
					}
				}else if(tInfo.getCastTaskList().size() > 0) {//后续排队任务
					tInfo.getCastTaskList().remove(workCastTask);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	/**
	 * 新增任务时开启分组终端广播
	 */
	private static void startTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal ter:workCastTask.getCastTeridlist()) {
			startCast(ter,workCastTask);
		}
	}
	/**
	 * 结束任务时开启分组终端广播
	 */
	public static void endStartTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal ter:workCastTask.getCastTeridlist()) {
			if(StringUtils.isNotNull(ter.getCastTask())) {
				startCast(ter,ter.getCastTask());
			}
		}
	}
	/**
	 * 
	 * @param tiInfo
	 * @param taskinfo
	 * TODO 添加备用任务入终端信息 添加对象锁，同一时间只能有一个线程调用此方法
	 * 时间：2019年1月2日
	 */
	public static void AddAltTasks(WorkCastTask taskinfo) {
		synchronized (lock) {
			if(lock.get() != 0) {
				try {
					lock.wait(500);//最多等待500ms
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lock.incrementAndGet();
			try {
				startEndTerCastTask(taskinfo);//停止对应终端现在的任务
				startTerCastTask(taskinfo);//开始组播命令批量发送
				new TimeReloady(taskinfo.getCastTeridlist());//定时检测终端入组情况
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.notify();
				lock.decrementAndGet();
			}
		}
	}
}
