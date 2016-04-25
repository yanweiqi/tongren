package com.ginkgocap.tongren.project.manage.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.project.manage.model.Aborted;
import com.ginkgocap.tongren.project.manage.service.AbortedService;

@Service("abortedService")
public class AbortedServiceImpl extends AbstractCommonService<Aborted> implements AbortedService{
	
	private static final Logger logger = LoggerFactory.getLogger(AbortedServiceImpl.class);

	@Override
	protected Class<Aborted> getEntity() {
		return Aborted.class;
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
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.AbortedService#create(long, long, java.lang.String, long, long, long, long)
	 */
	@Override
	public Aborted create(long projectId, long projectUndertakenId,
			String reason, long recipientId, long undertakenOrganizationId,
			long operationId, long createOrganizationId) throws Exception {
		Aborted returnAborted = null;
		try {
			Aborted aborted = new Aborted();
			aborted.setCreateOrganizationId(createOrganizationId);
			aborted.setOperationId(operationId);
			aborted.setProjectId(projectId);
			aborted.setProjectUndertakenId(projectUndertakenId);
			aborted.setReason(reason);
			aborted.setRecipientId(recipientId);
			aborted.setUndertakenOrganizationId(undertakenOrganizationId);
			returnAborted = save(aborted);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return returnAborted;
	}

}
