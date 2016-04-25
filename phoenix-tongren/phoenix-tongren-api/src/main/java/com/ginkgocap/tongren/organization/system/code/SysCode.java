/**
 * 
 */
package com.ginkgocap.tongren.organization.system.code;

/**
 * 描述：调用服务接口后返回的操作码 和 信息
 * @author liny
 *
 */
public enum SysCode {

	// 成功代码：000000
    // 系统代码：200000
    // 业务逻辑代码：
    //      权限：100100
    //      项目：100200
	//		组织：100300
	//		消息：100400
	//		上传：100500
	//      大数据接口:100600
	//  	资源:100700
	
	
	SUCCESS("000000", "操作成功"),
	ERROR_CODE("-1", "执行失败"),
    SYS_ERR("111111", "操作错误"),
    NO_ERR("222222", "系统未知错误"),
	PARAM_IS_ERROR("000004", "参数错误"),
	SECURITY_ERR("100100","权限认证失败"),
	USER_NOLOGIN("100101","用户没有登录"),
	BIGDATA_EMPTY("100600","没有数据返回"),
	/*上传部分begin*/
    UPLOAD_IMG_IS_ERR("100500", "上传图片失败"),
    UPLOAD_FILE_IS_ERR("100501","上传文件失败"),
    UPLOAD_FILE_IS_EMPTY("100502","没有文件要上传,请选择上传文件"),
    TOO_MANY_FILES("100503","附件最多传10个，请确认后再上传"),
    TOO_BIG_FILES("100504","文件太大请重新上传"),
    TASKID_LIST_ERR("100505","保存文件失败，请选择要提交的文件"),
    TOO_LENGTH("100506","文件标题太长不能超过50个字，请重新上传"),
	/*上传部分end*/
    /*资源文件begin*/
    RESOURCE_LIST_EMPTY("100700","获得资源文件列表为空"),
    RESOURCE_DELETE_ERR("100701","删除资源文件失败"),
    /*资源文件end*/
    /*关键词是否合法begin*/
	KEYWORK_TRUE("100180","关键词合法"),
	KEYWORK_FALSE("100181","关键词不合法"),
	/*关键词是否合法end*/
	/*消息部分begin*/
    MESSAGE_ERROR("100400", "消息发送失败"),
    MESSAGE_SUCCESS("100401","消息发送成功"),
    MESSAGE_CONTENT_ERR("100402","没有消息内容可发送"),
    MESSAGE_LIST_NULL("100403","消息列表为空"),
    MESSAGE_LIST_EMPTY_REEOR("100404","没有消息可清空"),
    MESSAGE_LIST_SEARCH_EMPTY("100405","没有搜索到此关键字的消息"),
    MESSAGE_OPERATE_APPLICATION_MESSAGE_FAIL("100406","操作消息是否通过或忽悠失败"),
    MESSAGE_PROCESSING_ERR("100407","操作消息失败，请稍后处理"),
    MESSAGE_PROCESSING_ERR_TYPE("100408","此消息不能被操作"),
    MESSAGE_SEND_USER_ERR("100409","没有选择要发送邀请的人"),
    MESSAGE_SEND_USER_NULL("100410","没有选择要发送的消息接收人"),
	/*消息部分end*/
	/*审核部分begin */
	REVIEW_FALSE("100501","审核名称已存在,请尝试用新的名称命名"),
	/*审核部分end */
	APPLICATION_MEMBER_LIST_NULL("100310","列表为空"),
	/*项目承接部分begin */
	UNDERTAKE_PROJECT_ERR("100600","承接项目失败"),
	UNDERTAKE_PROJECT_LIST_NULL("100601","我承接的项目列表为空"),
	PROJECT_OBJ_NULL("100602","没有查询到相关项目详情"),
	PROJECT_OBJ_UNDERTAKEN("100606","项目已经被承接"),
	EXTENSION_PROJECT_ERROR("100603","延期项目失败"),
	PROJECT_OPERATION_ERROR("100604","项目操作失败"),
	EXTENSION_PROJECT_ERROR_SUBMIT("100605","已经提交过延期申请"),
	UNFINISHED_PROJECT_ERROR("100607","存在未完成的项目"),
	UNFINISHED_TASK_ERROR("100608","存在未完成的任务"),
	UNFINISHED_REVIEW_ERROR("100609","存在未审批的流程"),
	REVIEWUSER_DOUBLE__ERROR("100700","不能存在重复审核人"),
	REVIEWGENRE_DOUBLE__ERROR("100701","不能存在重复审核类型"),
	PROJECT_STATUS_NOTONE("100702","项目状态非发布成功"),
	DOCUMENT_ALREADY_EXISTS("100801","输入的目录名已经存在");
	/*项目承接部分end */
	 private SysCode(String code, String message) {
	        this.code = code;
	        this.message = message;
	    }
	    
	    private String code;
	    private String message;
	    
	    public String getCode() {
	        return code;
	    }
	    
	    public void setCode(String code) {
	        this.code = code;
	    }
	    
	    public String getMessage() {
	        return message;
	    }
	    
	    public void setMessage(String message) {
	        this.message = message;
	    }
  
}
