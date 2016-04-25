package com.ginkgocap.tongren.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class JonsUtils {
	 public static void main(String[] args) {
		 
		JSONArray aaa= JSON.parseArray("[{\"tagid\":\"1112\",\"tagName\":\"master\"},{\"tagid\":\"1113\",\"tagName\":\"mobile\"}]");
		for(int i=0;i<aaa.size();i++){
			JSONObject jb= aaa.getJSONObject(i);
			System.out.println(jb.getString("tagid"));
			System.out.println(jb.getString("tagName"));
		}
	}
}


