package com.ginkgocap.tongren.common.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.JmsMsgBean;
import com.ginkgocap.tongren.common.model.MqmsgSendrecord;
import com.ginkgocap.tongren.common.utils.GenerateUtil;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.api.enums.TopicType;
import com.gintong.rocketmq.api.model.RocketSendResult;
import com.gintong.rocketmq.api.utils.FlagTypeUtils;

public class RocketmqSendServiceImpl extends AbstractCommonService<MqmsgSendrecord> implements JmsSendService{
	private  static final Logger logger = LoggerFactory.getLogger(RocketmqSendServiceImpl.class);
	
	private String nameServerAddr;
	
	private String producerGroup;
	
	private String instanceName;
	
	
	
	@Autowired
	private DefaultMessageService defaultMessageService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CodeRegionService codeRegionService;
	
	@Autowired
	private CodeService codeService;
	
	private String projectTopic="project_topic";
	
	
	/**
	 * 
	 * @return 1 发送成功 2 发送失败 3消息为空 4 不需要发送
	 */
	@Override
	public String sendJmsMsg(JmsMsgBean jmsMsgBean) {
		String msgStr = null;
		
		try{
			if (jmsMsgBean == null || jmsMsgBean.getContent() == null) {
				logger.info("jmsMsgBean is null " + jmsMsgBean);
				return "3";
			}
			if(CommonConstants.Publish.equals(jmsMsgBean.getOpType())){
				if(21==jmsMsgBean.getOperator()||22==jmsMsgBean.getOperator()){
					Publish publish=(Publish) jmsMsgBean.getContent();
					String msgId= GenerateUtil.genId(projectTopic)+"";
					msgStr=convertToProjectMsgStr(publish.getProjectId(),msgId);
					if(msgStr!=null){
						String flag=null;
						if(21==jmsMsgBean.getOperator()){
							flag=FlagTypeUtils.createDemandFlag();
						}else if(22==jmsMsgBean.getOperator()){
							flag=FlagTypeUtils.deleteDemandFlag();
						}
						String status=sendMsgByJms(projectTopic,flag,msgStr);
						saveSendRecord(msgStr,msgId,Integer.parseInt(status),projectTopic,flag);
						return status;
					}else{
						return "3";
					}
				}else{
					logger.info("this operator is ignored "+ jmsMsgBean.getOperator());
					return "4";
				}
				
			}else{
				logger.info("this optype is ignored "+jmsMsgBean.getOpType());
				return "4";
			}
			
		}catch(Exception e){
			logger.error("send jms msg failed! id:"+jmsMsgBean.getId()+",content:"+jmsMsgBean.getContent(),e);
			return "2" ;
		}
	}
	
	public String convertToProjectMsgStr(long projectId,String msgId){
		Project project=projectService.getEntityById(projectId);
		if(project!=null){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id",msgId);
			map.put("name", project.getName());
			map.put("introduction", project.getIntroduction());
			map.put("regions", getRegionInfo(Long.parseLong(project.getArea())));//地区
			map.put("industrys", getIndustryInfo(Long.parseLong(project.getIndustry())));//行业
			map.put("createTime", project.getCreateTime().getTime()+"");
			map.put("projectId",project.getId()+"");
			map.put("source", "2");//数据（来源）1 金铜网，2 桐人
			map.put("createId",project.getCreaterId()+"");
			//项目类型，金桐网的项目类型有投资，融资等，8-11对应金铜网的项目id
			Map<String, String> typeMap=new HashMap<String, String>();
			typeMap.put("id", "8-11");
			typeMap.put("name", "项目");
			map.put("type", new Object[]{typeMap});
			//设置权限，由于金桐网的项目有权限，桐人这边默认是大乐权限，所有人都能看
			Map<String, Object> permIdsMap=new HashMap<String, Object>();
			permIdsMap.put("dule", "false");
			
			Map<String, String> dales=new HashMap<String, String>();
			dales.put("id", "-1");
			dales.put("name", "全部");
			permIdsMap.put("dales",new Object[]{dales});
			
			permIdsMap.put("zhongles", new Object[]{});
			permIdsMap.put("xiaoles", new Object[]{});
			
			// "{\"dule\":\"false\",\"dales\":[{\"id\":\"-1\",\"name\":\"全部\"}],\"zhongles\":[],\"xiaoles\":[]}"
			map.put("permIds",permIdsMap);
			return JSON.toJSONString(map);
		}else{
			logger.warn("not found project by id "+projectId);
			return null;
		}
	}
	private List< Map<String, String>> getIndustryInfo(long ...ids){
		List< Map<String, String>> rslist =new ArrayList<Map<String,String>>();
		for(long id:ids){
			Code code=codeService.selectByPrimarKey(id);
			Map<String, String> industryMap=new HashMap<String, String>();
			if(code!=null){
				Code subCode=code;
				List<String> idList=new ArrayList<String>();
				idList.add(subCode.getId()+"");
				while("0".equals(subCode.getType())==false){
					subCode=codeService.selectByPrimarKey(Integer.parseInt(subCode.getType()));
					idList.add(subCode.getId()+"");
				}
				StringBuilder sb=new StringBuilder();
				for(int i=idList.size()-1;i>=0;i--){
					sb.append(idList.get(i));
					if(i>0){
						sb.append("-");
					}
				}
				industryMap.put("id",sb.toString());
				industryMap.put("name",code.getName() );
				rslist.add(industryMap);
			}else{
				logger.warn("not found code by "+id);
			}
		}
		return rslist;
	}
	/**
	 * 根据id获取区域信息
	 * 包括 省市县信息
	 * @param id
	 * @return
	 */
	private List< Map<String, String>> getRegionInfo(long ...ids){
		List< Map<String, String>> rslist =new ArrayList<Map<String,String>>();
		for(long id:ids){
			List<CodeRegion> list=new ArrayList<CodeRegion>();
			CodeRegion codeRegion= codeRegionService.selectByPrimaryKey(id);
			if(codeRegion==null){
				logger.warn("not found region info by id "+id);
				return null;
			}
			list.add(codeRegion);
			//只查询到省，不包括中国 ParentId=1 为中国
			while(codeRegion.getParentId()!=1&&list.size()<3){
				codeRegion= codeRegionService.selectByPrimaryKey(codeRegion.getParentId());
				list.add(codeRegion);
			}
			Map<String, String> areaMap=new HashMap<String, String>();
			StringBuilder regionIds=new StringBuilder();
			for(int i=list.size()-1;i>=0;i--){
				regionIds.append(list.get(i).getId());
				if(i>0){
					regionIds.append('-');
				}
			}
			areaMap.put("id", regionIds.toString());
			areaMap.put("name", list.get(0).getCname());
			rslist.add(areaMap);
		}
		return rslist;
	}
	
	private String sendMsgByJms(String topic,String tags,String msgStr){
		try {
			/*bscode 业务处理代号,0成功 -1失败，-2请求参数错误，未处理。默认值是0 */
			if(projectTopic.equals(topic)){
				RocketSendResult result=defaultMessageService.sendMessage(TopicType.DEMAND_TOPIC,tags, msgStr);
				logger.info("send msgStr "+msgStr+",result is "+result);
				return result.getBscode()+"";
			}
			return "-2";
		} catch (Exception e) {
			logger.error("发送 " + msgStr + "失败", e);
			return "2";
		}
		
	}

	/**
	 * 从数据库中查询发送失败的记录，重新发送
	 */
	public void resendFailedRecord(){
		String sql="MqmsgSendrecord_list_sendStatus";
		List<Long> ids=getKeysByParams(sql, new Object[]{"0"});
		List<MqmsgSendrecord> mlist=new ArrayList<MqmsgSendrecord>();
		for(Long id:ids){
			mlist.add(getEntityById(id));
		}
		Collections.sort(mlist,new Comparator<MqmsgSendrecord>() {
			//按时间倒序排序
			@Override
			public int compare(MqmsgSendrecord o1, MqmsgSendrecord o2) {
				return o2.getCreateTime().compareTo(o1.getCreateTime());
			}
		});
		logger.info("resend count is "+mlist.size());
		if(mlist.size()>0){
			try
			{
				for(MqmsgSendrecord ms:mlist){
					if(ms.getSendStatus()==2||ms.getSendStatus()==-1){
						if(ms.getSendCount()>=3){
							logger.info("has send 3 times id:"+ms.getId()+",content"+ms.getContent());
							continue;
						}else{
							logger.info("begin resend  id:"+ms.getId()+",times:"+ms.getSendCount()+",content"+ms.getContent());
						}
						String status=sendMsgByJms(ms.getTopic(),ms.getTags(),ms.getContent());
						logger.info("sendResult is : " + status+ms.getId());
						ms.setLastSendTime(new Timestamp(System.currentTimeMillis()));
						ms.setSendCount(ms.getSendCount()+1);
						ms.setSendStatus(Integer.parseInt(status));
						update(ms);
					}else{
						logger.info("status is cancel id:"+ms.getId()+",status:"+ms.getSendStatus());
					}
				}
			}catch(Exception e){
				logger.error("resend failed!",e);
			}
		}
		
	}
	
	/**
	 * 保存发送记录
	 * @param jmsMsgBean
	 * @param status 0成功1 失败
	 */
	private MqmsgSendrecord saveSendRecord(String msgStr,String msgId,int status,String stopic,String tags){
		MqmsgSendrecord mqmsgSendrecord=new MqmsgSendrecord();
		mqmsgSendrecord.setContent(msgStr);
		mqmsgSendrecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
		mqmsgSendrecord.setLastSendTime(mqmsgSendrecord.getCreateTime());//最后一次发送时间为创建时间
		mqmsgSendrecord.setMsgId(Long.parseLong(msgId));
		mqmsgSendrecord.setSendStatus(status);
		mqmsgSendrecord.setTopic(stopic);
		mqmsgSendrecord.setTags(tags);
		mqmsgSendrecord.setSendCount(1);
		return save(mqmsgSendrecord);
	}

	public String getNameServerAddr() {
		return nameServerAddr;
	}


	public void setNameServerAddr(String nameServerAddr) {
		this.nameServerAddr = nameServerAddr;
	}


	public String getProducerGroup() {
		return producerGroup;
	}


	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}


	public String getInstanceName() {
		return instanceName;
	}


	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}




	@Override
	protected Class<MqmsgSendrecord> getEntity() {
		return MqmsgSendrecord.class;
	}


	@Override
	public Map<String, Object> doWork() {
		return null;
	}


	@Override
	public Map<String, Object> doComplete() {
		return null;
	}


	@Override
	public String doError() {
		return null;
	}


	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

}
