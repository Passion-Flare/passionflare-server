package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.util.HttpUtil;
import flare.passion.util.UnitV2RequestBean;
import flare.passion.util.UnitV2RequestBean.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {
    public static String UNIT_URL ="https://aip.baidubce.com/rpc/2.0/unit/service/chat";
    /**
     * UNIT 2.0 API请求方法 只需要场景id 和对话内容参数 大家可以根据需要稍作修改
     * @param query 本轮对话内容
     * @return String
     * @throws Exception
     */
    public  String getUNITV2Result(String query) throws Exception{
        UnitV2RequestBean bean = new UnitV2RequestBean();
        bean.setVersion("2.0");
        bean.setService_id("S54743");
        
        bean.setLog_id("HYF"+System.currentTimeMillis());
        Request request = new Request();
        request.setUser_id("UNIT_DEV_PassionFlare");//测试设置 大家请自行更改
        request.setQuery(query);
        QueryInfo query_info = new QueryInfo();
        query_info.setType("TEXT");
        query_info.setSource("KEYBOARD");
        request.setQuery_info(query_info);
        request.setBernard_level(0);
        //希望传给bot的本地信息
        ClientSession client_session = new ClientSession();
        client_session.setClient_results("hhhh");
        List candidate_options = new ArrayList();
        candidate_options.add(0, "123");
        candidate_options.add(1, "456");
        client_session.setCandidate_options(candidate_options);
        /**
         * ClientSession所需要的是字符串类型 内容为json格式
         * "client_session": "{\"candidate_options\": [\"123\", \"456\"], \"client_results\": \"hhhh\"}"
         */
        String client_sessionparam = JSONObject.toJSONString(client_session);
        request.setClient_session(client_sessionparam);
        bean.setRequest(request);
        bean.setSession_id("HYF"+System.currentTimeMillis());
        String jsonparam = JSONObject.toJSONString(bean);
        String result = HttpUtil.post(UNIT_URL, "", "application/json", jsonparam);
        return result;
    }
}
