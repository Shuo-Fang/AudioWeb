package com.audioweb.work.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;
import com.audioweb.common.utils.StringUtils;

/**
 * 音频任务中所有音频的存储序列信息对象 work_file
 * 
 * @author shuofang
 * @date 2020-03-10
 */
public class WorkFile extends BaseEntity implements BaseRunning
{
    private static final long serialVersionUID = 1L;
    /**默认最大音频加载值为1000 -> 1000*0.75+1 = 751*/
	private static Map<String, WorkFile> fileMap = new ConcurrentHashMap<String, WorkFile>(512);

    /** 音频的路径md5值 */
    private String fileId;

    /** 音频名称 */
    @Excel(name = "音频名称")
    private String fileName;
    /** 音频所属类型  */
    private String fileType;

    /** 音频根路径 */
    private String filePath;
    
    /** 音频虚拟路径 */
    private String virPath;

    /** 歌曲名称(歌源) */
    @Excel(name = "歌曲名称(歌源)")
    private String songName;

    /** 歌手名称(歌源) */
    @Excel(name = "歌手名称(歌源)")
    private String artist;

    /** 歌曲专辑(歌源) */
    @Excel(name = "歌曲专辑(歌源)")
    private String album;

    /** 音轨时长长度(ms) */
    @Excel(name = "音轨时长长度(ms)")
    private Long duration;

    /** 音频格式 */
    private String format;

    /** 音频比特率 */
    private Integer bitRate;

    /** 音频采样率 */
    private Integer sampleRate;

    /** 音频开始字节(byte) */
    private Long startByte;

    /** 音频总长度(byte,包括开始前) */
    private Long musicLength;

    /** 删除标志（0代表存在 1代表搜索不到 2代表删除） */
    private String delFlag;
    
    /** 备注(或文字转语音中的文字信息) */
    private String remark;
    
    public WorkFile() {
    	
    }
    
    public WorkFile(String id) {
		fileId = id;
	}
    
    /** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @author ShuoFang
	 * @date 2020年3月11日 下午1:27:29 
	 */
    public void setFileId(String fileId) 
    {
        this.fileId = fileId;
    }

    public String getFileId() 
    {
        return fileId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setSongName(String songName) 
    {
        this.songName = songName;
    }

    public String getSongName() 
    {
        return songName;
    }
    public void setArtist(String artist) 
    {
        this.artist = artist;
    }

    public String getArtist() 
    {
        return artist;
    }
    public void setAlbum(String album) 
    {
        this.album = album;
    }

    public String getAlbum() 
    {
        return album;
    }
    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }
    public void setFormat(String format) 
    {
        this.format = format;
    }

    public String getFormat() 
    {
        return format;
    }
    public void setBitRate(Integer bitRate) 
    {
        this.bitRate = bitRate;
    }

    public Integer getBitRate() 
    {
        return bitRate;
    }
    public void setSampleRate(Integer sampleRate) 
    {
        this.sampleRate = sampleRate;
    }

    public Integer getSampleRate() 
    {
        return sampleRate;
    }
    public void setStartByte(Long startByte) 
    {
        this.startByte = startByte;
    }

    public Long getStartByte() 
    {
        return startByte;
    }
    public void setMusicLength(Long musicLength) 
    {
        this.musicLength = musicLength;
    }

    public Long getMusicLength() 
    {
        return musicLength;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getVirPath() {
		return virPath;
	}

	public void setVirPath(String virPath) {
		this.virPath = virPath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkFile other = (WorkFile) obj;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		return true;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("fileName", getFileName())
            .append("fileType", getFileName())
            .append("filePath", getFilePath())
            .append("virPath", getFileName())
            .append("songName", getSongName())
            .append("artist", getArtist())
            .append("album", getAlbum())
            .append("duration", getDuration())
            .append("format", getFormat())
            .append("bitRate", getBitRate())
            .append("sampleRate", getSampleRate())
            .append("startByte", getStartByte())
            .append("musicLength", getMusicLength())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getUpdateTime())
            .toString();
    }

	@Override
	public boolean put() {
		if(StringUtils.isNotEmpty(fileId)) {
			fileMap.put(fileId, this);
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean exist() {
		return fileMap.containsKey(fileId);
	}
	@Override
	public WorkFile get() {
		if(StringUtils.isNotEmpty(fileId)) {
			return fileMap.get(fileId);
		}else {
			return null;
		}
	}

	@Override
	public void clear() {
		fileMap.clear();
	}
	
	@Override
	public List<WorkFile> getList() {
		return new ArrayList<WorkFile>(fileMap.values());
	}
}
