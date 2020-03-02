package com.audioweb.file.mapper;

import com.audioweb.file.domain.WorkFile;
import java.util.List;

/**
 * 音频任务中所有音频的存储序列信息Mapper接口
 * 
 * @author shuofang
 * @date 2020-03-02
 */
public interface WorkFileMapper 
{
    /**
     * 查询音频任务中所有音频的存储序列信息
     * 
     * @param fileId 音频任务中所有音频的存储序列信息ID
     * @return 音频任务中所有音频的存储序列信息
     */
    public WorkFile selectWorkFileById(String fileId);

    /**
     * 查询音频任务中所有音频的存储序列信息列表
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 音频任务中所有音频的存储序列信息集合
     */
    public List<WorkFile> selectWorkFileList(WorkFile workFile);

    /**
     * 新增音频任务中所有音频的存储序列信息
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 结果
     */
    public int insertWorkFile(WorkFile workFile);

    /**
     * 修改音频任务中所有音频的存储序列信息
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 结果
     */
    public int updateWorkFile(WorkFile workFile);

    /**
     * 删除音频任务中所有音频的存储序列信息
     * 
     * @param fileId 音频任务中所有音频的存储序列信息ID
     * @return 结果
     */
    public int deleteWorkFileById(String fileId);

    /**
     * 批量删除音频任务中所有音频的存储序列信息
     * 
     * @param fileIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkFileByIds(String[] fileIds);
}
