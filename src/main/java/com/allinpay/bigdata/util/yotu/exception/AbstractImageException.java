package com.allinpay.bigdata.util.yotu.exception;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.allinpay.bigdata.util.yotu.http.ResponseBodyKey;

/**
 * 封装cos异常
 * @author chengwu
 *
 */
public abstract class AbstractImageException extends Exception {

    private static final long serialVersionUID = 7547532865194837136L;
    
    private ImageExceptionType type;

    public AbstractImageException(ImageExceptionType type, String message) {
        super(message);
        this.type = type;
    }

    public ImageExceptionType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        JSONObject responseObj = new JSONObject();
        try {
			responseObj.put(ResponseBodyKey.CODE, type.getErrorCode());
			responseObj.put(ResponseBodyKey.MESSAGE, getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return responseObj.toString();
    }
    
}
