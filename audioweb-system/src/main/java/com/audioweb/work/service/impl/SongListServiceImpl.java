package com.audioweb.work.service.impl;

import java.util.List;
import com.audioweb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.mapper.SongListMapper;
import com.audioweb.work.domain.SongList;
import com.audioweb.work.service.ISongListService;
import com.audioweb.common.core.text.Convert;

/**
 * 歌单Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-29
 */
@Service
public class SongListServiceImpl implements ISongListService 
{
    @Autowired
    private SongListMapper songListMapper;

    /**
     * 查询歌单
     * 
     * @param listId 歌单ID
     * @return 歌单
     */
    @Override
    public SongList selectSongListById(Long listId)
    {
        return songListMapper.selectSongListById(listId);
    }

    /**
     * 查询歌单列表
     * 
     * @param songList 歌单
     * @return 歌单
     */
    @Override
    public List<SongList> selectSongListList(SongList songList)
    {
        return songListMapper.selectSongListList(songList);
    }

    /**
     * 新增歌单
     * 
     * @param songList 歌单
     * @return 结果
     */
    @Override
    public int insertSongList(SongList songList)
    {
        songList.setCreateTime(DateUtils.getNowDate());
        return songListMapper.insertSongList(songList);
    }

    /**
     * 修改歌单
     * 
     * @param songList 歌单
     * @return 结果
     */
    @Override
    public int updateSongList(SongList songList)
    {
        songList.setUpdateTime(DateUtils.getNowDate());
        return songListMapper.updateSongList(songList);
    }
    /**
     * 修改歌单是否启用
     * 
     * @param songList 歌单
     * @return 结果
     */
    @Override
    public int changeStatus(SongList songList)
    {
    	songList.setUpdateTime(DateUtils.getNowDate());
    	return songListMapper.updateSongList(songList);
    }

    /**
     * 删除歌单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSongListByIds(String ids)
    {
        return songListMapper.deleteSongListByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除歌单信息
     * 
     * @param listId 歌单ID
     * @return 结果
     */
    @Override
    public int deleteSongListById(Long listId)
    {
        return songListMapper.deleteSongListById(listId);
    }
}
