package flare.passion.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    @Autowired
    private AIService aiService;

    @GetMapping("/getresponse")
    public JSONObject getResponse(@RequestParam String query){
        JSONObject jsonObject=new JSONObject();
        try {
            if(query.contains("北航校草")){
                jsonObject.put("message","谁不知道何宇峰是北航校草？");
                jsonObject.put("success",true);
            }else if(query.contains("何宇峰")&&query.contains("谁")){
                jsonObject.put("message","何宇峰是我的主人，也是北航校草");
                jsonObject.put("success",true);
            }else if(query.contains("赵致远")&&query.contains("谁")){
                jsonObject.put("message","赵致远？北航男酮罢了");
                jsonObject.put("success",true);
            }else if(query.contains("你是谁")){
                jsonObject.put("message","我是何宇峰的PassionFlareRobot");
                jsonObject.put("success",true);
            }else{
                JSONObject tmp= JSON.parseObject(aiService.getUNITV2Result(query));
                JSONArray responceList =tmp.getJSONObject("result").getJSONArray("response_list");
                int highConf=-1,place=-1;
                for (int i=0;i<=4;i++){
                    int conf=responceList.getJSONObject(i).getJSONArray("action_list").
                            getJSONObject(0).getInteger("confidence");
                    if(conf==100){
                        place=i;
                        break;
                    }
                    else if(conf>highConf){
                        highConf=conf;
                        place=i;
                    }
                }
                jsonObject.put("message",responceList.getJSONObject(place).getJSONArray("action_list").
                        getJSONObject(0).getString("say"));
                jsonObject.put("success",true);
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success",false);
            jsonObject.put("exc",e.getMessage());

        }
        return jsonObject;
    }
}
