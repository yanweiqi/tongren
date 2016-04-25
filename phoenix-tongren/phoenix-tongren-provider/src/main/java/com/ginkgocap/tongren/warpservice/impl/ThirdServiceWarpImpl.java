package com.ginkgocap.tongren.warpservice.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.warpservice.ThirdServiceWarp;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;

/**
 * 第三方service包转实现
 * @author hanxifa
 *
 */
@Service("thirdServiceWarp")
public class ThirdServiceWarpImpl implements ThirdServiceWarp {
	
	private final Logger logger  = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileIndexService fileIndexService;
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> taskCacheMap = Collections.synchronizedMap(new LRUMap(5000));
	/**
	 * 根据taskid查询资源的url
	 * 查询不到，则返回null
	 */
	@Override
	public String getUrlByTaskId(String taskId) {
		String path = null;
		if (taskId != null) {

			path = taskCacheMap.get(taskId);
			if (path == null) {
				List<FileIndex> list = fileIndexService.selectByTaskId(taskId, "1");
				if (list != null && list.size() > 0) {
					FileIndex s = list.get(0);
					path = s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath() + "/" + s.getFileTitle();
				}
				if (path == null) {
					taskCacheMap.put(taskId, "");
				} else {
					taskCacheMap.put(taskId, path);
				}
			}else{
				path=path.length()==0?null:path;
			}
		}
		logger.info("taskid " + taskId + " --> " + path);
		return path;
	}

}
