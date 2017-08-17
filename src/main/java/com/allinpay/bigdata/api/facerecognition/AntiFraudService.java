package com.allinpay.bigdata.api.facerecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.allinpay.bigdata.util.commonutil.CommonParams;
import com.allinpay.bigdata.util.commonutil.DateUtil;
import com.allinpay.bigdata.util.commonutil.ErrorCode;
import com.allinpay.bigdata.util.commonutil.StringUtils;
import com.allinpay.bigdata.util.rest.BaseRestService;
import com.allinpay.bigdata.util.yotu.Base64Util;
import com.allinpay.bigdata.util.yotu.BspAPIUtil;
import com.allinpay.bigdata.util.yotu.ImageClient;
import com.allinpay.bigdata.util.yotu.commonutil.CommonFileUtils;
import com.allinpay.bigdata.util.yotu.commonutil.FileUploadUtil;
import com.allinpay.bigdata.util.yotu.http.ApiRequest;
import com.allinpay.bigdata.util.yotu.http.ApiResponse;
import com.allinpay.bigdata.util.yotu.request.FaceIdCardLiveDetectFourRequest;

/**
 * 
 * 功能：腾讯反欺诈接口
 * 
 * @author Yuanxy
 * 
 * 创建日期：2017-7-10上午10:12:40
 */
@Slf4j
@Component
public class AntiFraudService extends BaseRestService {
	
	@Autowired
	private CommonParams commonParams;

	public JSONObject query(String idNumber, String phoneNumber, String name,
			String bankCardNumber) {
		JSONObject res = new JSONObject();
		//姓名不能为空
		if (StringUtils.isEmpty(name)) {
			res.put("code", ErrorCode.NAME_NULL_CODE_2002);
			res.put("msg", getMessage(ErrorCode.NAME_NULL_CODE_2002));
			return res;
		}
		//身份证不能为空
		if (StringUtils.isEmpty(idNumber)) {
			res.put("code", ErrorCode.IDCARD_NULL_CODE_2001);
			res.put("msg", getMessage(ErrorCode.IDCARD_NULL_CODE_2001));
			return res;
		}
		//手机号不能为空
		if (StringUtils.isEmpty(phoneNumber)) {
			res.put("code", ErrorCode.PHONE_NULL_CODE_2003);
			res.put("msg", getMessage(ErrorCode.PHONE_NULL_CODE_2003));
			return res;
		}

		Map<String, String> args = new TreeMap<String, String>();

        args.put("idNumber", idNumber);
        args.put("phoneNumber", commonParams.getPhonehead()+phoneNumber);
        if(!StringUtils.isEmpty(bankCardNumber)){
        	args.put("bankCardNumber", bankCardNumber);
        }
        args.put("name", name);
		try {
			String url = BspAPIUtil.makeURL("GET", "AntiFraud", "sh", commonParams.getYotosecretid(),
					commonParams.getYotosecretkey(), args, "utf-8");
			ApiResponse apiRes = ApiRequest.sendGet(url, "");
			Object resStr = apiRes.getBody();
			log.info("腾讯反欺诈接口返回结果:{}", resStr);
			res.put("data", resStr);
			if(resStr == null){
				String msg = getMessage(ErrorCode.FAIL_CODE_0001);
				res.put("code", ErrorCode.FAIL_CODE_0001);
				res.put("msg", msg);
			}else{
				String msg = getMessage(ErrorCode.SUCCESS_CODE_0000);
				res.put("code", ErrorCode.SUCCESS_CODE_0000);
				res.put("msg", msg);
				JSONObject response = JSONObject.parseObject(resStr.toString());
				String code = response.getString("code");
				if(!"0".equals(code)){
					//返回错误说明
					String codeDesc = response.getString("codeDesc");
					String message = response.getString("message");
					res.put("errorreason", ErrorCode.errorMsg(code,codeDesc+message));
				}
			}
			System.out.println(apiRes.getBody());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			log.info("反欺诈接口异常：InvalidKeyException", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.info("反欺诈接口异常：NoSuchAlgorithmException", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.info("反欺诈接口异常：UnsupportedEncodingException", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		}
		return res;
	}

}