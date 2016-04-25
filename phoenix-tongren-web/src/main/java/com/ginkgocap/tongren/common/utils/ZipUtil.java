package com.ginkgocap.tongren.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtil {
	static Logger logger = Logger.getLogger(ZipUtil.class);
	/** 
     * 压缩文件-由于out要在递归调用外,所以封装一个方法用来 
     * 调用ZipFiles(ZipOutputStream out,String path,File... srcFiles) 
     * @param zip 
     * @param path 
     * @param srcFiles 
     * @throws IOException 
     * @author isea533 
     */  
    public static void ZipFiles(File zip,String path,File... srcFiles) throws IOException{  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));  
        ZipFiles(out,path,srcFiles);  
        out.close();  
        logger.info("compress file succes "+zip.getPath());  
    }  
    /** 
     * 压缩文件-File 
     * @param zipFile  zip文件 
     * @param srcFiles 被压缩源文件 
     * @author isea533 
     */  
    public static void ZipFiles(ZipOutputStream out,String path,File... srcFiles){  
        path = path.replaceAll("\\*", "/");  
        if(!path.endsWith("/")){  
            path+="/";  
        }  
        byte[] buf = new byte[1024];  
        try {  
            for(int i=0;i<srcFiles.length;i++){  
                if(srcFiles[i].isDirectory()){  
                    File[] files = srcFiles[i].listFiles();  
                    String srcPath = srcFiles[i].getName();  
                    srcPath = srcPath.replaceAll("\\*", "/");  
                    if(!srcPath.endsWith("/")){  
                        srcPath+="/";  
                    }  
                    out.putNextEntry(new ZipEntry(path+srcPath));  
                    ZipFiles(out,path+srcPath,files);  
                }  
                else{  
                    FileInputStream in = new FileInputStream(srcFiles[i]);  
                    out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));  
                    int len;  
                    while((len=in.read(buf))>0){  
                        out.write(buf,0,len);  
                    }  
                    out.closeEntry();  
                    in.close();  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static void unZipFiles(String zipPath)throws IOException{ 
    	int index=zipPath.length();
    	while(--index>=0){
    		if(zipPath.charAt(index)=='\\'||zipPath.charAt(index)=='/'){
    			break;
    		}
    	}
    	if(index>0){
    		 unZipFiles(new File(zipPath), zipPath.substring(0,index)+File.separator); 
    	}else{
    		 unZipFiles(new File(zipPath),System.getProperty("user.dir")+File.separator); 
    	}
        
    }
    /** 
     * 解压到指定目录 
     * @param zipPath 
     * @param descDir 
     * @author isea533 
     */  
    public static void unZipFiles(String zipPath,String descDir)throws IOException{  
        unZipFiles(new File(zipPath), descDir);  
    }  
    /** 
     * 解压文件到指定目录 
     * @param zipFile 
     * @param descDir 
     * @author isea533 
     */  
    public static void unZipFiles(File zipFile,String descDir)throws IOException{  
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  
        ZipFile zip = new ZipFile(zipFile);  
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){  
            ZipEntry entry = (ZipEntry)entries.nextElement();  
            String zipEntryName = entry.getName();  
            InputStream in = zip.getInputStream(entry);  
            String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;  
            //判断路径是否存在,不存在则创建文件路径  
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
            if(!file.exists()){  
                file.mkdirs();  
            }  
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
            if(new File(outPath).isDirectory()){  
                continue;  
            }  
            //输出文件路径信息  
            OutputStream out = new FileOutputStream(outPath);  
            byte[] buf1 = new byte[1024];  
            int len;  
            while((len=in.read(buf1))>0){  
                out.write(buf1,0,len);  
            }  
            in.close();  
            out.close();  
            }  
        logger.info("unzip success!"+zipFile.getAbsolutePath()+","+descDir);  
    }  
    
    public static String zipDir(String dir){
    	logger.info("begin compress dir "+dir);
    	int index=dir.lastIndexOf(File.separator);
    	String dirName=dir.substring(index+1);
    	try {
			ZipFiles(new File(dir+".zip"), dirName, new File(dir).listFiles());
			return dir+".zip";
		} catch (Exception e) {
			logger.error("comprees dir failed "+dir,e);
			return null;
		}
    	
    }
    
//    public static void main(String[] args) {
//    	String path="D:\\tmphtml\\2016\\01\\15\\847444";
//    	zipDir(path);
//	}
    public static void main(String[] args) {
    	String path="D:\\tmp\\img\\2016\\01\\18\\213123123123.zip";
    	try {
			unZipFiles(new File(path),"D:\\tmp\\img\\2016\\01\\18\\");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
