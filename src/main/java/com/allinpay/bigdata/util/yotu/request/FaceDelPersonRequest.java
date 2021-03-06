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
 * @author serenazhao 删除一个Person
 */
public class FaceDelPersonRequest extends AbstractBaseRequest {
    
    	//个体Id
	private String personId = "";
              
        public FaceDelPersonRequest(String bucketName, String personId) {
		super(bucketName);
		this.personId = personId;
	}

        public String getPersonId() {
            return personId;
        }

	@Override
	public void check_param() throws ParamException {
		super.check_param();
                CommonParamCheckUtils.AssertNotNull("personId", personId);
	}
}
