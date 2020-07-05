package com.audioweb.common.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import com.audioweb.common.config.Global;
import com.audioweb.common.constant.Constants;
import com.audioweb.common.exception.file.FileNameLengthLimitExceededException;
import com.audioweb.common.exception.file.FileSizeLimitExceededException;
import com.audioweb.common.exception.file.InvalidExtensionException;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.security.Md5Utils;

/**
 * 文件上传工具类
 * 
 * @author ruoyi
 */
public class FileUploadUtils
{
    /**
     * 默认大小 100M
     */
    public static final long DEFAULT_MAX_SIZE = 100 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    //private static String defaultBaseDir = Global.getProfile();

    private static int counter = 0;

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException
    {
        try
        {
            return upload(Global.getProfile(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION,true);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件 rename 是否更名
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file, boolean rename) throws IOException
    {
        try
        {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION,rename);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }
    /**
     * 
     * @Title: saveImage 
     * @Description: 保存音频图像信息
     * @param imageData
     * @param fileName
     * @return 图像绝对路径
     * @throws FileSizeLimitExceededException
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException String 返回类型 
     * @throws Exception
     * @author 10155 
     * @date 2020年3月25日 下午8:28:10
     */
    public static final String saveImage(byte[] imageData,String fileName)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
    	String filePath = Global.getImagePath();
        String fileset  = DateUtils.datePath() + "/" + encodingFilename(fileName) + ".jpg";
        File desc = getAbsoluteFile(filePath, fileset);
        FileOutputStream fos = new FileOutputStream(desc);
        fos.write(imageData);
        fos.close();
        return desc.getPath();
    }
    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param rename 是否更名
     * @param extension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension, boolean rename)
    		throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
    		InvalidExtensionException
    {
    	int fileNamelength = file.getOriginalFilename().length();
    	if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
    	{
    		throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
    	}
    	
    	assertAllowed(file, allowedExtension);
    	
    	String fileName = extractFilename(file,rename);
    	
    	File desc = getAbsoluteFile(baseDir, fileName);
    	file.transferTo(desc);
    	String pathFileName = getPathFileName(baseDir, fileName, rename);
    	return pathFileName;
    }

    /**
     * 编码文件名 rename 是否更名
     */
    public static final String extractFilename(MultipartFile file,boolean rename)
    {
        String fileName = file.getOriginalFilename();
        if(rename) {
	        String extension = getExtension(file);
	        fileName = DateUtils.datePath() + "/" + encodingFilename(fileName) + "." + extension;
        }
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName, boolean rename) throws IOException
    {
    	if(rename) {
            int dirLastIndex = Global.getProfile().length() + 1;
            String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
            String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
            return pathFileName;
    	}else {
    		return uploadDir+"/"+fileName;
    	}
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName)
    {
        fileName = fileName.replace("_", " ");
        fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
        return fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException
    {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            }
            else
            {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }

    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     * 
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}