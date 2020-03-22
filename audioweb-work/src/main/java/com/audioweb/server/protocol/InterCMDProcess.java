package com.audioweb.server.protocol;

import java.nio.ByteBuffer;
import org.springframework.beans.factory.annotation.Value;

import com.audioweb.common.core.text.CharsetKit;
import com.audioweb.common.core.text.Convert;
import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.enums.ClientCommand;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.IpUtils;
import com.audioweb.common.utils.StringUtils;

/**
 * 终端服务器交互指令解析与打包
 * @author HTT
 */
public class InterCMDProcess {
	/** 心跳检测接收端口默认6970 */
	@Value("${netty.serverPort}")
	private static int netHeartRecPort;
	/** ByteBuffer默认指定大小 */
	private static final Integer NORMALSIZE = 50;
	
	//private static final SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd  HHmmss");
	/*******************解析接收到的数据*************************/
	/**
	 * 从收到的数据包解析登录终端编号
	 * @param content
	 * @return terid
	 */
	public static String getTeridFromLogin(byte[] content){
		byte[] ids = new byte[4];
		System.arraycopy(content, 6, ids, 0, ids.length);
		return Convert.str(ids, CharsetKit.GB2312);
	}
	
	/*******************编码需要发送或者返回到终端的数据***************************/
	/**
	 * 终端登录响应返回数据
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer returnLoginBytes(){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		encoded.put(ClientCommand.CMD_LOGIN.getCmd());
		encoded.put((byte)18);
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		encoded.put(ClientCommand.CMD_HAVE.getCmd());
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		String dateString = netHeartRecPort+DateUtils.dateTimeNow(DateUtils.YYMMDD__HHMMSS);//formatter.format(new Date());
		return sendStringToBytes(encoded,dateString,null);
	}
	/**
	 * 终端心跳包响应返回数据
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer returnNetHeart(){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		encoded.put(ClientCommand.CMD_NETHEART.getCmd());
		for(int i=2;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		return sendStringToBytes(encoded,"1",null);
	}
	/**
	 * 终端点播初始化列表
	 * @return 需要发送的ByteBuffer
	 */
	/*public static ByteBuffer vodFileList(String path){
		byte[] imot = TerminalUnicast.getFilesName(path);
		ByteBuffer encoded = ByteBuffer.allocate(imot.length+7);
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		if(path == null) {
			encoded.put(ClientCommand.CMD_VODFILELIST.getCmd());
		}
		else{
			encoded.put(ClientCommand.CMD_VODFILECAST.getCmd());
		}
		encoded.put(Convert.intToBytes(imot.length, 2));//音频包长度
		encoded.put(ClientCommand.CMD_HAVE.getCmd());
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		return sendStringToBytes(encoded,null,imot);
	}*/
	/**
	 * 寻呼话筒终端点播初始化列表
	 * @return 需要发送的ByteBuffer
	 */
	/*public static ByteBuffer vodDomainList(TerminalInfo tInfo,String domain){
		byte[] imot = TerminalUnicast.Unicastdomain(tInfo,domain);
		ByteBuffer encoded;
		if(imot != null)
			encoded = ByteBuffer.allocate(imot.length+ClientCommand.CMD_NORMAL.getCmd());
		else
			encoded = ByteBuffer.allocate(ClientCommand.CMD_NORMAL.getCmd());
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		if(domain == null)
			encoded.put(ClientCommand.CMD_CMICCAST.getCmd());
		else
			encoded.put(ClientCommand.CMD_CMICELIST.getCmd());
		byte[] length;
		if(imot != null)
			length = Convert.intToBytes(imot.length,2);//音频包长度
		else {
			length = Convert.intToBytes(0, 2);
		}
		encoded.put(length);
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		encoded.put((byte)20);
		return sendStringToBytes(encoded,null,imot);
	}*/
	/**
	 * 寻呼话筒终端点播使能
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer getEnable(int num){
		ByteBuffer encoded = ByteBuffer.allocate(ClientCommand.CMD_NORMAL.getCmd());
		encoded.put(ClientCommand.CMDTYPE_TERCONTROL.getCmd());
		encoded.put(ClientCommand.CMD_CMICREPLY.getCmd());
		encoded.put(ClientCommand.CMD_NORMAL.getCmd());//音频包长度
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		encoded.put(ClientCommand.CMD_HAVE.getCmd());
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		encoded.put((byte)num);
		return sendStringToBytes(encoded,null,null);
	}
	/**
	 * 寻呼话筒任务创建回复
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer reply(String groupip,String groupPort){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		encoded.put(ClientCommand.CMD_CMICENABLE.getCmd());
		for(int i=0;i<4;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		encoded.put(IpUtils.textToNumericFormatV4(groupip));
		return sendStringToBytes(encoded,groupPort,null);
	}
	/**
	 * 
	 * @Title: vodFileCast 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param type 命令类型
	 * @param imot	是否为文件播放完成停止 1为完成，0为未完成
	 * @return ByteBuffer 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月18日 下午9:42:51
	 */
	public static ByteBuffer vodFileCast(ClientCommand type,String imot){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_SERVERRETURN.getCmd());
		if(type == ClientCommand.CMD_STOPVOD && imot.equals("1")){//点播停止
			encoded.put(0, ClientCommand.CMDTYPE_TERCONTROL.getCmd());
			
		}
		encoded.put(type.getCmd());
		encoded.put(ClientCommand.CMD_HAVE.getCmd());
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		encoded.put(ClientCommand.CMD_HAVE.getCmd());
		encoded.put(ClientCommand.CMD_NONE.getCmd());
		return sendStringToBytes(encoded,null,Convert.isoToBytes(imot));
	}

	/**
	 * 发送广播命令汇总处理->非源终端
	 * @param isstart 是开始还是结束，true开始，false结束
	 * @param multiCastIp 组播IP地址，isstart为true时必须
	 * @param targetPort 终端接收组播端口号，isstart为true时必须，必须为5位数
	 * @param vol 音量0-40
	 * @param type 命令类型以及对应指令
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer sendCast(Boolean isstart,String multiCastIp, int targetPort,int vol,CastWorkType type){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_TERCONTROL.getCmd());
		switch(type) {
		case FILE:
			encoded.put(ClientCommand.CMD_FILECAST.getCmd());
			break;
		case TIME:
			encoded.put(ClientCommand.CMD_TIMINGCAST.getCmd());
			break;
		case REAL:
			encoded.put(ClientCommand.CMD_FILECAST.getCmd()/*CMD_PIC_SEND*/);
			break;
		case PLUG:
			encoded.put(ClientCommand.CMD_FILECAST.getCmd());
			break;
		case CLIENT:
			encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
			break;
		/*case "终端点播":
			encoded.put(Convert.toByte(CMD_FILECAST));
			break;*/
		case PAGING:
			encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
			break;
		default: 
			encoded.put(ClientCommand.CMD_FILECAST.getCmd());
			break;
		}
		
		for(int i=2;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		if(isstart){//开始
			String str = "1";
			/*if(types.get(0).equals("3")) {
				if(types.size()>4) {
					str = "1"+types.get(2)+types.get(3);
					types.remove(4);//移除主机标记
					return sendStringToBytes(encoded,str,null);
				}else{
					str = str+"0";
				}
			}*/
			//终端采播
			if(type == CastWorkType.CLIENT) {
				str = str+"0";//第8位为0
			}
			//寻呼话筒
			if(type == CastWorkType.PAGING) {
				str = str+"0"+netHeartRecPort+""+targetPort;//第7位为1
			}else {
				str = str+netHeartRecPort+""+targetPort;//第7位为1
			}
			byte[] ips = IpUtils.textToNumericFormatV4(multiCastIp);
			if(StringUtils.isNotNull(ips)){
				byte[] bs = new byte[6];
				for(int i=0;i<ips.length;i++) {
					bs[i] = ips[i];
				}
				if(type == CastWorkType.FILE||type == CastWorkType.TIME) {
					bs[4] = ClientCommand.CMD_NONE.getCmd();
					bs[5] = (byte)vol;//音量
				}
				return sendStringToBytes(encoded,str,bs);
			}else{
				return null;
			}
		}else{//结束
			String str = "0";//第7位不为1
			return sendStringToBytes(encoded,str,null);
		}
		
	}
	
	/**
	 * 发送源终端广播命令
	 * @param isstart 是开始还是结束，true开始，false结束
	 * @param ipOrCmd 组播IP地址或者为终端采播的类型，isstart为true时必须
	 * @param targetPort 终端接收组播端口号，isstart为true时必须，必须为5位数
	 * @param vol 音量0-40
	 * @param types 命令类型以及对应指令
	 * @return 需要发送的ByteBuffer
	 */
	public static ByteBuffer sendMainTermCast(Boolean isstart,String ipOrCmd, int targetPort,CastWorkType type){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_TERCONTROL.getCmd());
		encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
		/*switch(type) {
			case CLIENT:
				encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
				break;
			case PAGING:
				encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
				break;
			default: 
				encoded.put(ClientCommand.CMD_TERMINAL.getCmd());
				break;
		}*/
		for(int i=2;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		if(isstart){//开始
			String str = "1";
			if(type == CastWorkType.CLIENT) {//终端采播
				str += ipOrCmd+targetPort;
				return sendStringToBytes(encoded,str,null);
			}
			//寻呼话筒
			if(type == CastWorkType.PAGING) {//寻呼话筒
				str += "0"+netHeartRecPort+""+targetPort;//第7位为1
			}else {
				str += netHeartRecPort+""+targetPort;//第7位为1
			}
			return sendStringToBytes(encoded,str,IpUtils.textToNumericFormatV4(ipOrCmd));
		}else{//结束
			String str = "0";//第7位不为1
			return sendStringToBytes(encoded,str,null);
		}
		
	}
	/**
	 * 网络调终端命令 --test 
	 * @param terminals 终端信息
	 * @return
	 */
/*	public static byte[] sendTerReset(Terminals terminals){
		ByteBuffer encoded = ByteBuffer.allocate(40);
		encoded.put(Convert.hexStringToBytes(CMDTYPE_TERCONTROL));
		encoded.put(Convert.toByte(IP_REQUEST));
		encoded.put((byte)26);
		for(int i=3;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		try {
			String[] ips = terminals.getTIP().split("\\.");
			for(String ip:ips) {//发送设置的IP
				encoded.put(Convert.toByte(ip));
			}
			String[] subNet = GlobalInfoController.NETMASK.split("\\.");//获取子网掩码
			for(String ip:subNet) {//发送设置的IP
				encoded.put(Convert.toByte(ip));
			}
			String[] gateway = GlobalInfoController.GATEWAY.split("\\.");//获取网关
			for(String ip:gateway) {//发送设置的IP
				encoded.put(Convert.toByte(ip));
			}
			String[] serverIP = GlobalInfoController.SERVERIP.split("\\.");//获取配置服务器IP
			for(String ip:serverIP) {//发送设置的IP
				encoded.put(Convert.toByte(ip));
			}
			String Tid = terminals.getTIDString();//获取终端更改后的IP地址
			for(char id:Tid.toCharArray()) {//发送设置的IP
				encoded.put((byte)id);
			}
			String definedPort = Tools.GetValueByKey(Const.CONFIG, "definedPort");//获取终端设置端口
			for(char id:definedPort.toCharArray()) {//发送设置的IP
				encoded.put((byte)id);
			}
			encoded.flip();
			if(encoded.remaining() != 30) {
				return null;
			}
			byte[] exec  = new byte[40];
			encoded.get(exec,0,30);
			int sum = 0;
			byte y = ClientCommand.CMD_NONE.getCmd();
			for(int i = 6;i <26;i++) {//计算校验和 分别是累加校验与异或校验
				sum += (0xff & exec[i]);
				y ^= exec[i];
			}
			byte[] checksum = {(byte)sum,y};
			System.arraycopy(checksum, 0, exec, 30, 2);
			return exec;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}*/
	/**
	 * 终端调音命令
	 * @param vol 音量0-40
	 * @param issave 是否保存音量
	 * @return
	 */
	public static ByteBuffer sendVolSet(int vol,Boolean issave){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_TERCONTROL.getCmd());
		encoded.put(ClientCommand.CMD_VOLSET.getCmd());
		for(int i=2;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		encoded.put((byte)vol);
		String str = "";
		if(issave){//保存音量
			str = "1";//第8位为1
		}else{//不保存
			str = "0";//第8位 为0
		}
		return sendStringToBytes(encoded,str,null);
		
	}
	/**
	 * 终端重启命令
	 * @return
	 */
	public static ByteBuffer sendTerReboot(){
		ByteBuffer encoded = ByteBuffer.allocate(NORMALSIZE);
		encoded.put(ClientCommand.CMDTYPE_TERCONTROL.getCmd());
		encoded.put(ClientCommand.REQUEST_RESTART.getCmd());
		for(int i=2;i<6;i++){
			encoded.put(ClientCommand.CMD_NONE.getCmd());
		}
		String str = "1";
		return sendStringToBytes(encoded,str,null);
		
	}
	/**
	 * 将要发送的音频数据包加上头标志.点播
	 * @param audiodata
	 * @return
	 */
	public static ByteBuffer sendAudioDataPackt(byte[] audiodata){
		ByteBuffer encoded = ByteBuffer.allocate(audiodata.length+16);
		encoded.put((byte)11);
		for(int i=1;i<7;i++){
			encoded.put((byte)0);
		}
		encoded.put(Convert.intToBytes(audiodata.length, 2));
		for(int i=9;i<16;i++){
			encoded.put((byte)0);
		}
		encoded.put(audiodata);
		return encoded;
	}
	/**
	 * 将content编码放入ByteBuffer中并转化成byte[]返回
	 * @param bb
	 * @param content 没有则设置为""
	 * @param lastbytes 在ByteBuffer放入content后在放入lastbytes，没有则设置为null
	 * @return byte[]
	 */
	private static ByteBuffer sendStringToBytes(ByteBuffer bb,String content,byte[] lastbytes){
		if(StringUtils.isNotEmpty(content)) {
			bb.put(content.getBytes(CharsetKit.CHARSET_GBK));
		}
		if(StringUtils.isNotNull(lastbytes)) {
			bb.put(lastbytes);
		}
		bb.flip();
		return bb;
	}
}
