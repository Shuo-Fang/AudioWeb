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
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.audioweb.server.protocol.InterCmdProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.server.IoNettyServer;
import com.audioweb.server.service.impl.WorkCastTaskServiceImpl;
import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkTerminal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
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
	private static Lock lock = new ReentrantLock();
	/**
	 * 发送命令 后续方法都是复写方法
	 * @Title: sendCommand
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param buffer
	 * @param address void 返回类型
	 * @throws
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
	 * TODO 指定终端停止指定广播
	 * 时间：2019年1月2日
	 */
	public static void endCast(WorkTerminal tInfo) {
		//在线并且有任务
		if(tInfo.getIsOnline() < 2 && StringUtils.isNotNull(tInfo.getCastTask())) {
			ByteBuffer byteBuffer =InterCmdProcess.sendCast(false,"", 0,0,tInfo.getCastTask().getCastType());
			//发送停止广播命令
			sendCommand(tInfo.getAdress(),0,byteBuffer);
			//try {
			//如果为点播则直接停止点播
			if(tInfo.getCastTask().getCastType() == CastWorkType.POINT) {
					//tInfo.getOrderCastInfo().get(0).getMct().close();
				}else if(tInfo.getCastTask().getCastType() == CastWorkType.CLIENT || tInfo.getCastTask().getCastType() == CastWorkType.PAGING) {

					//tInfo.getOrderCastInfo().get(0).getMct().close();//如果为被动终端采播则直接停止采播
				}
			/*} catch (Exception e) {
				e.printStackTrace();
			}*/
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
				delTerTask(tInfo,castTaskInfo,false);
			}
		}
	}
	/**
	 *
	 * @param tInfo
	 * TODO 终端与所给任务进行对比（若为第一任务）并开始广播
	 * 时间：2019年1月2日
	 */
	public static boolean startCast(WorkTerminal tInfo) {
		if(tInfo.getCastTask().getIsCast()) {
			sendCastCMD(tInfo,
					tInfo.getCastTask());
			return true;
		}else {
			delTerTask(tInfo,tInfo.getCastTask(),true);
			return false;
		}
	}
	/**
	 * 发送入组广播命令
	 * @param tInfo
	 * @param castTaskInfo
	 */
	private static void sendCastCMD(WorkTerminal tInfo,WorkCastTask castTaskInfo) {
		//是否包含源终端
		if(castTaskInfo.getCastType() != CastWorkType.CLIENT && castTaskInfo.getCastType() != CastWorkType.PAGING) {
			ByteBuffer senddata = InterCmdProcess.sendCast(true, castTaskInfo.getCastAddress(), castTaskInfo.getCastPort(),castTaskInfo.getVol(),castTaskInfo.getCastType());
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
		//发送入组广播命令
		sendCommand(tInfo.getAdress(),0,senddata);
	}
	/**
	 * 从单一终端删除指定任务信息
	 * @param tInfo 终端信息
	 * @param castTaskInfo 任务信息
	 * @param isSync 是否调用同步
	 */
	public static void delTerTask(WorkTerminal tInfo, WorkCastTask castTaskInfo, boolean isSync) {
		if(StringUtils.isNotNull(tInfo.getCastTask()) && tInfo.getCastTask().equals(castTaskInfo)) {
			endCast(tInfo);
			//后续补上
			if(tInfo.getCastTaskList().size() > 0) {
				//后续存在任务
				tInfo.setCastTask(tInfo.getCastTaskList().remove(0));
				//第一任务删除后第二任务自动开始广播
				if(startCast(tInfo) && isSync) {
					//检测是否入组
					new TimeReloady(tInfo);
				};
			}else {
				//后续没有任务
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
	private static void endTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal tInfo:workCastTask.getCastTeridlist()) {
			try {
				//正在广播此任务的终端
				if(workCastTask.equals(tInfo.getCastTask())) {
					//结束正在广播的任务
					//endCast(tInfo);
					//后续补上
					//后续存在任务
					if(tInfo.getCastTaskList().size() > 0) {
						tInfo.setCastTask(tInfo.getCastTaskList().remove(0));
					}else {//后续没有任务
						tInfo.setCastTask(null);
					}
					//后续排队任务
				}else if(tInfo.getCastTaskList().size() > 0) {
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
	private static void endStartTerCastTask(WorkCastTask workCastTask) {
		for (WorkTerminal ter:workCastTask.getCastTeridlist()) {
			if(StringUtils.isNotNull(ter.getCastTask())) {
				startCast(ter);
			}
		}
	}
	/**
	 *
	 * @param taskInfo
	 * TODO 添加备用任务入终端信息 添加对象锁，同一时间只能有一个线程调用此方法
	 * 时间：2019年1月2日
	 */
	public static void addAltTasks(WorkCastTask taskInfo) {
			lock.lock();
			try {
				//停止对应终端现在的任务
				startEndTerCastTask(taskInfo);
				//开始组播命令批量发送
				startTerCastTask(taskInfo);
				//定时检测终端入组情况
				new TimeReloady(taskInfo.getCastTeridlist());
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
	}
	/**
	 *
	 * @param taskInfo
	 * TODO 结束任务并通知备用任务入终端信息 添加对象锁，同一时间只能有一个线程调用此方法
	 * 时间：2019年1月2日
	 */
	private static void endTerTasks(WorkCastTask taskInfo) {
		AsyncManager.me().execute(new TimerTask() {
			@Override
			public void run() {
				lock.lock();
				try {
					endTerCastTask(taskInfo);//停止对应终端现在的任务
					endStartTerCastTask(taskInfo);//开始组播命令批量发送
					new TimeReloady(taskInfo.getCastTeridlist());//定时检测终端入组情况
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					lock.unlock();
				}
			}
		});
	}
	/**
	 * 停止任务
	 */
	public static void closeTask(WorkCastTask taskinfo) {
		//做类型判断
		switch (taskinfo.getCastType()) {
			//文件广播
			case FILE:
				FileCastTask task = (FileCastTask)taskinfo;
				//task.setIsCast(false);//将广播标识设置为停止广播,防止形成关闭死循环
				/**发送广播停止命令以及停止定时器*/
				task.setIsCast(false);
				task.getTimer().destoryTimer();
				/**关闭文件读取*/
				task.getRunFile().destory();
				/**关闭组播线程**/
				task.getServer().destory();
				/**清理终端广播相关信息*/
				endTerTasks(task);
				/**从任务中删除**/
				task.remove();
				break;

			default:
				break;
		}
	}
}
