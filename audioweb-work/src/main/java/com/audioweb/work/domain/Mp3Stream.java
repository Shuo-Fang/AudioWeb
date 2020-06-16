package com.audioweb.work.domain;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mp3Stream {

	/**
	 * 横坐标为MPEG(V),纵坐标为Layer(L),sample[0][2]为MPEG-1,Layer-3的每帧采样数
	 */

	private final static int[][] Mp3_Sample = { { 384, 384, 384 },
			{ 1152, 1152, 1152 }, { 1152, 576, 576 } };

	/**
	 * 二维数组长度为14,横坐标范围:1~14.MPEG1中,纵坐标分别对应Layer-1,Layer-2,Layer-3.MPEG2中,纵坐标分别对应Layer-1,Layer-2或Layer-3.
	 */
	private final static int[][] MPeg1ByteRate = { { 32, 32, 32 },
			{ 64, 48, 40 }, { 96, 56, 48 }, { 128, 64, 56 }, { 160, 80, 64 },
			{ 192, 96, 80 }, { 224, 112, 96 }, { 256, 128, 112 },
			{ 288, 160, 128 }, { 320, 192, 160 }, { 352, 224, 192 },
			{ 384, 256, 224 }, { 416, 320, 256 }, { 448, 384, 320 } };

	private final static int[][] MPeg2ByteRate = { { 32, 8 }, { 48, 16 },
			{ 56, 24 }, { 64, 32 }, { 80, 40 }, { 96, 48 }, { 112, 56 },
			{ 128, 64 }, { 144, 80 }, { 160, 96 }, { 176, 112 }, { 192, 128 },
			{ 224, 144 }, { 256, 160 } };

	/**
	 * 采样频率.横坐标为变量,纵坐标为MPEG版本.
	 */
	private final static int[][] SampleFrequency = { { 44100, 48000, 32000 },
			{ 22050, 24000, 16000 }, { 11025, 12000, 8000 } };

	// ==================================================================================
	private BufferedInputStream stream;
	/**
	 * 读取执行锁
	 */
	private Lock lock = new ReentrantLock();

	public Mp3Stream(BufferedInputStream stream,long musicLength) throws IOException {
		this.stream = stream;
		this.stream.mark((int) (musicLength));
		parserMp3Header();
	}

	/**
	 * MpeG版本
	 */
	private int Mpeg_Version;

	/**
	 * Layer版本
	 */
	private int Layer_Version;

	/**
	 * 采样速率.(kbps)
	 */
	private int ByteRate;

	/**
	 * 采样频率.khz
	 */
	private int Frequency;

	/**
	 * 帧长度调整值.0或1
	 */
	private int Padding;

	/**
	 * 采样位数.
	 */
	private int Sample;

	public int parserMp3Header() {
		FrameHeader = new int[3];
		byte[] tempBytes = readFull(3);
		if (tempBytes == null) {
			return -1;
		}
		for (int i = 0; i < 3; i++) {
			FrameHeader[i] = tempBytes[i] & 0xFF;
		}
		int TagHeaderSize = 0;
		if (FrameHeader[0] == 'I' && FrameHeader[1] == 'D'
				&& FrameHeader[2] == '3') {
			byte[] tagHeader = readFull(7);
			if (tagHeader == null) {
				return -1;
			}
			int tagHeaderSize = ((tagHeader[3] & 0x7F) << 21)
					+ ((tagHeader[4] & 0x7F) << 14)
					+ ((tagHeader[5] & 0x7F) << 7) + (tagHeader[6] & 0x7F);
			// tagHeaderSize不包括前面10个字节.加上10以后,是整个标签头的大小.
			if (!skipBytes(tagHeaderSize)) {
				return -1;
			}
			// IDV3标签头的长度.
			TagHeaderSize = tagHeaderSize + 10;
			tempBytes = readFull(3);
			if (tempBytes == null) {
				return -1;
			}
			for (int i = 0; i < 3; i++) {
				FrameHeader[i] = tempBytes[i] & 0xFF;
			}
		}
		if (FrameHeader[0] != 0xFF || (FrameHeader[1] >> 5) != 7) {
			return -1;
		}
		switch ((FrameHeader[1] & 24) >> 3) {
		case 0:
			Mpeg_Version = 3;// MPEG-2.5
			break;
		case 2:
			Mpeg_Version = 2;
			break;
		case 3:
			Mpeg_Version = 1;
			break;
		default:
			return -1;
		}
		switch ((FrameHeader[1] & 6) >> 1) {
		case 1:
			Layer_Version = 3;
			break;
		case 2:
			Layer_Version = 2;
			break;
		case 3:
			Layer_Version = 1;
			break;
		default:
			return -1;
		}
		int index = FrameHeader[2] >> 4;
		if (index < 1 || index > 14) {
			return -1;
		}
		--index;
		if (Mpeg_Version == 1) {
			switch (Layer_Version) {
			case 1:
				ByteRate = MPeg1ByteRate[index][0];
				break;
			case 2:
				ByteRate = MPeg1ByteRate[index][1];
				break;
			case 3:
				ByteRate = MPeg1ByteRate[index][2];
				break;
			}
		} else {
			switch (Layer_Version) {
			case 1:
				ByteRate = MPeg2ByteRate[index][0];
				break;
			case 2:
			case 3:
				ByteRate = MPeg2ByteRate[index][1];
				break;
			}
		}
		Frequency = SampleFrequency[Mpeg_Version - 1][(FrameHeader[2] & 12) >> 2];
		Padding = (FrameHeader[2] & 2) >> 1;
		Sample = Mp3_Sample[Layer_Version - 1][Mpeg_Version - 1];
		return TagHeaderSize;
	}
	/***
	 * 跳过指定数量的音频帧
	 * @Title: SkipFrame 
	 * @Description: 跳过指定数量的音频帧
	 * @param frameSize
	 * @return long 返回类型 跳过的字节数
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年6月17日 上午12:19:53
	 */
	public long SkipFrame(long frameSize) {
		long skipped = 0;
		lock.lock();
		try {
			for (int i = 0; i < frameSize; i++) {
				int size = ParserFrame();
				if (size == -1) {
					return skipped;
				}
				skipped += size;
			}
		} finally {
			lock.unlock();
		}
		return skipped;
	}

	/**
	 * 第一帧的前三个字节.用来提取除采样速率以外的其它定值. 在前三个字节中,第一二两个字节是不变的.只有第三个字节会左右帧长度的变化.
	 */
	private int[] FrameHeader;

	/***
	 * 读取一次发包数据
	 * @Title: ParserFrame 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param buffer 缓存buffer
	 * @param frameCount 数据帧数
	 * @return int 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年6月17日 上午12:21:18
	 */
	public int ParserFrame(ByteBuffer buffer,int frameCount) {
		int frameSize = 0;
		lock.lock();
		try {
			while(frameCount > 0) {
				byte[] byteFrameHeader;
				int index = 0;
				if (FrameHeader == null) {
					byteFrameHeader = readFull(3);
					if (byteFrameHeader == null || byteFrameHeader.length < 3
							|| (byteFrameHeader[0] & 0xFF) == 'T'
							&& (byteFrameHeader[1] & 0xFF) == 'A'
							&& (byteFrameHeader[2] & 0xFF) == 'G') {
						return -1;
					}
					if ((byteFrameHeader[0] & 0xFF) != 0xFF
							|| ((byteFrameHeader[1] & 0xFF) >> 5) != 7) {
						return -1;// 帧头的第一个字节和第二个字节的前三位不全部为1,该MP3文件非法.
					}
					index = (byteFrameHeader[2] & 0xFF) >> 4;
					Padding = (byteFrameHeader[2] & 2) >> 1;
				} else {
					if (FrameHeader == null || FrameHeader.length < 3
							|| FrameHeader[0] == 'T' && FrameHeader[1] == 'A'
							&& FrameHeader[2] == 'G') {
						return -1;
					}
					index = FrameHeader[2] >> 4;
					byteFrameHeader = new byte[3];
					byteFrameHeader[0] = (byte) FrameHeader[0];
					byteFrameHeader[0] = (byte) FrameHeader[1];
					byteFrameHeader[0] = (byte) FrameHeader[2];
					FrameHeader = null;
				}
				if (index < 1 || index > 14) {
					return -1;// 获取位速的查找索引非法.
				}
				--index;
				if (Mpeg_Version == 1) {
					switch (Layer_Version) {
					case 1:
						ByteRate = MPeg1ByteRate[index][0];
						break;
					case 2:
						ByteRate = MPeg1ByteRate[index][1];
						break;
					case 3:
						ByteRate = MPeg1ByteRate[index][2];
						break;
					}
				} else {
					switch (Layer_Version) {
					case 1:
						ByteRate = MPeg2ByteRate[index][0];
						break;
					case 2:
					case 3:
						ByteRate = MPeg2ByteRate[index][1];
						break;
					}
				}
				// 计算帧长.(ByteRate,Frequency,Padding是可变的)
				int size = Sample * (ByteRate >> 3) * 1000 / Frequency + Padding;
				byte[] temp = readFull(size - 3);
				if (temp == null) {
					return -1;
				}
				buffer.put(byteFrameHeader);
				buffer.put(temp);
				frameSize += size;
				//System.arraycopy(byteFrameHeader, 0, data, offset, 3);
				//System.arraycopy(temp, 0, data, offset+3, temp.length);
				frameCount -- ;
			}
		} finally {
			lock.unlock();
		}
		//buffer.flip();
		return frameSize;
	}
	
	/**
	 * 跳过当前帧
	 * 
	 * @return byte[] 读到的一帧完整的可以播放的数据.遇到IDV1时,返回NULL
	 * @throws Exception
	 */
	public int ParserFrame() {
		byte[] byteFrameHeader;
		int index = 0;
		if (FrameHeader == null) {
			byteFrameHeader = readFull(3);
			if (byteFrameHeader == null || byteFrameHeader.length < 3
					|| (byteFrameHeader[0] & 0xFF) == 'T'
					&& (byteFrameHeader[1] & 0xFF) == 'A'
					&& (byteFrameHeader[2] & 0xFF) == 'G') {
				return -1;
			}
			if ((byteFrameHeader[0] & 0xFF) != 0xFF
					|| ((byteFrameHeader[1] & 0xFF) >> 5) != 7) {
				return -1;// 帧头的第一个字节和第二个字节的前三位不全部为1,该MP3文件非法.
			}
			index = (byteFrameHeader[2] & 0xFF) >> 4;
			Padding = (byteFrameHeader[2] & 2) >> 1;
		} else {
			if (FrameHeader == null || FrameHeader.length < 3
					|| FrameHeader[0] == 'T' && FrameHeader[1] == 'A'
					&& FrameHeader[2] == 'G') {
				return -1;
			}
			index = FrameHeader[2] >> 4;
			byteFrameHeader = new byte[3];
			byteFrameHeader[0] = (byte) FrameHeader[0];
			byteFrameHeader[0] = (byte) FrameHeader[1];
			byteFrameHeader[0] = (byte) FrameHeader[2];
			FrameHeader = null;
		}
		if (index < 1 || index > 14) {
			return -1;// 获取位速的查找索引非法.
		}
		--index;
		if (Mpeg_Version == 1) {
			switch (Layer_Version) {
			case 1:
				ByteRate = MPeg1ByteRate[index][0];
				break;
			case 2:
				ByteRate = MPeg1ByteRate[index][1];
				break;
			case 3:
				ByteRate = MPeg1ByteRate[index][2];
				break;
			}
		} else {
			switch (Layer_Version) {
			case 1:
				ByteRate = MPeg2ByteRate[index][0];
				break;
			case 2:
			case 3:
				ByteRate = MPeg2ByteRate[index][1];
				break;
			}
		}
		// 计算帧长.(ByteRate,Frequency,Padding是可变的)
		int frameSize = Sample * (ByteRate >> 3) * 1000 / Frequency + Padding;
		if (!skipBytes(frameSize - 3)) {
			return -1;
		}
		return frameSize;
	}
	
	public byte[] readFull(int size) {
		byte[] data = new byte[size];
		int n = 0;
		while (n < size) {
			int k = 0;
			try {
				k = stream.read(data, n, size - n);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (k <= 0) {
				break;
			}
			n += k;
		}
		if (n <= 0) {
			return null;
		}
		if (n < size) {
			byte[] temp = new byte[n];
			System.arraycopy(data, 0, temp, 0, n);
			data = null;
			return temp;
		}
		return data;
	}

	public boolean skipBytes(long skipLength) {
		long k = 0;
		while (k < skipLength) {
			long n = 0;
			try {
				n = stream.skip(skipLength - k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (n <= 0) {
				return false;
			}
			k += n;
		}
		return true;
	}

}
