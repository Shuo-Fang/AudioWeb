package com.audioweb.server.service;

import java.util.List;
import java.util.TimerTask;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.work.domain.WorkTerminal;
/**
 * 定时延时执行判断终端是否入组
 * @ClassName: TimeReloady
 * @Description: 定时延时执行判断终端是否入组
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月16日 下午5:40:57
 */
public class TimeReloady extends TimerTask{
	private WorkTerminal tInfo = null;
	private List<WorkTerminal> workTerminals = null;
	
	public TimeReloady(WorkTerminal tInfo) {
		// TODO Auto-generated constructor stub
		this.tInfo = tInfo;
		if(StringUtils.isNotNull(tInfo.getCastTask())) {
			AsyncManager.me().execute(this, tInfo.getRetry()*1000);
		}
	}
	
	public TimeReloady(List<WorkTerminal> workTerminals) {
		// TODO Auto-generated constructor stub
		this.workTerminals = workTerminals;
		AsyncManager.me().execute(this, 1000);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (tInfo != null){
			infoIsCastReturn(tInfo);
		} else if(workTerminals != null){
			isCastReturn();
		}
	}
	
	
	/**
	 * 
	 * TODO	监测终端是否入组(分组)
	 * 时间：2019年1月11日
	 */
	private void isCastReturn() {
		for(WorkTerminal tInfo:workTerminals) {
			infoIsCastReturn(tInfo);
		}
	}
	
	/**
	 * 
	 * TODO	监测终端是否入组（单一终端）
	 * 时间：2019年1月11日
	 */
	private void infoIsCastReturn(WorkTerminal tInfo) {
		try {
			if(StringUtils.isNotNull(tInfo.getCastTask()) && tInfo.getIsOnline() < 2 && tInfo.getRetry() > 0) {
				//重新对指定终端发送入组命令
				if(WorkServerService.startCast(tInfo)) {
					new TimeReloady(tInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
