package com.ginkgocap.tongren.common.utils;

import java.util.Random;

import com.ginkgocap.ywxt.framework.dao.id.IdCreator;
import com.ginkgocap.ywxt.framework.dao.id.IdCreatorFactory;
import com.ginkgocap.ywxt.framework.dao.id.exception.CreateIdException;

public class GenerateUtil {
	
	public static String generateNumbering() {
		String no="";  
	        int num[]=new int[8];  
	        int c=0;  
	        for (int i = 0; i < 8; i++) {  
	            num[i] = new Random().nextInt(10);  
	            c = num[i];  
	            for (int j = 0; j < i; j++) {  
	                if (num[j] == c) {  
	                    i--;  
	                    break;  
	                }  
	            }  
	        }  
	        if (num.length>0) {  
	            for (int i = 0; i < num.length; i++) {  
	                no+=num[i];  
	            }  
	        }  
			return "RV-"+no;
	}	
	
	
	public static long genId(String key){
		IdCreator idCreator  = IdCreatorFactory.getTimeIdCreator(1);
		try {
			return idCreator.nextId(key);
		} catch (CreateIdException e) {
			return System.currentTimeMillis();
		}
		
	}

}
