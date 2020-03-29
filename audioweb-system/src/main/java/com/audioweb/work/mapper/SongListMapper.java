package com.audioweb.work.mapper;

import java.util.List;
import com.audioweb.work.domain.SongList;

/**
 * 歌单Mapper接口
 * 
 * @author shuofang
 * @date 2020-03-29
 */
public interface SongListMapper 
{
    /**
     * 查询歌单
     * 
     * @param listId 歌单ID
     * @return 歌单
     */
    public SongList selectSongListById(Long listId);

    /**
     * 查询歌单列表
     * 
     * @param songList 歌单
     * @return 歌单集合
     */
    public List<SongList> selectSongListList(SongList songList);

    /**
     * 新增歌单
     * 
     * @param songList 歌单
     * @return 结果
     */
    public int insertSongList(SongList songList);

    /**
     * 修改歌单
     * 
     * @param songList 歌单
     * @return 结果
     */
    public int updateSongList(SongList songList);

    /**
     * 删除歌单
     * 
     * @param listId 歌单ID
     * @return 结果
     */
    public int deleteSongListById(Long listId);

    /**
     * 批量删除歌单
     * 
     * @param listIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSongListByIds(String[] listIds);
}
