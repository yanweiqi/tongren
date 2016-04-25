package com.ginkgocap.tongren.organization.user.model;

import java.io.Serializable;

import com.ginkgocap.ywxt.user.model.UserConfig;

public class GinTongUser implements Serializable {
    private static final long serialVersionUID = -3200471165235804436L;
    private long id; // id
    private long uid; // uid
    private Integer test =1;//是否为测试用户 0 正常用户 1测试用户
    private Integer status =1;//1：正常；0：锁定；-1：注销

    //private String activationCode ="";//激活验证码
    private String nameIndex="";//简拼音
    private String nameIndexAll="";//全拼音
    private int type=1;//注册类型 ，个人："1"，机构："2" 推荐机构 3
    //基本信息
    private String userName=""; // 用户名
  
    //private String password=""; // 密码

    //private String salt=""; //用户注册时由系统自动为用户产生
    
    private String name="";//真实姓名
    private Integer sex=1;//性别 1男  2 女
    private boolean country=false; //国家
    private String province="";//省份
    private String city="";//城市
    private String county="";//县
    //联系方式
    private String email=""; // 邮箱
    private String mobile=""; // 手机
    private String companyName="";//单位名称
    private String companyJob="";//职务
    
    private String ctime;//注册日期
    private String ipRegistered="";//注册IP地址
    private String remark="";//备注
    private Integer isOnline=0;//是否在线  1：在线  0：未在线
    private String picPath="";//头像地址
    private long remainPoints=0; // 用户剩余积分
    private String lastLoginTime;//最后登录时间
    private String level="";//用户级别
   
    private boolean virtual=false;//虚拟用户，是否是公司   0否  1是
    private long corpId=0;//公司id  业务系统中的公司id
    private long remainMoney=0; // 用户剩余金额
  
    private UserConfig userConfig;//设置权限
    private int isOneLogin=0;
    private int isFirstSet=0; //是否已提交了完善提示 0：未完善 1：已完善
    private int isReceiveEmail = 2;// 是否接收email推送消息  1 接收  2. 不接收
    private int shieldStatus = 0;//屏蔽状态 0 未屏蔽 1 已屏蔽 2已屏蔽但尚未生效
    private String nameFirst="";// 用户首字母  比如 “张三”对应的就是“z”
    private String regFrom;//注册时客户端类型  gintongweb、wegintongweb、gintongapp, gintongadmin等
    private String peopleId; //人脉id  ,客户id
    private int recommendMark = 0;//是否推荐用户标示 0:默认未推荐 1：已推荐
	private int isSecretary; // 是否为小秘书
	
	private String shortName;//组织简称（用户昵称）
	private String orgType;//组织类型 1.金融机构 2一般企业 3.政府组织 4.中介机构 5.专业媒体 6.期刊报纸 7.研究机构 8.电视广播 9.互联网媒体,10.通用类型
	private int auth = -1;//-1 未进行认证 0认证进行中 1认证失败 2认证成功
	private String industry;//组织行业 多个行业逗号隔开
	private String industryIds;//行业id，多个行业逗号隔开
	private String intro;//组织简介
	private String isListing;// 是否上市 是 否
	private String stkcd;//'证券代码',
	
	private String careIndustryIds;//感兴趣的行业id，多个行业逗号隔开
	private String careIndustryNames;//感兴趣的行业名称  多个行业逗号隔开
	private Integer userStatus;//用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息
	private String mobileAreaCode;//手机号前缀，国家区号
	
	private String nemail; //电子邮件
	private String qqlogin;//qq登陆
	private String sinalogin;//新浪登陆
	private String phone;//电话
	

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIsListing() {
		return isListing;
	}

	public void setIsListing(String isListing) {
		this.isListing = isListing;
	}

	public String getStkcd() {
		return stkcd;
	}

	public void setStkcd(String stkcd) {
		this.stkcd = stkcd;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public int getRecommendMark() {
		return recommendMark;
	}

	public void setRecommendMark(int recommendMark) {
		this.recommendMark = recommendMark;
	}
	
    public int getIsSecretary() {
		return isSecretary;
	}

	public void setIsSecretary(int isSecretary) {
		this.isSecretary = isSecretary;
	}

	public long getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(long remainMoney) {
        this.remainMoney = remainMoney;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

//    public String getActivationCode() {
//        return activationCode;
//    }
//
//    public void setActivationCode(String activationCode) {
//        this.activationCode = activationCode;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public boolean isCountry() {
        return country;
    }

    public void setCountry(boolean country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getCompanyJob() {
        return companyJob;
    }

    public void setCompanyJob(String companyJob) {
        this.companyJob = companyJob;
    }


    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getIpRegistered() {
        return ipRegistered;
    }

    public void setIpRegistered(String ipRegistered) {
        this.ipRegistered = ipRegistered;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public long getRemainPoints() {
        return remainPoints;
    }

    public void setRemainPoints(long remainPoints) {
        this.remainPoints = remainPoints;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }


	
	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public long getCorpId() {
        return corpId;
    }

    public void setCorpId(long corpId) {
        this.corpId = corpId;
    }

	public UserConfig getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(UserConfig userConfig) {
		this.userConfig = userConfig;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getIsOneLogin() {
		return isOneLogin;
	}

	public void setIsOneLogin(int isOneLogin) {
		this.isOneLogin = isOneLogin;
	}

	public int getIsFirstSet() {
		return isFirstSet;
	}

	public void setIsFirstSet(int isFirstSet) {
		this.isFirstSet = isFirstSet;
	}

	public int getIsReceiveEmail() {
            return isReceiveEmail;	
	}

	public void setIsReceiveEmail(int email) {
            this.isReceiveEmail = email;	
	}

	public int getShieldStatus() {
		return shieldStatus;
	}

	public void setShieldStatus(int shieldStatus) {
		this.shieldStatus = shieldStatus;
	}

    public String getNameIndex() {
        return nameIndex;
    }

    public String getNameIndexAll() {
        return nameIndexAll;
    }

    public void setNameIndex(String nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setNameIndexAll(String nameIndexAll) {
        this.nameIndexAll = nameIndexAll;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

    public String getRegFrom() {
        return regFrom;
    }

    public void setRegFrom(String regFrom) {
        this.regFrom = regFrom;
    }

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustryIds() {
		return industryIds;
	}

	public void setIndustryIds(String industryIds) {
		this.industryIds = industryIds;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCareIndustryIds() {
		return careIndustryIds;
	}

	public void setCareIndustryIds(String careIndustryIds) {
		this.careIndustryIds = careIndustryIds;
	}

	public String getCareIndustryNames() {
		return careIndustryNames;
	}
	public void setCareIndustryNames(String careIndustryNames) {
		this.careIndustryNames = careIndustryNames;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getMobileAreaCode() {
		return mobileAreaCode;
	}

	public void setMobileAreaCode(String mobileAreaCode) {
		this.mobileAreaCode = mobileAreaCode;
	}

	public String getNemail() {
		return nemail;
	}

	public void setNemail(String nemail) {
		this.nemail = nemail;
	}

	public String getQqlogin() {
		return qqlogin;
	}

	public void setQqlogin(String qqlogin) {
		this.qqlogin = qqlogin;
	}

	public String getSinalogin() {
		return sinalogin;
	}

	public void setSinalogin(String sinalogin) {
		this.sinalogin = sinalogin;
	}

}
