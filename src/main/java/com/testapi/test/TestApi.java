package com.testapi.test;

import com.google.common.collect.Maps;
import com.testapi.utils.HttpClientUtils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * api测试工具
 */
public class TestApi {


    /**
     * 接口秘钥
     */
    static String AccessKey = "825b8ef5e90d8d01db16904dc91fc325";
    /**
     * 加密密钥
     */
    static String Secret = "dd8b6f1d-ac42-4662-a461-77926eb9c2ea";


    /**
     * 调用http请求接口
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //请求接口
        sendHttp();
        System.exit(0);
    }

    /**
     * accessKey
     * timestamp
     * orderId
     * market
     * pageSize
     * pageNumber
     *
     * @return
     */
    private static String sendHttp() {

        /**
         * 1、定义访问接口地址
         */

        //String url = "http://show.bihuex.com/api-web/";
        String url = "https://home.bihuex.com/api-web/";
        String apiUrl = "api/v2/user/getMyTradeLog";

        /**
         * 2、根据所需参数进行拼接
         */
        HashMap<String, String> params = Maps.newHashMap();
        params.put("accessKey", AccessKey);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        //params.put("orderId","1643977501");
        params.put("market", "sse_usdt");
        //params.put("pageSize","10");
        //params.put("pageNumber","1");
        //params.put("type","1");
        //params.put("price","0.1");
        //params.put("payPassWord","123456");
        //params.put("amount","1");
        //params.put("token","mobie_token_rrdrxxmxrutbfmzzkfyjbwxgymloqzhxtxdjvsucxgdfwjrvfwpawoduncpffbnw");
        //params.put("remark","测试积分减少");
        //params.put("timestamp","1551860557901");
        //params.put("balance","10");
        //params.put("accountApiKey","1c761c026c34698b9f69df224c479c7f");

        /**
         * 3、生成签名进行验签
         */
        System.out.println("====生成签名开始====：" + String.valueOf(System.currentTimeMillis()));
        String sign = getSignature(params, Secret);
        System.out.println("====验签前====" + sign);
        System.out.println("====生成签名结束====：" + String.valueOf(System.currentTimeMillis()));
        params.put("sign", sign);
        System.out.println("====验签后====" + sign);
        StringBuffer buff = new StringBuffer();
        buff.append(url);
        buff.append(apiUrl);
        String httpClientResult = HttpClientUtils.sendHttpPostMap(buff.toString(), params);
        System.out.println(httpClientResult + "=====调用接口返回JSON串=====");
        return httpClientResult;


    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     *
     * @param params 消息
     * @param secret 秘钥
     * @return 加密后字符串
     */
    private static String getSignature(Map<String, String> params, String secret) {
        String hash = "";
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseStr = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            baseStr.append(param.getKey()).append("=").append(param.getValue());
        }
        baseStr.append(secret);
        System.out.println(baseStr.toString());
        System.out.println(secret);
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(baseStr.toString().getBytes());
            //  String s = Base64.encodeBase64String(bytes);
            hash = byteArrayToHexString(bytes);
            //MessageDigest md5 = MessageDigest.getInstance("MD5");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

}