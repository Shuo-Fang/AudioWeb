package com.audioweb.common.enums;

public enum ClientCommand {
	/**	空闲填充 */
	CMD_NONE((byte)0),
	/**	空闲校验填充 */
	CMD_HAVE((byte)1),
	/** 终端控制包 */
	CMDTYPE_TERCONTROL((byte)3),
	/** 终端心跳包 */
	CMD_NETHEART((byte)3),
	/**	终端登录 */
	CMD_LOGIN((byte)1),
	/**	回复线程端口 */
	IP_REQUEST((byte)4),
	/**	网络调音 */
	CMD_VOLSET((byte)5),
	/** 服务器响应包 */
	CMDTYPE_SERVERRETURN((byte)7),
	/** 停止点播 */
	CMD_STOPVOD((byte)7),
	/** 音频包默认长度 */
	CMD_NORMAL((byte)7),
	/** 终端采播(终端采播) */
	CMD_TERMINAL((byte)8),
	/**	终端回送的音频包指令 */
	CMD_PACKAGE((byte)10),
	/**	服务器发送的音频数据包 0x0b  单播、组播、广播 */
	CMDTYPE_AUDIODATA((byte)11),
	/** 点播音频列表 */
	CMD_VODFILELIST((byte)12),
	/** 点播开始命令 */
	CMD_VODFILECAST((byte)13),
	/** 点播暂停命令 */
	CMD_VODFILEPAUSE((byte)26),
	/** 文件组播命令 */
	CMD_FILECAST((byte)27),
	/** 定时组播命令 */
	CMD_TIMINGCAST((byte)40),
	/** 声卡采播命令(暂时弃用) */
	CMD_PIC_SEND((byte)41),
	/** 确认命令 */
	CMD_OK((byte)49),
	/** 终端重启 */
	REQUEST_RESTART((byte)89),
	/** 寻呼话筒点播列表 */
	CMD_CMICCAST((byte)90),
	/** 寻呼话筒开启回复 */
	CMD_CMICREPLY((byte)93),
	/** 寻呼话筒点播开始 */
	CMD_CMICENABLE((byte)95),
	/** 寻呼话筒进入下级列表 */
	CMD_CMICELIST((byte)96),
	/**	寻呼话筒对讲或传输填充命令 */
	CMD_PAD((byte)0xAA),
	/**	16位发包数据用作数据头 */
	CMD_HEADER_SIZE((byte)16),
	;
	private Byte cmd;
	private ClientCommand(Byte b) {
		cmd = b;
	}
	public Byte getCmd() {
		return cmd;
	}
	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}
	
	public static ClientCommand valueOf(Byte value) {    //    手写的从Byte到enum的转换函数
        switch (value) {
        case 0:
            return CMD_NONE;
        case 1:
        	return CMD_LOGIN;
        case 3:
        	return CMD_NETHEART;
        case 4:
        	return IP_REQUEST;
        case 5:
        	return CMD_VOLSET;
        case 7:
        	return CMD_STOPVOD;
        case 8:
        	return CMD_TERMINAL;
        case 10:
        	return CMD_PACKAGE;
        case 11:
        	return CMDTYPE_AUDIODATA;
        case 12:
        	return CMD_VODFILELIST;
        case 13:
        	return CMD_VODFILECAST;
        case 26:
        	return CMD_VODFILEPAUSE;
        case 27:
        	return CMD_FILECAST;
        case 40:
        	return CMD_TIMINGCAST;
        case 41:
        	return CMD_PIC_SEND;
        case 49:
        	return CMD_OK;
        case 89:
        	return REQUEST_RESTART;
        case 90:
        	return CMD_CMICCAST;
        case 93:
        	return CMD_CMICREPLY;
        case 95:
        	return CMD_CMICENABLE;
        case 96:
        	return CMD_CMICELIST;
        case (byte) 0xAA:
        	return CMD_PAD;
        default:
            return null;
        }
    }
}
