package com.audioweb.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;

/**
 * 音频任务中所有音频的存储序列信息对象 work_file
 * 
 * @author shuofang
 * @date 2020-03-10
 */
public class WorkFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 音频的路径md5值 */
    private String fileId;

    /** 音频名称 */
    @Excel(name = "音频名称")
    private String fileName;

    /** 音频根路径 */
    private String filePath;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
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
            .append("createTime", getCreateTime())
            .toString();
    }
}
