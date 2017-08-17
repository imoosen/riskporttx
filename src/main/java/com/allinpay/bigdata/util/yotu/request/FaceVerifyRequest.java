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
 * @author serenazhao 人脸验证请求
 */
public class FaceVerifyRequest extends AbstractBaseRequest {      
        //是否是url
        private boolean isUrl = true;
        
    	// url
	private String url = "";
        
	// 图片内容列表
        private String image = "";
        
        //要查询的个人Id
         private String personId = "";
              
        public FaceVerifyRequest(String bucketName, String personId, String url) {
		super(bucketName);
		this.isUrl = true;
                this.personId = personId;
                this.url = url;
	}
 
        public FaceVerifyRequest(String bucketName, String personId, String name, String image) {
		super(bucketName);
		this.isUrl = false;
                this.personId = personId;
                this.image = image;             
	}
        
        public boolean isUrl() {
            return isUrl;
        }
        
        public String getUrl() {
            return url;
        }
        
        public String getPersonId() {
            return personId;
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
                if(isUrl){
                    CommonParamCheckUtils.AssertNotNull("url", url);
                }else{
                    CommonParamCheckUtils.AssertNotNull("image content", image);
                }
	}
}
