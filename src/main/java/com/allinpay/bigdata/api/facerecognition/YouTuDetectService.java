package com.allinpay.bigdata.api.facerecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
import com.allinpay.bigdata.util.yotu.ImageClient;
import com.allinpay.bigdata.util.yotu.commonutil.CommonFileUtils;
import com.allinpay.bigdata.util.yotu.commonutil.FileUploadUtil;
import com.allinpay.bigdata.util.yotu.request.FaceIdCardLiveDetectFourRequest;

/**
 * 
 * 功能：腾讯人脸识别
 * 
 * @author Yuanxy
 * 
 * 创建日期：2017-6-14下午7:22:40
 */
@Slf4j
@Component
public class YouTuDetectService extends BaseRestService {
	
	@Autowired
	private FileUploadUtil fileUploadUtil;
	@Autowired
	private CommonParams commonParams;

	public JSONObject Youtu(String idCard, String name, String serverId,
			String access_token, String validateData) {
		String startDate = DateUtil.getCurrDateTime();
		log.info("开始时间=============={}", startDate);
		JSONObject res = new JSONObject();
		log.info(serverId);
		log.info(access_token);
		log.info(validateData);
		if (StringUtils.isEmpty(name)) {
			res.put("code", ErrorCode.NAME_NULL_CODE_2002);
			res.put("msg", getMessage(ErrorCode.NAME_NULL_CODE_2002));
			return res;
		}
		if (!StringUtils.isChinese(name) || name.trim().length() > 8
				|| name.trim().length() < 2) {
			res.put("code", ErrorCode.NAME_FORMAT_CODE_2010);
			res.put("msg", getMessage(ErrorCode.NAME_FORMAT_CODE_2010));
			return res;
		}
		if (StringUtils.isEmpty(idCard)) {
			res.put("code", ErrorCode.IDCARD_NULL_CODE_2001);
			res.put("msg", getMessage(ErrorCode.IDCARD_NULL_CODE_2001));
			return res;
		}

		try {
			InputStream in = null;
			FileOutputStream outStr = null;
			log.info("=================先取视频========");
			// 先得到视频流
			String filePath = null;
			String mediaCurl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
					+ access_token + "&media_id=" + serverId;
			log.info("<==========腾讯地址：" + mediaCurl);
			URL urlGet = new URL(mediaCurl);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			in = http.getInputStream();
			byte[] dataBy = new byte[10240];
			int len = 0;

			// 目前固定，以后根据不同的serverID再去生成，同时再保存图片
			//filePath = ParamUtil.vedioPath + serverId + ".mp4";
			//系统临时目录
			String path = System.getProperty("java.io.tmpdir");
			String videoName = serverId + ".mp4"; 
			filePath = path + videoName;
			File videoFile = new File(filePath);
			outStr = new FileOutputStream(videoFile);
			while ((len = in.read(dataBy)) != -1) {
				outStr.write(dataBy, 0, len);
			}
			outStr.close();
			String startDate1 = DateUtil.getCurrDateTime();
			log.info("=================开始验证===============");
			ImageClient imageClient = new ImageClient(
					Integer.valueOf(commonParams.getAppid()), commonParams.getYotosecretid(),
					commonParams.getYotosecretkey());
			String video = "";
			String seq = "";
			try {
				video = CommonFileUtils.getFileContent(filePath);
			} catch (Exception ex) {
				log.info("获取视频内容错误：{}", ex);
			}
			FaceIdCardLiveDetectFourRequest liveDetectReq = new FaceIdCardLiveDetectFourRequest(
					commonParams.getBucket(), validateData, video, idCard, name, seq);
			String rest = imageClient.faceIdCardLiveDetectFour(liveDetectReq);
			String endDate1 = DateUtil.getCurrDateTime();
			log.info("============接口结束时间：{}========开始时间{}", endDate1,
					startDate1);
			JSONObject respose = JSONObject.parseObject(rest);
			log.info("face idCard live detect four ret人脸返回结果:{}", rest);
			res.put("data", rest);
			if (respose != null) {
				if (respose.getInteger("code") == 0) {
					res.put("code", ErrorCode.SUCCESS_CODE_0000);
					res.put("msg", getMessage(ErrorCode.SUCCESS_CODE_0000));
					JSONObject data = respose.getJSONObject("data");
					if (data != null) {
						if (data.containsKey("video_photo")) {
							log.info("===========返回照片不为空=========");
							String imageName = serverId + ".jpg";
							String imagePath = path + imageName;
							//Base64解码并保存图片到系统临时目录
							Base64Util.decodeImage(data.getString("video_photo"), imagePath);
							File inageFile = new File(imagePath);
							//保存图片到文件服务器
							fileUploadUtil.uploadFile(inageFile, imageName);
							//保存视频到文件服务器
							fileUploadUtil.uploadFile(videoFile, videoName);
							data.remove("video_photo");
							respose.put("data", data);
							res.put("data", respose);
						}
					}
				} else {
					String msg = getMessage(ErrorCode.SUCCESS_CODE_0000);
					res.put("code", ErrorCode.SUCCESS_CODE_0000);
					res.put("msg", msg);
					res.put("errorreason", ErrorCode.errorMsg(respose.getString("code"),respose.getString("message")));
				}
			} else {
				String msg = getMessage(ErrorCode.FAIL_CODE_0001);
				res.put("code", ErrorCode.FAIL_CODE_0001);
				res.put("msg", msg);
				res.put("errorreason", ErrorCode.errorMsg(ErrorCode.FAIL_CODE_0001,msg));
			}
		} catch (MalformedURLException e) {
			log.info("人脸识别异常：MalformedURLException", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		} catch (ProtocolException e) {
			log.info("人脸识别异常：ProtocolException", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		} catch (IOException e) {
			log.info("人脸识别IO流错误：{}", e);
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
		}
		String endDate = DateUtil.getCurrDateTime();
		log.info("结束时间=============={}====开始时间{}", endDate, startDate);
		return res;
	}

}