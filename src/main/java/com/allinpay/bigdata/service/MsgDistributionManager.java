package com.allinpay.bigdata.service;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.bigdata.api.facerecognition.AntiFraudService;
import com.allinpay.bigdata.api.facerecognition.YouTuDetectService;
import com.allinpay.bigdata.mapper.TChanlqyInfoMapper;
import com.allinpay.bigdata.model.TChanlqyInfo;

@Slf4j
@Component
public class MsgDistributionManager {

	@Autowired
	private TChanlqyInfoMapper tChanlqyInfoMapper;
	@Autowired
	private KafkaManager kafkaManager;
	@Autowired
	private YouTuDetectService youTuDetectService;
	@Autowired
	private AntiFraudService antiFraudService;
	
	@Async
	public void messageHandler(String key,String value)
	{
		log.info("kexin处理Kafka消息{} - {}",key,value);
		JSONObject jsonResult = JSONObject.parseObject(value);
		String interfaceId = jsonResult.getString("interfaceId");//接口ID
		String interfaceName = jsonResult.getString("interfaceName");//接口名
		String operatorId = jsonResult.getString("operatorId");//操作员ID
		String orgId = jsonResult.getString("orgId");//机构ID
		String redisKey = jsonResult.getString("redisKey");//redisKey
		String seralNo = jsonResult.getString("seralNo");//查询批次
		
		TChanlqyInfo chanlInfo = new TChanlqyInfo();
		chanlInfo.setQyBatchId(seralNo);
		chanlInfo.setMerId(orgId);
		chanlInfo.setUserId(operatorId);
		chanlInfo.setIntfcId(interfaceId);
		chanlInfo.setIntfcName(interfaceName);
		chanlInfo.setRediskey(redisKey);
		chanlInfo.setInsertime(new Date());
		
		JSONObject param = jsonResult.getJSONObject("param");
		
		String idCard = param.getString("idCard");
		String name = param.getString("name");
		String telephone = param.getString("phoneNo");
		String cardNum = param.getString("bankNo");
		String serverId = param.getString("serverId");
		String validateData = param.getString("validate_data");
		String access_token = param.getString("accessToken");
		JSONObject result = new JSONObject();
		switch (interfaceId) {
		case "0001":
			result = youTuDetectService.Youtu(idCard, name, serverId, access_token, validateData);
			break;
		case "0002":
			result = antiFraudService.query(idCard, telephone, name, cardNum);
			break;	
		default:
			break;
		}
		log.info(result.toString());
		String code = result.getString("code");
		//接口错误原因
		String errorreason = result.getString("errorreason");
		jsonResult.put("code", code);
		jsonResult.put("msg", result.getString("msg"));
		String data = result.getString("data");
		if(data != null && !"".equals(data)){
			jsonResult.put("data",JSONUtils.parse(data));
		}else{
			jsonResult.put("data","");
		}
		//jsonResult.put("data", JSONUtils.parse(result.getString("data")));
		kafkaManager.produce("wjt_data", jsonResult.toString());
		log.info("发送kafka消息wjt_data--"+jsonResult.toString());
		chanlInfo.setQueryStatus("0000".equals(code)?"1":"2");
		if(errorreason != null){
			chanlInfo.setErrorreason(errorreason);
		}
		tChanlqyInfoMapper.insert(chanlInfo);
		
	}
	
}
