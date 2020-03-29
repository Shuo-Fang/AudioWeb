package com.audioweb.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;

/**
 * 歌单对象 work_song_list
 * 
 * @author shuofang
 * @date 2020-03-29
 */
public class SongList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 歌单ID号 */
    private Long listId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long listUserId;

    /** 歌单名称 */
    @Excel(name = "歌单名称")
    private String listName;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 音频信息json字符串 */
    @Excel(name = "音频信息json字符串")
    private String songData;

    public void setListId(Long listId) 
    {
        this.listId = listId;
    }

    public Long getListId() 
    {
        return listId;
    }
    public void setListUserId(Long listUserId) 
    {
        this.listUserId = listUserId;
    }

    public Long getListUserId() 
    {
        return listUserId;
    }
    public void setListName(String listName) 
    {
        this.listName = listName;
    }

    public String getListName() 
    {
        return listName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setSongData(String songData) 
    {
        this.songData = songData;
    }

    public String getSongData() 
    {
        return songData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("listId", getListId())
            .append("listUserId", getListUserId())
            .append("listName", getListName())
            .append("status", getStatus())
            .append("songData", getSongData())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
