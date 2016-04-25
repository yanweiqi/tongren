package com.ginkgocap.tongren.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页对象
 * @author hanxifa
 *
 * @param <T>
 */
public class Page<T extends Serializable> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String,Object> queryParams=new HashMap<String,Object>();
	
	private int index=1;//第几页，
	private int size=10;//每页的记录数
	
	private int totalCount;//总记录数
	
	private List<T> result;

	public void addParam(String key,Object val){
		queryParams.put(key, val);
	}

	public Object getParam(String key) {
		return queryParams.get(key);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		if(index<1){
			index=1;
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		if(size>50){
			size=50;
		}
		if(size<1){
			size=10;
		}
		
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
	
	public int getStart(){
		return (index-1)*size;
	}
	
	public int getEnd(){
		return getStart()+size;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * 返回总共的页数
	 * @return
	 */
	public int getPageCount(){
		if(totalCount<=0){
			return 0;
		}
		int tc=totalCount/getSize();
		if(totalCount%getSize()>0){
			tc++;
		}
		return tc;
	}
}
