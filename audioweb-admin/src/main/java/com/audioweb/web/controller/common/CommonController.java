package com.audioweb.web.controller.common;

import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.audioweb.common.annotation.Log;
import com.audioweb.common.config.Global;
import com.audioweb.common.config.ServerConfig;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.audio.Mp3Utils;
import com.audioweb.common.utils.file.FileUploadUtils;
import com.audioweb.common.utils.file.FileUtils;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.system.service.ISysConfigService;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@Api("文件上传管理")
@Controller
public class CommonController {
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ServerConfig serverConfig;    
	
	@Autowired
    private ISysConfigService configService;

    @Autowired
    private IWorkFileService workFileService;

	/**
	 * 通用下载请求
	 * 
	 * @param fileName
	 *            文件名称
	 * @param delete
	 *            是否删除
	 */
	@GetMapping("common/download")
	public void fileDownload(String fileName, Boolean delete, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			if (!FileUtils.isValidFilename(fileName, FileUtils.AUDIONAME_PATTERN)) {
				throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
			}
			String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
			String filePath = Global.getDownloadPath() + fileName;

			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
			FileUtils.writeBytes(filePath, response.getOutputStream());
			if (delete) {
				FileUtils.deleteFile(filePath);
			}
		} catch (Exception e) {
			log.error("下载文件失败", e);
		}
	}

	/**
	 * 通用上传请求
	 */
	@PostMapping("/common/upload")
	@ResponseBody
	public AjaxResult uploadFile(@RequestParam("file_data")MultipartFile file) throws Exception {
		try {
			// 上传文件路径
			String filePath = Global.getUploadPath();
			// 上传并返回新文件名称
			String fileName = FileUploadUtils.upload(filePath, file, true);
			String url = serverConfig.getUrl() + fileName;
			AjaxResult ajax = AjaxResult.success();
			ajax.put("fileName", fileName);
			ajax.put("url", url);
			return ajax;
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 本地资源通用下载
	 */
	@GetMapping("/common/download/resource")
	public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 本地资源路径
		String localPath = Global.getProfile();
		// 数据库资源地址
		String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
		// 下载名称
		String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
		FileUtils.writeBytes(downloadPath, response.getOutputStream());
	}

	 /**
     * 通用上传请求
     */
    @ApiOperation("音频文件上传管理")
    @ApiImplicitParams ({
    	@ApiImplicitParam(name = "type", value = "上传音频文件属性类型,work.file为文件广播文件,work.point为终端采播文件,work.word为文本广播文件", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "file_data", value = "上传的音频文件", required = true, dataType = "file", paramType = "form")
    })
    @RequestMapping(value = "/common/audio/upload", method = RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions("work:file:add")
    @Log(title = "音频存储信息", businessType = BusinessType.INSERT)
	public AjaxResult uploadReport(HttpServletRequest request, HttpServletResponse response) {
		if (ServletFileUpload.isMultipartContent(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> fileList = multipartRequest.getFiles("file_data");
			try {
				// 上传文件路径
				String value = multipartRequest.getParameterMap().get("type")[0];
				String filePath = configService.selectConfigByKey(value);
				filePath = FileUtils.formatPath(FileUtils.formatToLin(filePath));
				for (MultipartFile item : fileList) {
					try {
						// 上传并返回新文件名称
						File file = new File(filePath+"/"+item.getOriginalFilename());
						if(file.exists()) {
							return AjaxResult.error("此文件已存在！");
						}else {
							String fileName = FileUploadUtils.upload(filePath, item, false);
							if(Mp3Utils.isMp3(fileName)){
								WorkFile fWorkFile = workFileService.insertWorkFile(filePath, fileName, value.equals("work.file")?"0":"1",ShiroUtils.getLoginName());
								if(StringUtils.isNotNull(fWorkFile)) {
									String url = fWorkFile.getVirPath();
									AjaxResult ajax = AjaxResult.success();
									ajax.put("fileName", fWorkFile.getFileName());
									ajax.put("url", serverConfig.getUrl()+url);
									return ajax;
								}else {
									FileUtils.deleteFile(fileName);
									return AjaxResult.error("保存失败");
								}
							}else {
								FileUtils.deleteFile(fileName);
								return AjaxResult.error("并非为MP3文件");
							}
						}
					} catch (Exception e) {
						return  AjaxResult.error(e.getMessage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return AjaxResult.error("格式出错");
	}
}
