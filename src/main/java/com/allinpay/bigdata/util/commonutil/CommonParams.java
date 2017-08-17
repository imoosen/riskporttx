package com.allinpay.bigdata.util.commonutil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="commonparams") //接收application.yml中的commonparams下面的属性 
public class CommonParams {

	//文件服务器配置
	private String storageuser;
	private String uppoint;
	private String downpoint;
	private String accessKey;
	private String secretKey;
	
	//腾讯优图配置
	private String appid;
	private String bucket;
	private String yotosecretid;
	private String yotosecretkey;
	
	//天御反欺诈配置
	private String phonehead;//手机号码头
	public String getStorageuser() {
		return storageuser;
	}
	public void setStorageuser(String storageuser) {
		this.storageuser = storageuser;
	}
	public String getUppoint() {
		return uppoint;
	}
	public void setUppoint(String uppoint) {
		this.uppoint = uppoint;
	}
	public String getDownpoint() {
		return downpoint;
	}
	public void setDownpoint(String downpoint) {
		this.downpoint = downpoint;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getYotosecretid() {
		return yotosecretid;
	}
	public void setYotosecretid(String yotosecretid) {
		this.yotosecretid = yotosecretid;
	}
	public String getYotosecretkey() {
		return yotosecretkey;
	}
	public void setYotosecretkey(String yotosecretkey) {
		this.yotosecretkey = yotosecretkey;
	}
	public String getPhonehead() {
		return phonehead;
	}
	public void setPhonehead(String phonehead) {
		this.phonehead = phonehead;
	}
	
}
