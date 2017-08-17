package com.allinpay.bigdata.util.yotu.commonutil;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.allinpay.bigdata.util.commonutil.CommonParams;
import com.oss.sdk.OSSClient;

@Slf4j
@Component
public class FileUploadUtil {

	@Autowired
	private CommonParams commonParams;
	
	@Async
	public void uploadFile(File file, String fileName){
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(commonParams.getUppoint(), commonParams.getAccessKey(),commonParams.getSecretKey());
		//保存到文件服务器
		ossClient.putObject(commonParams.getStorageuser(),fileName, file);
		// 关闭client
		ossClient.shutdown();
		log.info("保存{}到文件服务器",fileName);
	}
	
}
