package com.ginkgocap.tongren.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.ywxt.cache.Cache;

/**
 * 定时器调用 失败的消息重新 发送
 * 
 * @author hanxifa
 * 
 */
@Controller
@RequestMapping(value = "/jmssend")
public class JmsSendController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(JmsSendController.class);

	@Autowired
	private JmsSendService jmsSendService;

	@Autowired
	private Cache cache;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/resendFailedRecord.do")
	public void resendFailedRecord(HttpServletRequest request, HttpServletResponse response) {
		if (isAllowUpdate()) {
			logger.info("begin resendFailedRecord ");
			jmsSendService.resendFailedRecord();
			logger.info("end resendFailedRecord ");
		} else {
			logger.info("resendFailedRecord has been cancel!");
		}
	}

	private boolean isAllowUpdate() {
		String cacheCey = RedisKeyUtils.getSessionIdKey("last_resend_time");
		Object val = cache.getByRedis(cacheCey);
		int mins = 3;// 默认5分钟
		if (val != null) {
			Long preTime = (Long) val;
			if (System.currentTimeMillis() - preTime >= mins * 60 * 1000) {
				cache.setByRedis(cacheCey, new Long(System.currentTimeMillis()), 24 * 60 * 60);
				return true;
			} else {
				return false;
			}
		} else {
			cache.setByRedis(cacheCey, new Long(System.currentTimeMillis()), 24 * 60 * 60);
			return true;
		}
	}
}
