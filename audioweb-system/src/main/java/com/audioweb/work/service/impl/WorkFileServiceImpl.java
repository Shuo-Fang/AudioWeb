package com.audioweb.work.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.audioweb.common.utils.CommonUtils;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.audio.Mp3Utils;
import com.audioweb.common.utils.bean.BeanUtils;
import com.audioweb.common.utils.file.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.mapper.WorkFileMapper;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.core.text.Convert;

/**
 * 音频任务中所有音频的存储序列信息Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-10
 */
@Service
public class WorkFileServiceImpl implements IWorkFileService 
{
	private final Logger log = LoggerFactory.getLogger(WorkFileServiceImpl.class);

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
        workFile.setCreateTime(DateUtils.getNowDate());
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

    /**
     * 批量修改音频任务中所有音频的存储序列信息
     * 
     * @param workFiles 音频任务中所有音频的存储序列信息
     * @return 结果
     */
	@Override
	public int updateWorkFileList(List<WorkFile> workFiles) 
	{
		return workFileMapper.updateWorkFileList(workFiles);
	}

    /**
     * 批量新增音频任务中所有音频的存储序列信息
     * 
     * @param workFiles 音频任务中所有音频的存储序列信息
     * @return 结果
     */
	@Override
	public int batchInsertWorkFiles(List<WorkFile> workFiles) 
	{
		return workFileMapper.batchInsertWorkFiles(workFiles);
	}

    /**
     * 扫描本地音频文件信息
     * @Title: initWorkFiles 
     * @Description: 扫描本地音频文件信息
     * @param paths 路径 void 返回类型 
     * @throws 抛出错误
     * @author ShuoFang 
     * @date 2020年3月11日 下午2:45:09
     */
	@Override
	public void initWorkFiles(List<String> paths) {
		Long time = System.currentTimeMillis();
		List<String> allFiles = new ArrayList<>();
		/**获取路径下全部文件地址*/
		if(StringUtils.isNotEmpty(paths)) {
			CommonUtils.removeDuplicate(paths);
			for(String path:paths) {
				allFiles.addAll(FileUtils.getCurFilesList(path));
			}
		}
		/**数据库中存储的文件信息*/
		List<WorkFile> workFiles = selectWorkFileList(new WorkFile());
		/**需要更新的文件信息*/
		List<WorkFile> upDataFiles = new ArrayList<WorkFile>();
		/**需要新增的文件信息*/
		List<WorkFile> addFiles = new ArrayList<WorkFile>();
		/**需要删除的文件信息*/
		String deleteIds = "";
		Map<String, WorkFile> fileMap = new HashMap<String, WorkFile>();
		for(WorkFile file:workFiles) {
			fileMap.put(file.getFileId(), file);
		}
		/**遍历读取音频文件信息并进行对比*/
		for(String filePath:allFiles) {
			if(Mp3Utils.isMp3(filePath)) {
				String fileId=Mp3Utils.getFileId(filePath);
				WorkFile workFile = fileMap.get(fileId);
				if(workFile == null) {
					try {
						addFiles.add(BeanUtils.mapToBean(Mp3Utils.getMusicInfo(filePath), WorkFile.class));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(!workFile.getDelFlag().equals(WorkConstants.AUDIOFILENORMAL)){
					workFile.setDelFlag(WorkConstants.AUDIOFILENORMAL);
					workFile.setUpdateTime(new Date(time));
					upDataFiles.add(workFile);
				}
				/**对比后删除map中对应的文件信息*/
				fileMap.remove(fileId);
			}
		}
		/**遍历完后剩余数据的更新处理*/
		for(Map.Entry<String, WorkFile> entry:fileMap.entrySet()) {
			WorkFile file = entry.getValue();
			if(file.getDelFlag().equals(WorkConstants.AUDIOFILENORMAL)) {
				file.setDelFlag(WorkConstants.AUDIOFILENOTFOND);
				file.setUpdateTime(new Date(time));
				upDataFiles.add(file);
			}else if(file.getUpdateTime().getTime() < (time - WorkConstants.AUDIOFILENOTFONDDATE)){
				/** 删除或丢失文件存储时间超过了7天*/
				deleteIds += file.getFileId()+",";
			}
		}
		/**执行数据库新增更新删除操作*/
		if(StringUtils.isNotEmpty(deleteIds)) {
			try {
				int num = deleteWorkFileByIds(deleteIds);
				log.info("{}个删除或丢失的音频文件已被删除",num);
			} catch (Exception e) {
				log.error("音频文件扫描删除丢失废弃文件失败：",e);
			}
		}
		if(StringUtils.isNotEmpty(addFiles)) {
			try {
				int num = batchInsertWorkFiles(addFiles);
				log.info("{}个本地新增的音频文件已添加",num);
			} catch (Exception e) {
				log.error("本地新增的音频文件扫描写入数据库失败：",e);
			}
		}
		if(StringUtils.isNotEmpty(upDataFiles)) {
			try {
				int num = updateWorkFileList(upDataFiles);
				log.info("{}个本地状态更新的音频文件已更新",num);
			} catch (Exception e) {
				log.error("本地更新的音频文件扫描写入数据库失败：",e);
			}
		}
	}
}
