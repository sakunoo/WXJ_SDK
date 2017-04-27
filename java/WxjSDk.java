package com.kingkoo.wxjin;

import org.apache.commons.collections.map.HashedMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author sakuno
 * @date 2017/4/27
 * @Description
 *  微型金简易SDK，返回内容为JSON字符串，可自行处理
 *  具体的返回字段请查阅官方API文档
 *  https://www.wxjin.com/agreement/api
 */
public class WxjSDk {
    public static final String HOST_URL = "https://api.wxjin.com/trade/api/third/";

    //微型金ID
    private String fundId;

    //微型金ID对应的授权码
    private String authorize;

    private WxjSDk(){}

    /**
     * 对外提供的构造方法
     * @param fundId    微型金ID
     * @param authorize 微型金ID对应的授权码
     */
    public WxjSDk(String fundId, String authorize) {
        this.fundId = fundId;
        this.authorize = authorize;
    }


    /**
     *查询资金信息
     * @return
     */
    public String queryFunds() {
        return sendPost("fundInfo", toParam(null));
    }


    /**
     * 查询持仓
     * @return
     */
    public String queryHold(){
        return sendPost("holdsList", toParam(null));
    }


    /**
     * 委托下单
     * @param stockCode     股票代码
     * @param price         委托价格
     * @param num           委托数量
     * @return
     */
    public String buy(String stockCode,BigDecimal price,String num){
        Map<String, Object> params = new HashedMap();
        params.put("stockCode", stockCode);
        params.put("price", price);
        params.put("num", num);
        return sendPost("buy", toParam(params));
    }

    /**
     *
     * @param stockCode     股票代码
     * @param price         委托价格
     * @param num           委托数量
     * @return
     */
    public String sell(String stockCode, BigDecimal price,Integer num){
        Map<String, Object> params = new HashedMap();
        params.put("stockCode", stockCode);
        params.put("price", price);
        params.put("num", num);
        return sendPost("sell", toParam(params));
    }


    /**
     * 将map格式的数据转换为所需的请求参数
     * @param params
     *              请求参数
     * @return
     */
    public  String toParam(Map<String, Object> params) {
        if (params == null) {
            params = new HashedMap();
        }
        params.put("fundId", fundId);
        params.put("authorize", authorize);
        StringBuffer buffer = new StringBuffer();
        for (String k : params.keySet()) {
            buffer.append(k).append("=").append(params.get(k)).append("&");
        }
        return buffer.toString();
    }

    /**
     * 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String func, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String url = HOST_URL + func;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("微型金请求发送异常："+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
