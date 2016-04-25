package com.ginkgocap.tongren.project.manage.model;

public enum ProjectStatus{
	
    Project_Create_Draft(0,"草稿",10),                          //项目草稿，表示未发布
    Project_Create_Official(1,"正式",11),                       //项目正式
    Project_Publish_Fail(0,"发布失败",20),						//项目发布失败
    Project_Publish_Success(1,"发布成功",21),    				//项目发布
    Project_Publish_Discussed(2,"屏蔽发布",22),  				//项目屏蔽、项目删除
    Project_Publish_Expired(3,"发布已过期",23),  				//项目过期
    Project_Publish_Complect(4,"项目完成",24),   				//项目完成
    Project_Publish_Abort(5,"项目放弃",25),      				//项目放弃
    Project_Publish_Delay(8,"项目延期",26),      				//项目延期
    Project_Publish_Expired_Unfinished(7,"项目过期未完成",27),	//项目过期未完成
    Project_Publish_Inprogress(8,"项目进行中",28),               //项目进行中
    Project_Publish_Resumit(1,"重新发布项目",29),                //重新发布项目
    Project_Apply_Fail(0,"申请失败",30),							//项目过期未完成
    Project_Apply_CheckPending(1,"待审核",31),					//项目申请待审核
    Project_Apply_Success(2,"申请成功",32)                      //项目申请成功，表示已经承接
    ;
	
	public static String getValueBykey(int key){
		String value = null;
		for(ProjectStatus ps: ProjectStatus.values()){
			if(ps.getKey() == key){
				value = ps.getValue();
			}
		}
		return value;
	}
	
	public static Integer getMqType(int key){
		Integer type = null;
		for(ProjectStatus ps: ProjectStatus.values()){
			if(ps.getKey() == key){
				type = ps.getType();
			}
		}
		return type;
	}
	
	public static ProjectStatus getProjectStatusByKey(int key){
		ProjectStatus projectStatus = null;
		for(ProjectStatus ps: ProjectStatus.values()){
			if(ps.getKey() == key){
				projectStatus = ps;
			}
		}
		return projectStatus;
	}
	
	private ProjectStatus(int key, String value,int type) {
		this.key = key;
		this.value = value;
		this.type = type;
	}
    
    private int key;
    private String value;
    private int type;
    
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
