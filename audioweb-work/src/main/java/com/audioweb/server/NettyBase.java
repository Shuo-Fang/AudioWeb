package com.audioweb.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

public abstract class NettyBase {
	 protected final static int NUMBER_OF_CORES = Math.max(1, SystemPropertyUtil.getInt(
	            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors()));
	 protected static final Logger  log = LoggerFactory.getLogger(NettyBase.class);
	 
	 
	 public abstract Channel getChannel();
	 
	 public abstract void startServer();
	 
	 public abstract void destory();
	 
}
