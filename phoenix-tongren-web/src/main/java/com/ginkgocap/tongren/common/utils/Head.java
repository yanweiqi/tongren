/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

/**
 * 前后端JSON对象公用Head部分
 * @author liny
 *
 */
public class Head {
	 /**
     * serialNumber : <交易序列号>
     */
    private String serialNumber;

    /**
     * method : <交易方法>
     */
    private String method;

    /**
     * version : <交易方法版本号>
     */
    private String version;

    /**
     * terminalstate : <终端类型>
     * 
     * 0-安卓 10-用户版appstore(个人) 11-用户版inhouse(企业) 12-商户版appstore(个人)
     * 13-商户版inhouse(企业)
     */

    private int terminalstate;

    /**
     * sysVersion : <终端系统版本号>
     */
    private String sysVersion;

    /**
     * imei : <设备IME码>
     */
    private String imei;


    /**
     * appVersion : <APP版本号>
     */
    private String appVersion;

    /**
     * appSys : <IOS-1 Andriod-2 WEB-3>
     */
    private int appSys;
    

    public Head() {
    }

    public Head(String method, String serialNumber, String version) {
        this.method = method;
        this.serialNumber = serialNumber;
        this.version = version;
    }

    public Head(String method, String serialNumber, String version, int terminalstate, String sysVersion, String imei) {
        this.method = method;
        this.serialNumber = serialNumber;
        this.version = version;
        this.terminalstate = terminalstate;
        this.sysVersion = sysVersion;
        this.imei = imei;
    }
    
    public Head(String method, String serialNumber, String version, int terminalstate, String sysVersion, String imei,
            String appVersion) {
        this.method = method;
        this.serialNumber = serialNumber;
        this.version = version;
        this.terminalstate = terminalstate;
        this.sysVersion = sysVersion;
        this.imei = imei;
        this.appVersion = appVersion;
    }
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "serialNumber:" + serialNumber + ",method:" + method + ",version:" + version + ",terminalstate:"
                + terminalstate + ",sysVersion:" + sysVersion + ",imei:" + imei 
                + ",appVersion:" + appVersion;
    }

    public int getAppSys() {
        return appSys;
    }

    public void setAppSys(int appSys) {
        this.appSys = appSys;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTerminalstate() {
        return terminalstate;
    }

    public void setTerminalstate(int terminalstate) {
        this.terminalstate = terminalstate;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

}
