package com.audioweb.server.service;

import java.util.List;
import java.util.TimerTask;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.work.domain.WorkTerminal;

public class TimeReloady extends TimerTask{
	private WorkTerminal tInfo = null;
	private List<WorkTerminal> WorkTerminals = null;
	public TimeReloady(WorkTerminal tInfo) {
		// TODO Auto-generated constructor stub
		this.tInfo = tInfo;
		if(StringUtils.isNotNull(tInfo.getCastTask())) {
			AsyncManager.me().execute(this, tInfo.getRetry());
		}
	}
	public TimeReloady(List<WorkTerminal> WorkTerminals) {
		// TODO Auto-generated constructor stub
		this.WorkTerminals = WorkTerminals;
		AsyncManager.me().execute(this, 1000);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(tInfo != null)
			tInfoisCastCMDReturn(tInfo);
		else if(WorkTerminals != null) {
			isCastCMDReturn();
		}
	}
	
	
	/**
	 * 
	 * TODO	监测终端是否入组(分组)
	 * 时间：2019年1月11日
	 */
	private void isCastCMDReturn() {
		for(WorkTerminal tInfo:WorkTerminals) {
			tInfoisCastCMDReturn(tInfo);
		}
	}
	
	/**
	 * 
	 * TODO	监测终端是否入组（单一终端）
	 * 时间：2019年1月11日
	 */
	private void tInfoisCastCMDReturn(WorkTerminal tInfo) {
		try {
			if(StringUtils.isNotNull(tInfo.getCastTask()) && tInfo.getRetry() > 0) {
				//重新对指定终端发送入组命令
				WorkServerService.startCast(tInfo,tInfo.getCastTask());
				new TimeReloady(tInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
