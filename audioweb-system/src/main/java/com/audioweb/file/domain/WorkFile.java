package com.audioweb.file.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;

/**
 * 音频任务中所有音频的存储序列信息对象 work_file
 * 
 * @author shuofang
 * @date 2020-03-02
 */
public class WorkFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 音频名称 */
    @Excel(name = "音频名称")
    private String fileName;

    /** 音频根路径 */
    private String filePath;

    /** 音轨时长长度(ms) */
    @Excel(name = "音轨时长长度(ms)")
    private Long trackLength;

    /** 音频比特率 */
    @Excel(name = "音频比特率")
    private Integer bitRate;

    /** 音频开始字节 */
    private Long startByte;

    /** 音频声道数 */
    @Excel(name = "音频声道数")
    private String channels;

    /** 音频格式 */
    @Excel(name = "音频格式")
    private String format;

    /** 删除标志（0代表存在 1代表搜索不到 2代表删除） */
    private String delFlag;

    /** 音频所属地址类型,多个以 ','分开*/
    @Excel(name = "音频所属地址类型,多个以','分开")
    private String belongs;

    /** 音频的父路径和音频合计的md5值 */
    private String fileId;

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
    public void setTrackLength(Long trackLength) 
    {
        this.trackLength = trackLength;
    }

    public Long getTrackLength() 
    {
        return trackLength;
    }
    public void setBitRate(Integer bitRate) 
    {
        this.bitRate = bitRate;
    }

    public Integer getBitRate() 
    {
        return bitRate;
    }
    public void setStartByte(Long startByte) 
    {
        this.startByte = startByte;
    }

    public Long getStartByte() 
    {
        return startByte;
    }
    public void setChannels(String channels) 
    {
        this.channels = channels;
    }

    public String getChannels() 
    {
        return channels;
    }
    public void setFormat(String format) 
    {
        this.format = format;
    }

    public String getFormat() 
    {
        return format;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setBelongs(String belongs) 
    {
        this.belongs = belongs;
    }

    public String getBelongs() 
    {
        return belongs;
    }
    public void setFileId(String fileId) 
    {
        this.fileId = fileId;
    }

    public String getFileId() 
    {
        return fileId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("trackLength", getTrackLength())
            .append("bitRate", getBitRate())
            .append("startByte", getStartByte())
            .append("channels", getChannels())
            .append("format", getFormat())
            .append("delFlag", getDelFlag())
            .append("belongs", getBelongs())
            .append("fileId", getFileId())
            .toString();
    }
}
