package com.ginkgocap.tongren.common.upload;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.UUIDGenerator;



/**
 * FTP文件错做
 * 
 * @author 
 *
 */
public class FtpApche {

	private static FTPClient ftpClient = new FTPClient();

	/**
	 * Description: 向FTP服务器上传文件
	 * @Version1.0
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录,如果是根目录则为“/”
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            本地文件输入流
	 * @return 成功返回String，否则返回文件路径
	 */
	public static String uploadFile(String url, String port, String username,
	String password, String path, String filename, InputStream input) {
		String removePath = null;
		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.connect(url);
			// ftp.connect(url, port);// 连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			
			ftpClient.enterLocalPassiveMode();//设置为被动模式传输
			ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);
			// 检验是否连接成功
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("连接失败");
				ftpClient.disconnect();
			}

			// 转移工作目录至指定目录下
			boolean change = ftpClient.changeWorkingDirectory(path);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 创建子文件夹
			String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
			ftpClient.makeDirectory(dateDir);
			change = ftpClient.changeWorkingDirectory(path+"/"+dateDir);
			if (change) {
				String removeFileName = new String(filename.getBytes(FileInstance.FILE_ENCODING), "UTF-8");
				System.out.println(removeFileName);
				ftpClient.makeDirectory(new String(path.getBytes("UTF-8"),"iso-8859-1"));
				//检查是否有同名文件，如果存在则加上时间后缀。
				removeFileName = browseFiles(removeFileName);
				System.out.println(removeFileName);
				System.out.println(input);
				
				if (ftpClient.storeFile(removeFileName,input)) {
					System.out.println("上传成功!");
					removePath = path + dateDir + "/" + removeFileName; 
				}
				
				
				else{
					System.out.println("false");
				}
			}


		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//关闭连接
				if(input != null){
					input.close();
				}
			} catch (IOException e) {
				System.out.println("关闭流失败！");
				e.printStackTrace();
			}
			try {
				//登出
				if(ftpClient != null){
					ftpClient.logout();
				}
			} catch (IOException e) {
				System.out.println("登出失败！");
				e.printStackTrace();
			}
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return removePath;
	}


/**
 * FTP文件
 * 
 * @author 
 *
 */

	public static List<String> uploadFiles(String url, String port, String username,
			String password, String path, List<String> filenames, List<InputStream> inputs) throws Exception{
				List<String> pathList = new ArrayList<String>();
				try {
					int reply;
					// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
					ftpClient.connect(url);
					// ftp.connect(url, port);// 连接FTP服务器
					// 登录
					ftpClient.login(username, password);
					
					ftpClient.enterLocalPassiveMode();//设置为被动模式传输
					ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);
					// 检验是否连接成功
					reply = ftpClient.getReplyCode();
					
					if (!FTPReply.isPositiveCompletion(reply)) {
						System.out.println("连接失败");
						ftpClient.disconnect();
						throw new Exception("连接失败!");
					}

					// 转移工作目录至指定目录下
					boolean change = ftpClient.changeWorkingDirectory(path);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					// 创建子文件夹
					String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
					ftpClient.makeDirectory(dateDir);
					change = ftpClient.changeWorkingDirectory(path+"/"+dateDir);
					if (change) {
						for(int i=0;i<inputs.size();i++){
							InputStream input = inputs.get(i);
							String filename = filenames.get(i);
							String removeFileName = new String(filename.getBytes(FileInstance.FILE_ENCODING), "UTF-8");
							System.out.println(removeFileName);
							ftpClient.makeDirectory(new String(path.getBytes("UTF-8"),"iso-8859-1"));
							//检查是否有同名文件，如果存在则加上时间后缀。
							removeFileName = browseFiles(removeFileName);
							System.out.println(removeFileName);
							
							if (ftpClient.storeFile(removeFileName,input)) {
								System.out.println("上传成功!");
								pathList.add(path + dateDir + "/" + removeFileName); 
							}
							
							//关闭连接
							if(input != null){
								input.close();
							}
						}
					}else{
						System.out.println("不能访问服务器路径为["+path+"/"+dateDir+"]");
						return pathList=null;
					}


				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("FTP上传失败！");
					
				} finally {
					try {
						//FTP登出
						if(ftpClient != null){
							ftpClient.logout();
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new Exception("FTP登出失败！");
					}
					// 如果ftp上传打开，就关闭掉
					if (ftpClient.isConnected()) {
						try {
							ftpClient.disconnect();
						} catch (IOException e) {
							e.printStackTrace();
							throw new Exception("FTP关闭失败！");
						}
					}
				}
				return pathList;
			}
	/**
	 * 通过传过来的图片大小进行图片压缩并上传到ftp
	 * @param weight
	 * @param hight
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param filename
	 * @param input
	 * @return Map(removePath：压缩后图片上传到ftp的路径,
	 *             bigPath压缩后的文件名，
                   removeFileName压缩前的文件名)

	 */
	public static Map<String,String> toUploadFile(int weight,int hight,String url, String port, String username,
			String password, String path, String filename, InputStream input) {
				String removePath = null;
				Map<String,String> map=new HashMap<String,String>();
				File file1 = null;
				File file2 = null;
				try {
					
					int reply;
					// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
					ftpClient.connect(url);
					// ftp.connect(url, port);// 连接FTP服务器
					// 登录
					ftpClient.login(username, password);
					ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);
					ftpClient.enterLocalPassiveMode();//设置为被动模式传输
					// 检验是否连接成功
					reply = ftpClient.getReplyCode();
					if (!FTPReply.isPositiveCompletion(reply)) {
						System.out.println("连接失败");
						ftpClient.disconnect();
					}

					// 转移工作目录至指定目录下
					boolean change = ftpClient.changeWorkingDirectory(path);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					// 创建子文件夹
					String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
					ftpClient.makeDirectory(dateDir);
					change = ftpClient.changeWorkingDirectory(path+"/"+dateDir);
					if (change) {
						String removeFileName = new String(filename.getBytes(FileInstance.FILE_ENCODING), "UTF-8");
						//检查是否有同名文件，如果存在则加上时间后缀。
						removeFileName = browseFiles(removeFileName);
						map.put("removeFileName", removeFileName);//压缩前的文件名
						//1.源文件保存在本地
						//对文件进行写操作
						file1=new File(removeFileName);
			            FileOutputStream fos=new FileOutputStream(file1); 
			            byte[] buffer=new byte[1024]; 
			            int len=0; 
			            //读入流，保存至byte数组
			            while((len=input.read(buffer))>0){ 
		                fos.write(buffer,0,len); 
			            }
			            fos.close();
			            FileInputStream fis1=new FileInputStream(removeFileName); 
			            
			            String bigPath = bigPathName(removeFileName);
			            
			            
						 
					
						String ext = removeFileName.substring(removeFileName.lastIndexOf(".") + 1,removeFileName.length());
						
						
						
						BufferedImage biBig = ImageIO.read(fis1);
						biBig = BufferUtil.resize(biBig, weight, hight); 
						file2=new File(bigPath);
						ImageIO.write(biBig, ext, file2);
						
						
						
						
			            FileInputStream fisBig=new FileInputStream(bigPath); 
						
			    		
						if (ftpClient.storeFile(bigPath,fisBig)) {
							System.out.println("上传成功!");
							removePath = path + dateDir + "/" + bigPath; 
							map.put("bigPath", bigPath);//bigPath压缩后的文件名
							map.put("removePath", removePath);//压缩后图片上传到ftp的路径
							/*ftpClient.deleteFile(removePath);
							ftpClient.deleteFile(bigPath);*/
						}
			            
					}
					
					input.close();
					ftpClient.logout();
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

					if (ftpClient.isConnected()) {
						try {
							ftpClient.disconnect();
							System.out.println(file1.isFile()+"+++++++++++++++++++++++++++++++++++++++++");
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}
				return map;
			}
	
	/**
	 * 上传 文件并压缩
	 * by chunlong.song@rogrand.com
	 * 2014-07-10
	 * */
	public static Map<String,String> uploadFileResize(String url, String port, String username,
			String password, String path, String filename, InputStream input) {
				String removePath = null;
				Map<String,String>map=new HashMap<String,String>();
				try {
					int reply;
					// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
					ftpClient.connect(url);
					// ftp.connect(url, port);// 连接FTP服务器
					// 登录
					ftpClient.login(username, password);
					ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);
					ftpClient.enterLocalPassiveMode();//设置为被动模式传输
					// 检验是否连接成功
					reply = ftpClient.getReplyCode();
					if (!FTPReply.isPositiveCompletion(reply)) {
						System.out.println("连接失败");
						ftpClient.disconnect();
					}

					// 转移工作目录至指定目录下
					boolean change = ftpClient.changeWorkingDirectory(path);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					// 创建子文件夹
					String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
					ftpClient.makeDirectory(dateDir);
					change = ftpClient.changeWorkingDirectory(path+"/"+dateDir);
					if (change) {
						String removeFileName = new String(filename.getBytes(FileInstance.FILE_ENCODING), "UTF-8");
						//检查是否有同名文件，如果存在则加上时间后缀。
						removeFileName = browseFiles(removeFileName);
						
						//1.源文件保存在本地
						//对文件进行写操作
			            FileOutputStream fos=new FileOutputStream(removeFileName); 
			            byte[] buffer=new byte[1024]; 
			            int len=0; 
			            //读入流，保存至byte数组
			            while((len=input.read(buffer))>0){ 
			                fos.write(buffer,0,len); 
			            }
			            fos.close();
			            FileInputStream fis1=new FileInputStream(removeFileName); 
			            FileInputStream fis2=new FileInputStream(removeFileName); 
			            FileInputStream fis3=new FileInputStream(removeFileName); 
			            FileInputStream fis4=new FileInputStream(removeFileName); 
			            
			            String bigPath = bigPathName(removeFileName);;
			            String midPath = midPathName(removeFileName);
			        	String smPath = smPathName(removeFileName);
						String mmPath = mmPathName(removeFileName);
			            
			            
						
					
						String ext = removeFileName.substring(removeFileName.lastIndexOf(".") + 1,removeFileName.length());
						
						
						
						BufferedImage biBig = ImageIO.read(fis1);
						biBig = BufferUtil.resize(biBig, Integer.parseInt(FileInstance.BIG_IMG_WEIGHT), Integer.parseInt(FileInstance.BIG_IMG_HEIGHT));         
						ImageIO.write(biBig, ext, new File(bigPath));
						
						
						
						BufferedImage midBig = ImageIO.read(fis2);
						midBig = BufferUtil.resize(midBig, Integer.parseInt(FileInstance.MID_IMG_WEIGHT), Integer.parseInt(FileInstance.MID_IMG_HEIGHT));         
						ImageIO.write(midBig, ext, new File(midPath));
						
					
						
						
						BufferedImage smBig = ImageIO.read(fis3);
						smBig = BufferUtil.resize(midBig, Integer.parseInt(FileInstance.SM_IMG_WEIGHT), Integer.parseInt(FileInstance.SM_IMG_HEIGHT));         
						ImageIO.write(smBig, ext, new File(smPath));
						
						
						BufferedImage mmBig = ImageIO.read(fis4);
						mmBig = BufferUtil.resize(mmBig, Integer.parseInt(FileInstance.MM_IMG_WEIGHT), Integer.parseInt(FileInstance.MM_IMG_HEIGHT));         
						ImageIO.write(mmBig, ext, new File(mmPath));
						
						
				
						//System.out.println("removeFileName:"+removeFileName+"bigPath:"+bigPath+"midPath:"+midPath+"smPath:"+smPath+"mmPath:"+mmPath);
						FileInputStream fisResource=new FileInputStream(removeFileName); 
			            FileInputStream fisBig=new FileInputStream(bigPath); 
			            FileInputStream fisMid=new FileInputStream(midPath); 
			            FileInputStream fisSm=new FileInputStream(smPath); 
			            FileInputStream fisMm=new FileInputStream(mmPath);
						
			    		
						if (ftpClient.storeFile(removeFileName,fisResource)) {
							System.out.println("上传成功!");
							removePath = path + dateDir + "/" + removeFileName; 
							//System.out.println(removeFileName);
						}
			            
						//ftpClient.storeFile(removeFileName,fisResource);
						ftpClient.storeFile(bigPath,fisBig);
						ftpClient.storeFile(midPath,fisMid);
						ftpClient.storeFile(smPath,fisSm);
						ftpClient.storeFile(mmPath,fisMm);
					}
					
					input.close();
					ftpClient.logout();
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (ftpClient.isConnected()) {
						try {
							ftpClient.disconnect();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}
				return map;
			}
	
	public static String browseFiles(String removeFileName) throws IOException{
		if(removeFileName!=null && !"".equals(removeFileName)){
					String name = removeFileName.substring(0,removeFileName.indexOf("."));
					String suffix = removeFileName.substring(removeFileName.indexOf("."));
					String upName = UUIDGenerator.getUUID();
					removeFileName= upName + suffix;
		}
		return removeFileName;
	}
	
	public static String bigPathName(String imgName) throws IOException{
		if(imgName==null){
			return null;
		}
		String bigTag = "_big";
		String name = imgName.substring(0,imgName.indexOf("."));
		String suffix = imgName.substring(imgName.indexOf("."));
		return name+bigTag+suffix;
	}
	
	public static String midPathName(String imgName) throws IOException{
		if(imgName==null){
			return null;
		}
		String midTag = "_mid";
		String name = imgName.substring(0,imgName.indexOf("."));
		String suffix = imgName.substring(imgName.indexOf("."));
		return name+midTag+suffix;
	}
	
	public static String smPathName(String imgName) throws IOException{
		if(imgName==null){
			return null;
		}
		String smTag = "_sm";
		String name = imgName.substring(0,imgName.indexOf("."));
		String suffix = imgName.substring(imgName.indexOf("."));
		return name+smTag+suffix;
	}

	public static String mmPathName(String imgName) throws IOException{
		if(imgName==null){
			return null;
		}
		String mmTag = "_mm";
		String name = imgName.substring(0,imgName.indexOf("."));
		String suffix = imgName.substring(imgName.indexOf("."));
		return name+mmTag+suffix;
	}
	
	/**
	 * 将本地文件上传到FTP服务器上
	 * 
	 */
	public static String testUpLoadFromDisk(InputStream in,String path,String fileName) {
		String removePath = uploadFile(FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, path,
				fileName, in);
		
//		removePath = FileInstance.PROTOCOL+FileInstance.FTP_IP+removePath;

		System.out.println(removePath);
		return removePath;
	}
	public static List<String> testUpLoadFromDisks(List<InputStream> ins,String path,List<String> fileNames) throws Exception{
		return uploadFiles(FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, path,
				fileNames, ins);
	}
	/**
	 * 将本地文件上传到FTP服务器上
	 * 
	 */
	public static Map<String,String> uploadResizeImg(InputStream in,String path,String fileName) {
		Map<String,String> removePath = uploadFileResize(FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, path,fileName, in);
		System.out.println(removePath.size());
		return removePath;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * @Version1.0
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, String port, String username,
	String password, String remotePath, String fileName,
	String localPath) throws Exception{
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(FileInstance.FILE_ENCODING);

			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替
			 * new String(remotePath.getBytes(encoding),"iso-8859-1")转码。
			 * 经过测试，通不过。
			 */
			// FTPClientConfig conf = new
			// FTPClientConfig(FTPClientConfig.SYST_NT);
			// conf.setServerLanguageCode("zh");
			int pot=21;
			if(port!=null){
				pot = Integer.parseInt(port);
			}
			ftpClient.connect(url, pot);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.login(username, password);// 登录
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return result;
			}

			// 转移到FTP服务器目录至指定的目录下
			ftpClient.changeWorkingDirectory(new String(remotePath
					.getBytes(FileInstance.FILE_ENCODING), FileInstance.ENCODING));
			// 获取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.retrieveFile(ff.getName(), is);
					is.close();
					break;
				}
			}
			ftpClient.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}

		return result;
	}
	/**
	 * 将本地文件上传到FTP服务器上(有压缩 )
	 * @param weight
	 * @param hight
	 * @param in
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static Map<String,String> testUpLoad(int weight,int hight,InputStream in,String path,String fileName) {
		Map<String,String> map = toUploadFile(weight,hight,FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, path,
				fileName, in);
		
//		removePath = FileInstance.PROTOCOL+FileInstance.FTP_IP+removePath;

//		System.out.println(removePath);
		return map;
	}
	/**
	 * 将FTP服务器上文件下载到本地
	 */
	public static void testDownFile(String path,String name) {
		try {
			boolean flag = downFile(FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME,
			FileInstance.FTP_PASSWORD, path, name, FileInstance.DOWNLOAD_PATH);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从ftp上删除
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static boolean removeFile(String path)throws Exception 
	         
	    {  
	        boolean success = false;  
	        FTPClient ftp = new FTPClient();
	        try  
	        {  
	            int reply;  
	            String url=FileInstance.FTP_IP;
	            String username=FileInstance.FTP_USER_NAME;
	            String password=FileInstance.FTP_PASSWORD;
	            String port=FileInstance.FTP_PORT;
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				ftp.connect(url);
				// ftp.connect(url, port);// 连接FTP服务器
				// 登录
				ftp.login(username, password);
	            reply = ftp.getReplyCode(); 
	            System.out.println(reply);
	            if (!FTPReply.isPositiveCompletion(reply))  
	            {  
	                ftp.disconnect();  
	                return success;  
	            }  
	            ftp.changeWorkingDirectory(path); 
	           if(ftp.deleteFile(path)){  
	            ftp.logout();  
	            success = true;  
	        }  
	           else{
		        	success=false;
		        }
	        }
	       
	        catch (IOException e)  
	        {  
	            success = false;  
	            throw e;
	        }  
	        finally  
	        {  
	            if (ftp.isConnected())  
	            {  
	                try  
	                {  
	                    ftp.disconnect();  
	                }  
	                catch (IOException e)  
	                {  
	                    throw e;
	                }  
	            }  
	        }  
	        return success;  
	    }  

//	public static void main(String[] args) {
//		try {
//			String path = "/rrs_photo/20150113/c87bd8a79efe41248855b616aea79e52_big.jpg";
//			boolean f;
//			f = removeFile(path);
//			System.out.println(f);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public static void main(String[] args) {
		try {
			
			String path = System.getProperty("user.dir") + File.separator + "imgTemp";
			System.out.println(path);
//					testUpLoadFromDisk(in, path, fileName)
			boolean isCheck = downFile(FileInstance.FTP_URL, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "/images/20151104164247", "5C514F607F823831446626567995.png", path);
			System.out.println(isCheck);
//			String str = FilenameUtils.getName("/images/20151104120327/F4A18FB80C248FB1446609807295.jpg");
//			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
