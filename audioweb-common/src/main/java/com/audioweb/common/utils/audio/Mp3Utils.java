/**   
 * @Title: Mp3Utils.java 
 * @Package com.audioweb.common.utils.audio 
 * @Description: MP3音频信息处理获取类
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月10日 下午1:58:10 
 * @version V1.0   
 */ 
package com.audioweb.common.utils.audio;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.config.Global;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.utils.ExceptionUtil;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.file.FileUploadUtils;
import com.audioweb.common.utils.file.FileUtils;
import com.audioweb.common.utils.security.Md5Utils;

/** 
 * @ClassName: Mp3Utils 
 * @Description: MP3音频信息处理获取类
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月10日 下午1:58:10  
 */
public class Mp3Utils {
    private static final Logger log = LoggerFactory.getLogger(Mp3Utils.class);
	/** 歌曲名称 */
    private static final String SONG_NAME_KEY = "TIT2";
    /** 歌手名称 */
    private static final String ARTIST_KEY = "TPE1";
    /** 歌曲专辑 */
    private static final String ALBUM_KEY = "TALB";
    /** 歌曲图片 */
    private static final String PICTURE_KEY = "APIC";
    
    /**
     * 通过歌曲文件地址, 获取歌曲信息
     *
     * @param path 歌曲文件地址
     * @return 歌曲信息
     * @throws Exception 可能抛出空指针异常
     */
    public static Map<String, Object> getMusicInfo(String path){
    	Map<String, Object> music = new HashMap<String, Object>();
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(path));
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            // 歌曲名称
            String songName = getInfoFromFrameMap(mp3File, SONG_NAME_KEY);
            music.put("songName", StringUtils.isNotNull(songName)?songName:"");
            // 歌手名称
            String artist = getInfoFromFrameMap(mp3File, ARTIST_KEY);
            music.put("artist", StringUtils.isNotNull(artist)?artist:"");
            // 歌曲专辑
            String album = getInfoFromFrameMap(mp3File, ALBUM_KEY);
            music.put("album", StringUtils.isNotNull(album)?album:"");
            // 播放时长
            Long duration =  Math.round(audioHeader.getPreciseTrackLength()*1000);
            music.put("duration", StringUtils.isNotNull(duration)?duration:null);
            // 采样率
            Integer sampleRate = StringUtils.isNotNull(audioHeader.getSampleRate())?Integer.parseInt(audioHeader.getSampleRate()):null;
            music.put("sampleRate", StringUtils.isNotNull(sampleRate)?sampleRate:null);
            // 起始字节
            Long startByte = audioHeader.getMp3StartByte();
            music.put("startByte", StringUtils.isNotNull(startByte)?startByte:null);
            // 总长度
            Long musicLength = mp3File.getFile().length();
            music.put("musicLength", StringUtils.isNotNull(musicLength)?musicLength:null);
            // 音频比特率
            Integer bitRate = (int) audioHeader.getBitRateAsNumber();
            music.put("bitRate", StringUtils.isNotNull(bitRate)?bitRate:null);
            // 音频格式
            String format = audioHeader.getFormat()+" "+audioHeader.getChannels();
            music.put("format", StringUtils.isNotEmpty(format)?format:"");
            // 音频路径
            String filePath = FileUtils.formatToLin(mp3File.getFile().getPath());
            music.put("filePath", StringUtils.isNotEmpty(filePath)?filePath:"");
            // 音频名称
            String fileName = mp3File.getFile().getName();
            music.put("fileName", StringUtils.isNotEmpty(fileName)?fileName:"");
            //音频ID
            String fileId = Md5Utils.hash(mp3File.getFile().getPath());
            music.put("fileId", StringUtils.isNotEmpty(fileId)?fileId:"");
            //音频图片路径
            byte[] imageData = getImageFromFrameMap(mp3File);
            if(StringUtils.isNotNull(imageData)) {
            	String imagePath = FileUtils.formatToLin(FileUploadUtils.saveImage(imageData, fileName));
            	music.put("imagePath", StringUtils.isNotEmpty(imagePath)?imagePath:"");
            	music.put("imageVirPath", StringUtils.isNotEmpty(imagePath)?imagePath.replace(FileUtils.formatPath(FileUtils.formatToLin(Global.getImagePath())), Constants.RESOURCE_PREFIX+"/image"):"");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件读取失败: {}", path);
            log.error(ExceptionUtil.getExceptionMessage(e.getCause()));
            return null;
        }
        return music;
    }

    /**
     * 通过键值,获取歌曲中对应的字段信息
     *
     * @param mp3File mp3音乐文件
     * @param key     键值
     * @return 歌曲信息
     * @throws Exception 可能抛出空指针异常
     */
    private static String getInfoFromFrameMap(MP3File mp3File, String key){
    	try {
    		ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().frameMap.get(key);
    		return frame.getContent();
		} catch (Exception e) {
			return null;
		}
    }
    /**
     * 通过键值,获取歌曲中对应的字段信息
     *
     * @param mp3File mp3音乐文件
     * @return 歌曲信息
     * @throws Exception 可能抛出空指针异常
     */
    private static byte[] getImageFromFrameMap(MP3File mp3File){
    	try {
    		ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().frameMap.get(PICTURE_KEY);
    		FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
    		return body.getImageData();
    	} catch (Exception e) {
    		return null;
    	}
    }
    /**
     * 判断是否为MP3文件
     * @Title: isMp3 
     * @Description:判断是否为MP3文件
     * @param path
     * @return boolean 返回类型 
     * @throws 抛出错误
     * @author 10155 
     * @date 2020年3月10日 下午10:00:46
     */
    public static boolean isMp3(File file) {
    	try {
    		MP3File mp3File = (MP3File) AudioFileIO.read(file);
    		mp3File.getMP3AudioHeader();
		} catch (Exception e) {
			return false;
		}
    	return true;
	}
    /**
     * 判断是否为MP3文件
     * @Title: isMp3 
     * @Description: 判断是否为MP3文件
     * @param path
     * @return boolean 返回类型 
     * @throws 抛出错误
     * @author 10155 
     * @date 2020年3月10日 下午10:06:11
     */
    public static boolean isMp3(String path) {
    	try {
    		return isMp3(new File(path));
		} catch (Exception e) {
			return false;
		}
	}
    /**
     * 获取file的ID
     * @Title: getFileId 
     * @Description: 获取file的ID
     * @param path
     * @return String 返回类型 
     * @throws 抛出错误
     * @author ShuoFang 
     * @date 2020年3月11日 上午10:34:44
     */
    public static String getFileId(String path) {
		return Md5Utils.hash(path);
	}
    /*public static void main(String[] args) {
		System.out.println(isMp3("E:\\music\\CloudMusic\\梁邦彦 - Forgotten Sorrow.mp3"));
	}*/
}
