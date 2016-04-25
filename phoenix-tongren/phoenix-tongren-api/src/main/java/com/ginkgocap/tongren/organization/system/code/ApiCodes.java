package com.ginkgocap.tongren.organization.system.code;
/**
 * @author yanweiqi
 */
public enum ApiCodes{
	
	OutofHandles("12001","Out of Handles","处理越界"),
	Timeout("12002","Timeout","超时"),
	ExtendedError("12003","Extended Error","延伸错误"),
	InternalError("12004","Internal Error","内部错误"),
	InvalidURL("12005","Invalid URL","无效的URL"),
	UnrecognizedScheme("12006","Unrecognized Scheme","未被认可的方案"),
	NameNotResolved("12007","Name Not Resolved","名字没有被解决"),
	ProtocolNotFound("12008","Protocol Not Found","找不到协议"),
	InvalidOption("12009","Invalid Option","无效的选项"),
	BadOptionLength("12010","Bad Option Length","错误选项太长"),
	OptionnotSettable("12011","Option not Settable","选择不可设置"),
	Shutdown("12012","Shutdown","关机"),
	IncorrectUserName("12013","Incorrect User Name","错误的用户名"),
	IncorrectPassword("12014","Incorrect Password","错误的密码"),
	LoginFailure("12015","Login Failure","登陆失败"),
	//InvalidOption("12016","Invalid Option","无效的选项"),
	OperationCancelled("12017","Operation Cancelled","操作被取消"),
	IncorrectHandleType("12018","Incorrect Handle Type","错误的处理类型"),
	InccorectHandleState("12019","Inccorect Handle State","错误的处理状态"),
	NotProxyRequest("12020","Not Proxy Request","没有代理请求"),
	RegistryValueNotFound("12021","Registry Value Not Found","注册值找不到"),
	BadRegistryParameter("12022","Bad Registry Parameter","错误的注册参数"),
	NoDirectAccess("12023","No Direct Access","没有直接存取"),
	NoContent("12024","No Content","没有内容"),
	NoCallback("12025","No Callback","没有回调"),
	RequestPending("12026","Request Pending","未决请求"),
	IncorrectFormat("12027","Incorrect Format","不正确的格式"),
	ItemNotFound("12028","Item Not Found","条款找不到"),
	CannotConnect("12029","Cannot Connect","无法连接"),
	ConnectionAborted("12030","Connection Aborted","连接失败"),
	ConnectionReset("12031","Connection Reset","连接被重置"),
	ForceRetry("12032","Force Retry","强制重试"),
	InvalidProxyRequest("12033","Invalid Proxy Request","无效的代理请求"),
	NeedUI("12034","Need UI","需要UI"),
	NotDefinedinWinInet("12035","Not Defined in WinInet","没有定义在客户端"),
	HandleExists("12036","Handle Exists","处理存在"),
	SeeCertDateInvalid("12037","See Cert Date Invalid","证书日期无效"),
	SeeCertCNInvalid("12038","See Cert CN Invalid","证书号码无效"),
	HTTPtoHTTPSonRedir("12039","HTTP to HTTPS on Redir","HTTPs to HTTP on Redir"),
	HTTPstoHTTPonRedir("12040","HTTPs to HTTP on Redir","HTTPs to HTTP on Redir"),
	MixedSecurity("12041","Mixed Security","混合安全"),
	ChgPostisNonSecure("12042","Chg Post is Non Secure","Chg Post is Non Secure"),
	PostisNonSecure("12043","Post is Non Secure","Post不安全"),
	ClientAuthCertNeeded("12044","Client Auth Cert Needed","需要客户端验证证书"),
	InvalidCACert("12045","Invalid CA (Cert)","错误的CA证书"),
	ClientAuthNotSetup("12046","Client Auth Not Setup","客户端没有安装证书"),
	AsyncThreadFailed("12047","Async Thread Failed","异步线程失败") ,
	RedirectSchemeChanged("12048","Redirect Scheme Changed","重定向改变"),
	DialogPending("12049","Dialog Pending","等待对象框"),
	RetryDialog("12050","Retry Dialog","重试对话框"),
	HttpsHttpSubmitRedir("12052","Https Http Submit Redir","重试提交https、http"),
	InsertCdrom("12053","Insert Cdrom ","Insert Cdrom"),
	FailedDueToSecurityCheck("12171","Failed DueToSecurityCheck","Failed DueToSecurityCheck"),
	RequestParameterError("12172", "ParameterError","参数错误"),
	UserIsNotExist("12173","UserIsNotExist","用户不存在"),
	DepIsExist("12174","DepIsExist","部门已经存在"),
	DepNotSava("12175","DepNotSava","部门不能保存"),
	DepNotUpdate("12176","DepNotUpdate","部门不能更新"),
	DepIsNotExist("12177","DepIsNotExist","部门已经不存在"),
	DepMemberIsNotExist("12178","DepMemberIsNotExist","部门已经不存在"),
	DepMemberIsExist("12179","DepMemberIsExist","部门成员已存在"),
	DepMemberDelFail("12180","DepMemberDelFail","部门成员删除失败"),
	DepMemberGetFail("12181","DepMemberGetFail","获取部门成员失败"),
	OrganizationMemberGetFail("12182","OrganizationMemberGetFail","获组织成员失败"),
	OrganizationMemberDelFail("12183","OrganizationMemberDelFail","删除织成员失败"),
	UserIsNotExistInDep("12184","UserIsNotExistInDep","用户不存在部门中"),
	MemberRoleGetFail("12185","MemberRoleGetFail","成员角色获取失败"),
	UserIsNotExistInMemberRole("12186","UserIsNotExistInMemberRole","用户不存在成员角色"),
	ProjectIsNotExist("12187","ProjectIsNotExist","项目不存在"),
	ProjectHasBeenToUndertake("12188","ProjectHasBeenToUndertake","项目已经被承接"),
	RoleIdIsNotAdmin("12189","RoleIdIsNotAdmin","roleId不是管理员Id"),
	MemberRoleIsNotExist("12190","MemberRoleIsNotExist","成员角色不存在"),
	MemberRoleIsExist("12191","MemberRoleIsExist","成员角色已经存在"),
	ProjectApplyRepeat("12192","ProjectApplyRepeat","不能重复申请"),
	ProjectIsExist("12193","ProjectIsExist,no repeat create","项目已经存在，不能重复创建"),
	PublishIsExist("12194","PublishIsExist,no repeat publish","项目已经存在，不能重复发布"),
	PublishIsNotExist("12195","PublishIsNotExist","项目发布不存在"),
	OrganizationCreaterIsExist("12196","OrganizationCreaterIsExist","组织创建者已经存在"),
	OrganizationMemberIsExist("12197","OrganizationMemberIsExist","组织成员已经存在"),
	OrganizationRoleIsExist("12198","OrganizationRoleIsExist","组织角色已经存在"),
	OrganizationMemberHasBeanOneRoler("12199","OrganizationMemberHasBeanOneRoler","组织成员只能有一个角色"),
	ProjectCreateCycleError("12200","ProjectCreateCycleError","项目结束时间不能小于开始时间"),
	OrganizationRoleDoNotDel("12201","OrganizationRoleDoNotDel,HasMembersUsingTheRole ","不能删除组织角色，有成员正在使用该角色"),
	NoEnoughAuthority("12202","Sorry, You don't have enough authority","对不起，您的权限不够"),
	;
	
	private String code;
	private String message;
	private String description;
	
	private ApiCodes(String code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

}