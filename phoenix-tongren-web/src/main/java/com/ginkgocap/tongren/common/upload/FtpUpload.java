/*
 * 文件名： FtpUpload.java
 * 创建日期： 2015年10月14日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.common.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.UploadType;


 /**
 * ftp上传方法
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月14日
 */
public class FtpUpload {
	
	private static final Logger logger = LoggerFactory.getLogger(FtpUpload.class);

    public static final String ENCODING = "UTF-8";

//    private static String FTP_BASEPATH = "/webserver/upload/ftpdir";
    
    private static DateFormat dateDirFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private FTPClient ftpClient;

    /**
     * 描述：〈连接服务器〉 <br/>
     * 
     * @param server 服务器地址
     * @param port 服务器端口
     * @param user 用户名
     * @param password 密码
     * @param path 初始路径
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public boolean connect(String server, int port, String user, String password, String path) throws SocketException,
            IOException {
    	logger.info("begin Connected to " + server + ":"+port+",user:"+user+",password:"+password+",path:"+path);
        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);
        logger.info("Connected to " + server + ".");
        logger.info("FTP server reply code:" + ftpClient.getReplyCode());
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(user, password)) {
                if (path != null && path.trim().length() != 0) {
                    ftpClient.changeWorkingDirectory(path);
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                }
                return true;
            }
        }

        disconnect();

        return false;
    }

    /**
     * 描述：〈关闭连接〉 <br/>
     * 
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    /**
     * 描述：〈递归创建目录结构〉 <br/>
     * 
     * @param path 目录路径（以“/”结尾）
     * @throws IOException
     */
    public void createDirectory(String path) throws IOException {
        if (path == null || path.trim().length() == 0)
            return;

        if (!existDirectory(path)) {
            String dir = path.substring(0, path.lastIndexOf("/"));
            createDirectory(dir);
            logger.debug("创建目录[" + dir + "]");
            ftpClient.makeDirectory(dir);
        }
    }

    /**
     * 描述：〈切换目录〉 <br/>
     * 
     * @param path 新目录
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {
        return ftpClient.changeWorkingDirectory(path);
    }

    /**
     * 描述：〈移除空目录〉 <br/>
     * 
     * @param path 目录路径（必须为空目录）
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {
        return ftpClient.removeDirectory(path);
    }

    /**
     * 描述：〈递归方式移除目录结构〉 <br/>
     * 
     * @param path 目录结构
     * @param isForce 是否强制移除
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isForce) throws IOException {
        if (!isForce) {
            return removeDirectory(path);
        }

        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr == null || ftpFileArr.length == 0) {
            return removeDirectory(path);
        }

        for (FTPFile ftpFile : ftpFileArr) {
            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                logger.info("* [sD]Delete subPath [" + path + "/" + name + "]");
                removeDirectory(path + "/" + name, true);
            } else if (ftpFile.isFile()) {
                logger.info("* [sF]Delete file [" + path + "/" + name + "]");
                deleteFile(path + "/" + name);
            } else if (ftpFile.isSymbolicLink()) {

            } else if (ftpFile.isUnknown()) {

            }
        }
        return ftpClient.removeDirectory(path);
    }

    /**
     * 描述：〈指定目录是否存在〉 <br/>
     * 
     * @param path 目录路径
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path) throws IOException {
        /*
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
        */
        try {
            int retCode = ftpClient.cwd(path);
            return FTPReply.isPositiveCompletion(retCode);
        } catch (Exception e) {
            logger.warn("目录[" + path + "]不存在.");
            return false;
        }
    }
    
    /**
     * 描述：〈判断文件是否存在〉 <br/>
     * 
     * @param path 文件地址,如:/kkmy/upload/picture/merchant/2013/12/12/13554390084
     * @return 实际文件地址,如:/kkmy/upload/picture/merchant/2013/12/12/13554390084.jpg
     * @throws IOException 
     */
    public String existFile(String path) throws IOException {
        String fileName = FilenameUtils.getBaseName(path);
        String savePath = path.substring(0, path.lastIndexOf("/") + 1);
        
        FTPFile[] ftpFileArr = ftpClient.listFiles(savePath);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isFile()) {
                String baseName = FilenameUtils.getBaseName(ftpFile.getName());
                if(baseName.equalsIgnoreCase(fileName)) {
                    return savePath + ftpFile.getName();
                }
            }
        }
        return null;
    }

    /**
     * 描述：〈删除文件〉 <br/>
     * 
     * @param pathName 文件路径名
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {
        return ftpClient.deleteFile(pathName);
    }

    /**
     * 描述：〈移动文件〉 <br/>
     * 
     * @param from 原始文件路径
     * @param to 目标文件路径
     * @return
     * @throws IOException
     */
    public boolean moveFile(String from, String to) throws IOException {
        createDirectory(to);

        return ftpClient.rename(from, to);
    }

    /**
     * 描述：〈获取目录下的文件列表〉 <br/>
     * 
     * @param path 目录路径
     * @return
     * @throws IOException
     */
    public List<String> getFileList(String path) throws IOException {
        return getFileList(path, null);
    }

    /**
     * 描述：〈获取目录下的文件列表〉 <br/>
     * 
     * @param path 目录路径
     * @param filter 文件过滤器
     * @return
     * @throws IOException
     */
    public List<String> getFileList(String path, FTPFileFilter filter) throws IOException {
        FTPFile[] ftpFiles = null;
        
        if (filter != null) {
            ftpFiles = ftpClient.listFiles(path, filter);
        } else {
            ftpFiles = ftpClient.listFiles(path);
        }
        
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param type 上传类型
     * @param file 上传文件
     * @return
     * @throws IOException
     */
    public String uploadFile(UploadType type, File file) throws IOException {
        if (file == null || !file.isFile()) {
            return null;
        }

        return uploadFile(type, file.getPath());
    }

    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param type 上传类型
     * @param filePath 本地文件路径
     * @return
     * @throws IOException
     */
    public String uploadFile(UploadType type, String filePath) throws IOException {
        return uploadFile(filePath, genNewFilePath(type, FilenameUtils.getName(filePath)));
    }

    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param filePath 本地文件名路径
     * @param newFilePath 远程文件名路径
     * @return
     * @throws IOException
     */
    public String uploadFile(String filePath, String newFilePath) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            return uploadFile(in, newFilePath);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param type 上传类型
     * @param in 上传文件流
     * @param fileName 原始文件名
     * @return
     * @throws IOException
     */
    public String uploadFile(UploadType type, InputStream in, String fileName) throws IOException {
        if (fileName == null || fileName.length() == 0)
            return null;

        return uploadFile(in, genNewFilePath(type, fileName));
    }

    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param in 上传文件流
     * @param newFilePath 上传文件路径
     * @return
     * @throws IOException
     */
    public String uploadFile(InputStream in, String newFilePath) throws IOException {
        createDirectory(newFilePath);
        
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        if (ftpClient.storeFile(new String(newFilePath.getBytes("GBK"),"iso-8859-1"), in))
            return newFilePath;
        else
            return null;
    }

    /**
     * 描述：〈生成上传文件名〉 <br/>
     * 
     * @param type 上传类型
     * @param fileName 原始文件名
     * @return 上传文件名
     */
    public String genNewFilePath(UploadType type, String fileName) {
        String dateDir = dateDirFormat.format(new Date());
        // String name = FilenameUtils.getBaseName(fileName);
        // String extension = FilenameUtils.getExtension(fileName);
        if (UploadType.OR_PIC == type) {
            return FileInstance.REMOVE_FILE_ROOT_PATH + "/picture/organization/" + dateDir + "/" + fileName;
        } else if (UploadType.TEMP == type) {
            return FileInstance.REMOVE_FILE_ROOT_PATH + "/" + type.getName() + "/" + fileName;
        } else {
            /*
             * return FTP_BASEPATH + "/" + type.getName() + "/" + dateDir + "/"
             * + UUID.randomUUID().toString().replace("-", "") + "." +
             * extension;
             */
            return FileInstance.REMOVE_FILE_ROOT_PATH + "/" + type.getName() + "/" + dateDir + "/" + fileName;
        }
    }

    /**
     * 描述：〈下载文件〉 <br/>
     * 
     * @param sourceFilePath 远程文件路径
     * @param localFilePath 本地文件路径
     * @return
     * @throws IOException
     */
    public boolean downloadFile(String sourceFilePath, String localFilePath) throws IOException {
        File file = new File(localFilePath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            return ftpClient.retrieveFile(sourceFilePath, out);
        } finally {
            out.close();
        }
    }

    /**
     * 描述：〈下载文件到流〉 <br/>
     * 
     * @param sourceFilePath 源文件路径
     * @return
     * @throws IOException
     */
    public InputStream downloadFile(String sourceFilePath) throws IOException {
        return ftpClient.retrieveFileStream(sourceFilePath);
    }
    
    /**
     * 描述：〈上传文件〉 <br/>
     * 
     * @param in 上传文件流
     * @param newFilePath 上传文件路径
     * @return
     * @throws IOException
     */
    public boolean uploadDir(String localPath, String remotePath) throws IOException {
        File localDir = new File(localPath);
        if (localDir != null && localDir.exists() && localDir.isDirectory()) {
            String dirName = new String(localDir.getName().getBytes(FileInstance.FILE_ENCODING), "ISO-8859-1");
            logger.debug("准备上传目录[" + localDir + "]下的文件...");
            return processDir(localDir, remotePath.concat(dirName).concat("/"));
        }
        return false;
    }
    /**
     * 功能描述：  上传       
     *                                                       
     * @param localPath 本地路径 绝对路径 
     * @param remotePath 远程路径
     * @return
     * @author 林阳 [linyang@gintong.com]
     * @throws Exception 
     * @since 2015年11月5日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
	public boolean uploadFileProcessing(String localPath, String remotePath) throws Exception {
		File localFile = new File(localPath);
		if (localFile.exists() == false) {
			return false;
		}
		FileInputStream in = new FileInputStream(localFile);
		String name = new String(localFile.getName().getBytes(FileInstance.FILE_ENCODING), "ISO-8859-1");
		String remoteFilePath = remotePath.concat("/").concat(name);
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        ftpClient.enterLocalPassiveMode();
			return ftpClient.storeFile(remoteFilePath, in);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
			}
		}
	}
    
    /**
     * 描述：〈递归处理文件夹〉 <br/>
     * 
     * @param dir 本地目录
     * @param remotePath 远程路径
     * @return
     * @throws IOException 
     */
    private boolean processDir(File dir, String remotePath) throws IOException {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            createDirectory(remotePath);
            
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {// 是文件夹
                        processDir(
                                file,
                                remotePath.concat(new String(file.getName().getBytes(FileInstance.FILE_ENCODING), "ISO-8859-1")).concat(
                                        "/"));
                    } else {// 是文件
                        processFile(file, remotePath);
                    }
                }
            }
            return true;
        } else {
            System.out.println("指定文件夹不存在.");
            return false;
        }
    }

    /**
     * 描述：〈处理文件〉 <br/>
     * 
     * @param file 本地文件
     * @param remotePath 远程路径
     * @return
     * @throws IOException 
     */
    private boolean processFile(File file, String remotePath) throws IOException {
        String name = new String(file.getName().getBytes(FileInstance.FILE_ENCODING), "ISO-8859-1");
        String remoteFilePath = remotePath.concat(name);

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            logger.debug("上传文件[" + file.getPath() + "]");
            return ftpClient.storeFile(remoteFilePath, in);
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
   /**
    * 功能描述：     验证图片格式
    *                                                       
    * @return
    * @throws Exception                                                                                                 
    * @author 林阳 [linyang@gintong.com]
    * @since 2015年11月5日
    * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
    *
    */
   public boolean isPicture(String fileName) throws Exception{
	   String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png|.ico|.ICO|.tiff|.TIFF|.tif|.TIF|.dib|.DIB)$";
       Pattern pattern = Pattern.compile(reg);
       Matcher matcher = pattern.matcher(fileName.toLowerCase());
	   return matcher.find();
   }
    public static void main(String[] args) throws IOException {
        FtpUpload uploader = new FtpUpload();
        try {
            boolean connected = uploader.connect("192.168.101.12", 21, "admin", "admingintwww123", "/");
//            boolean connected =  uploader.connect(FileInstance.FTP_URL.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "/");
            if (connected) {
//                String savePath = uploader.uploadFile(UploadType.IMAGE, "C:\\test\\1.jpg");
//                System.out.println("savePath:" + savePath);
//                /*
//                 * uploader.moveFile(
//                 * "/kkmy/upload/images/2014/04/03/c84ce59871c74c2e9469e19f06537600.jpg"
//                 * , "/kkmy/upload/temp/c84ce59871c74c2e9469e19f06537600.jpg");
//                 */
//
//                // uploader.createDirectory("/kkmy/upload/aaaa/asdfasdf.jpg");
//                /*List<String> fileNames = uploader.getFileList("/kkmy/upload/pic/");
//                System.out.println(fileNames.size());*/
//                
//                boolean result = uploader.uploadDir("C:\\test", "/images/20151104164247");
//            	boolean result = uploader.uploadFileProcessing("C:\\test", "/images/20151104164247");
//                System.out.println(result);
                //System.out.println(uploader.existFile("/kkmy/upload/picture/merchant/2013/12/12/15102724856_a1"));
                //System.out.println(uploader.existDirectory("/kkmy/upload/activity/html/kkmy-common-provider-2.0.1-SNAPSHOT"));
//            	boolean result  = uploader.downloadFile("c:\\", "/images/20151104120327");
//            	InputStream in = uploader.downloadFile("/object/20151103140844/桐人首页消息提醒规范.doc");
//            	String str = FilenameUtils.getFullPathNoEndSeparator("/object/20151103140844/桐人首页消息提醒规范.doc");
//                System.out.println(str);
            }
            
//            System.out.println(new File("D:\\oracle地址信息.txt").getPath());
//            InputStream in = uploader.downloadFile("/ftpdir//object/20151103140844/桐人首页消息提醒规范.doc","C:/");
            
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            uploader.disconnect();
        }
    }
}
