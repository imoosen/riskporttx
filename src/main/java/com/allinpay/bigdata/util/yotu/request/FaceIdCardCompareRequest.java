/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinpay.bigdata.util.yotu.request;

import com.allinpay.bigdata.util.yotu.commonutil.CommonParamCheckUtils;
import com.allinpay.bigdata.util.yotu.exception.ParamException;


/**
 *
 * @author serenazhao 人脸识别请求
 */
public class FaceIdCardCompareRequest extends AbstractBaseRequest {
        //是否是url
        private boolean isUrl = true;
        
    	// url
	private String url = "";
        
	// 图片内容
        private String image = "";
        
        //要查询的身份证号码
         private String idcardNumber = "";
         
         //要查询的身份证名字
         private String idcardName = "";
         
          //相应请求的session标识符
         private String sessionId = "";
              
        public FaceIdCardCompareRequest(String bucketName, String idcardNumber, String idcardName, String url, String sessionId) {
		super(bucketName);
		this.isUrl = true;
                this.idcardNumber = idcardNumber;
                this.idcardName = idcardName;
                this.url = url;
                this.sessionId = sessionId;
	}
 
        public FaceIdCardCompareRequest(String bucketName, String idcardNumber, String idcardName, String name, String image, String sessionId) {
		super(bucketName);
		this.isUrl = false;
                this.idcardNumber = idcardNumber;
                this.idcardName = idcardName;
                this.image = image;
                this.sessionId = sessionId;
	}
        
        public boolean isUrl() {
            return isUrl;
        }
        
        public String getUrl() {
            return url;
        }
        
        public String getIdcardNumber() {
            return idcardNumber;
        }
        
        public String getIdcardName() {
            return idcardName;
        }
        
        public String getSessionId() {
            return sessionId;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

	@Override
	public void check_param() throws ParamException {
		super.check_param();
                CommonParamCheckUtils.AssertNotNull("idcardName", idcardName);
                CommonParamCheckUtils.AssertNotNull("idcardNumber", idcardNumber);
                if(isUrl){
                    CommonParamCheckUtils.AssertNotNull("url", url);
                }else{
                    CommonParamCheckUtils.AssertNotNull("image content", image);
                }
	}
}
