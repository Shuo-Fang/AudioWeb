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
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.json.JSON;
import com.audioweb.work.domain.SongList;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.ISongListService;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 歌单Controller
 * 
 * @author shuofang
 * @date 2020-03-29
 */
@Controller
@RequestMapping("/work/songlist")
public class SongListController extends BaseController
{
    private String prefix = "work/songlist";

    @Autowired
    private ISongListService songListService;

    @Autowired
    private IWorkFileService workFileService;

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
    	songList.setListUserId(ShiroUtils.getUserId());
        startPage();
        List<SongList> list = songListService.selectSongListList(songList);
        return getDataTable(list);
    }

    /**
     * 导出歌单列表
     */
    @RequiresPermissions("work:songlist:export")
    @Log(title = "歌单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SongList songList)
    {
        List<SongList> list = songListService.selectSongListList(songList);
        ExcelUtil<SongList> util = new ExcelUtil<SongList>(SongList.class);
        return util.exportExcel(list, "songlist");
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
    @Log(title = "歌单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SongList songList)
    {
    	songList.setListUserId(ShiroUtils.getUserId());
        songList.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(songListService.insertSongList(songList));
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
    @RequiresPermissions("work:songlist:edit")
    @Log(title = "歌单", businessType = BusinessType.UPDATE)
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
    @Log(title = "歌单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(songListService.deleteSongListByIds(ids));
    }
    /**
     * 查询歌单列表
     */
    @RequiresPermissions("work:songlist:list")
    @PostMapping("/getListById")
    @ResponseBody
    public TableDataInfo getListById(SongList songList)
    {
        List<WorkFile> list = workFileService.selectWorkFileByIds(songList.getSongData());
    	return getDataTable(list);
    }
}
