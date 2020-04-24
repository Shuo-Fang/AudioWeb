package com.audioweb.server.service.impl;

import java.util.TimerTask;

import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.server.service.SpringBeanServicePool;
import com.audioweb.work.domain.WorkTerminal;

/**
 * @author ShuoFang
 */
public class SpringBeanServiceImpl {
	private static SpringBeanServicePool service = SpringBeanServicePool.getService();
	/** 终端登录 */
	public static void loginTerminal(WorkTerminal terminal) {
		AsyncManager.me().execute(new TimerTask() {

			@Override
			public void run() {
				service.getTerminalServiceImpl().updateTerminalOnline(terminal);
			}
		},0);
	}
}
