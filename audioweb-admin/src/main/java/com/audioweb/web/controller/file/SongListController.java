package com.audioweb.web.controller.file;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.audioweb.common.annotation.Log;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.enums.OperatorType;
import com.audioweb.common.json.JSON;
import com.audioweb.work.domain.SongList;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.ISongListService;
import com.audioweb.work.service.IWorkFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.system.domain.SysDictData;
import com.audioweb.system.service.ISysDictTypeService;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 歌单Controller
 * 
 * @author shuofang
 * @date 2020-03-29
 */
@Api("歌单信息管理")
@Controller
@RequestMapping("/work/songlist")
public class SongListController extends BaseController
{
	private static final String STATUS = "work_song_status";
	
    private String prefix = "work/songlist";

    @Autowired
    private ISongListService songListService;

    @Autowired
    private IWorkFileService workFileService;

    @Autowired
    private ISysDictTypeService dictTypeService;
    
    @RequiresPermissions("work:songlist:view")
    @GetMapping()
    public String songlist()
    {
        return prefix + "/songlist";
    }
    
    @RequiresPermissions("work:songlist:view")
    @GetMapping("/listselect")
    public String listselect()
    {
    	return prefix + "/listselect";
    }

    /**
     * 查询歌单列表
     */
    @RequiresPermissions("work:songlist:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SongList songList)
    {
    	if(StringUtils.isNull(songList.getStatus())) {
    		songList.setStatus(WorkConstants.NORMAL);
    	}
    	songList.setListUserId(ShiroUtils.getUserId());
        startPage();
        List<SongList> list = songListService.selectSongListList(songList);
        return getDataTable(list);
    }
    /**
     * 查询歌单列表
     */
    @ApiOperation("查询歌单列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "listName", value = "查询的歌单名称，支持模糊查询，为空默认查询全部", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "listId", value = "查询的指定歌单id，为空则默认查询全部", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "statu", value = "歌单状态,0为查询启用的歌单,1为查询停用的歌单,为空则查询全部", dataType = "String", paramType = "query"),
    })
    @RequiresPermissions("work:songlist:list")
    @GetMapping("/listAll")
    @ResponseBody
    public AjaxResult listAll(String listName, String statu,Long listId)
    {
    	SongList songList = new SongList();
    	try {
        	if(StringUtils.isNotEmpty(statu)) {
        		songList.setStatus(statu);
        	}
        	if(StringUtils.isNotEmpty(listName)) {
        		songList.setListName(listName);
        	}
        	if(StringUtils.isNotNull(listId)) {
        		songList.setListId(listId);
        	}
        	songList.setListUserId(ShiroUtils.getUserId());
        	List<SongList> list = songListService.selectSongListList(songList);
        	AjaxResult result = success();
        	result.put(AjaxResult.DATA_TAG, list);
        	return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error();
    }

    /**
     * 新增歌单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存歌单
     */
    @RequiresPermissions("work:songlist:add")
    @Log(title = "歌单管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SongList songList)
    {
    	songList.setListUserId(ShiroUtils.getUserId());
        songList.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(songListService.insertSongList(songList));
    }
    /**
     * 新增保存歌单
     */
    @ApiOperation("新增保存歌单")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "listName", value = "歌单名称", required = true,  dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "songData", value = "歌单包含音频ID组，逗号隔开", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "status", value = "歌单状态，0，启用，1，停用, 为空则默认启用", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "remark", value = "备注，可为空",dataType = "String", paramType = "query"),
    })
    @RequiresPermissions("work:songlist:add")
    @Log(title = "歌单管理", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
    @PostMapping("/addSongList")
    @ResponseBody
    public AjaxResult addSongList(String listName,String status,String songData,String remark)
    {
    	try {
        	SongList songList = new SongList();
        	if(StringUtils.isEmpty(listName)) {
        		return error("歌单名称为空！");
        	}
        	songList.setListName(listName);
        	if(StringUtils.isNotEmpty(status) && "01".contains(status)) {
        		songList.setStatus(status);
        	}
        	songList.setSongData(songData);
        	songList.setRemark(remark);
        	songList.setListUserId(ShiroUtils.getUserId());
        	songList.setCreateBy(ShiroUtils.getLoginName());
        	return toAjax(songListService.insertSongList(songList));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error();
    }

    /**
     * 修改歌单
     */
    @GetMapping("/edit/{listId}")
    public String edit(@PathVariable("listId") Long listId, ModelMap mmap)
    {
        SongList songList = songListService.selectSongListById(listId);
        mmap.put("songList", songList);
        return prefix + "/edit";
    }

    /**
     * 修改保存歌单
     */
    @ApiOperation("修改保存歌单")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "listId", value = "修改的歌单ID", required = true,  dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "listName", value = "歌单名称,为空则默认不修改", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "status", value = "歌单状态，0，启用，1，停用，为空则默认不修改", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "songData", value = "歌单包含音频ID组，逗号隔开，为空则不然不修改", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "remark", value = "备注，可为空，为空则不然不修改",dataType = "String", paramType = "query"),
    })
    @RequiresPermissions("work:songlist:edit")
    @Log(title = "歌单管理", businessType = BusinessType.UPDATE, operatorType = OperatorType.MOBILE)
    @PostMapping("/editSongList")
    @ResponseBody
    public AjaxResult editSongList(String listId,String listName,String status,String songData,String remark)
    {
    	try {
    		SongList songList = new SongList();
        	if(StringUtils.isEmpty(listId) && StringUtils.isNull(Long.parseLong(listId))) {
        		return error("歌单ID为空！");
        	}
    		songList.setListId(Long.parseLong(listId));
    		if(StringUtils.isNotEmpty(listName)){
    			songList.setListName(listName);
    		}
    		if(StringUtils.isNotEmpty(status) && "01".contains(status)){
    			songList.setListName(status);
    		}
    		if(StringUtils.isNotEmpty(songData)){
    			songList.setSongData(songData);
    		}
    		if(StringUtils.isNotEmpty(remark)){
    			songList.setRemark(remark);
    		}
    		songList.setUpdateBy(ShiroUtils.getLoginName());
    		return toAjax(songListService.updateSongList(songList));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error();
    }
    /**
     * 修改保存歌单
     */
    @RequiresPermissions("work:songlist:edit")
    @Log(title = "歌单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SongList songList)
    {
    	songList.setUpdateBy(ShiroUtils.getLoginName());
    	return toAjax(songListService.updateSongList(songList));
    }

    /**
     * 删除歌单
     */
	@RequiresPermissions("work:songlist:remove")
    @Log(title = "歌单管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(songListService.deleteSongListByIds(ids));
    }
    @ApiOperation("删除歌单")
    @ApiImplicitParam(name = "ids", value = "需要删除的歌单id或者是id组,逗号隔开", required = true,  dataType = "String", paramType = "query")
    @RequiresPermissions("work:songlist:remove")
    @Log(title = "歌单管理", businessType = BusinessType.DELETE, operatorType = OperatorType.MOBILE)
    @PostMapping( "/removeSongList")
    @ResponseBody
    public AjaxResult removeSongList(String ids)
    {
    	try {
    		return toAjax(songListService.deleteSongListByIds(ids));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error();
    }
    /**
     * 查询歌单下音频列表
     */
    @RequiresPermissions("work:songlist:list")
    @PostMapping("/getListById")
    @ResponseBody
    public TableDataInfo getListById(SongList songList)
    {
        List<WorkFile> list = workFileService.selectWorkFileByIds(songList.getSongData());
    	return getDataTable(list);
    }
    /**
     * 查询歌单下音频列表
     */
    @ApiOperation("查询歌单下音频列表")
    @ApiImplicitParam(name = "songData", value = "需要具体音频信息的id组，由逗号分隔，例如：‘1,2,3,4,’（最后一位逗号无影响） 可以直接将歌单种的songData作为查询条件，也可以自行拼接获取", required = true, dataType = "String", paramType = "query")
    @RequiresPermissions("work:songlist:list")
    @GetMapping("/getSongByData")
    @ResponseBody
    public AjaxResult getSongByData(String songData)
    {
    	try {
    		List<WorkFile> list = workFileService.selectWorkFileByIds(songData);
    		AjaxResult result = success();
    		result.put(AjaxResult.DATA_TAG, list);
    		return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error();
    }
    /**
     * 终端状态修改
     */
    @Log(title = "歌单管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("work:songlist:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SongList songList)
    {
        return toAjax(songListService.changeStatus(songList));
    }
    
}
