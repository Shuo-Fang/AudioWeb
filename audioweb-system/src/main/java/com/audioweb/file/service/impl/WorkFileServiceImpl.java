package com.audioweb.file.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.file.mapper.WorkFileMapper;
import com.audioweb.file.domain.WorkFile;
import com.audioweb.file.service.IWorkFileService;
import com.audioweb.common.core.text.Convert;

/**
 * 音频任务中所有音频的存储序列信息Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-02
 */
@Service
public class WorkFileServiceImpl implements IWorkFileService 
{
    @Autowired
    private WorkFileMapper workFileMapper;

    /**
     * 查询音频任务中所有音频的存储序列信息
     * 
     * @param fileId 音频任务中所有音频的存储序列信息ID
     * @return 音频任务中所有音频的存储序列信息
     */
    @Override
    public WorkFile selectWorkFileById(String fileId)
    {
        return workFileMapper.selectWorkFileById(fileId);
    }

    /**
     * 查询音频任务中所有音频的存储序列信息列表
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 音频任务中所有音频的存储序列信息
     */
    @Override
    public List<WorkFile> selectWorkFileList(WorkFile workFile)
    {
        return workFileMapper.selectWorkFileList(workFile);
    }

    /**
     * 新增音频任务中所有音频的存储序列信息
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 结果
     */
    @Override
    public int insertWorkFile(WorkFile workFile)
    {
        return workFileMapper.insertWorkFile(workFile);
    }

    /**
     * 修改音频任务中所有音频的存储序列信息
     * 
     * @param workFile 音频任务中所有音频的存储序列信息
     * @return 结果
     */
    @Override
    public int updateWorkFile(WorkFile workFile)
    {
        return workFileMapper.updateWorkFile(workFile);
    }

    /**
     * 删除音频任务中所有音频的存储序列信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkFileByIds(String ids)
    {
        return workFileMapper.deleteWorkFileByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除音频任务中所有音频的存储序列信息信息
     * 
     * @param fileId 音频任务中所有音频的存储序列信息ID
     * @return 结果
     */
    @Override
    public int deleteWorkFileById(String fileId)
    {
        return workFileMapper.deleteWorkFileById(fileId);
    }
}
