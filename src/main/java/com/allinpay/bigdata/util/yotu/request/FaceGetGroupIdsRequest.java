/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinpay.bigdata.util.yotu.request;

import com.allinpay.bigdata.util.yotu.exception.ParamException;

/**
 *
 * @author serenazhao 获取组列表
 */
public class FaceGetGroupIdsRequest extends AbstractBaseRequest {
        
        public FaceGetGroupIdsRequest(String bucketName) {
		super(bucketName);

	}

	@Override
	public void check_param() throws ParamException {
		super.check_param();
	}
}
