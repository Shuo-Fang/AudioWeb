package com.audioweb.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
/**
 * 基础自定义netty接口
 * @ClassName: BaseNetty 
 * @Description: 基础自定义netty接口
 * @author 10155 hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月16日 下午7:34:43
 */
public abstract class BaseNetty {
	 protected final static int NUMBER_OF_CORES = Math.max(1, SystemPropertyUtil.getInt(
	            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors()));
	 protected static final Logger  log = LoggerFactory.getLogger(BaseNetty.class);
	 
	 /**
	  * 获取netty的channel
	  * @Title: getChannel 
	  * @Description: 获取netty的channel
	  * @return Channel 返回类型 
	  * @throws 抛出错误
	  * @author 10155 
	  * @date 2020年4月16日 下午7:35:12
	  */
	 public abstract Channel getChannel();
	 /***
	  * 开启netty服务，启动或者初始化时调用
	  * @Title: startServer 
	  * @Description: 开启netty服务，启动或者初始化时调用  void 返回类型 
	  * @throws 抛出错误
	  * @author 10155 
	  * @date 2020年4月16日 下午7:35:33
	  */
	 public abstract void startServer();
	 
	 /***
	  * 销毁这个netty服务或者监听
	  * @Title: destory 
	  * @Description: 销毁这个netty服务或者监听  void 返回类型 
	  * @throws 抛出错误
	  * @author 10155 
	  * @date 2020年4月16日 下午7:36:00
	  */
	 public abstract void destory();
	 
}
