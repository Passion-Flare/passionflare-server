package flare.passion.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedList;
import java.util.List;

import java.util.LinkedList;
import java.util.List;

@Service
public class RiskAreaService {


    public JSONObject getRiskAreaDataFromDxy() {
        String url = "https://file1.dxycdn.com/2021/0202/196/1680100273140422643-135.json";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);
        return json;
    }

    public List<JSONObject> getRiskAreaDataFromDxy_v2() {
        String url = "https://file1.dxycdn.com/2021/0202/196/1680100273140422643-135.json";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);

        JSONArray jsonDataList = json.getJSONArray("data");
        List<JSONObject> dataList = new LinkedList<>();

        int data_count = jsonDataList.size();
        for (int i = 0; i < data_count; i++) {
            JSONObject jsonData = jsonDataList.getJSONObject(i);
            JSONArray jsonDangerProsList = jsonData.getJSONArray("dangerPros");
            int id =1;
            String dangerLevel = jsonData.getString("dangerLevel"); //1: high risk 2: middle risk
            int dangerPros_count = jsonDangerProsList.size();
            for (int j = 0; j < dangerPros_count; j++) {
                JSONObject jsonDangerPro = jsonDangerProsList.getJSONObject(j);
                JSONArray jsonDangerAreasList = jsonDangerPro.getJSONArray("dangerAreas");

                int dangerAreas_count = jsonDangerAreasList.size();
                for (int k = 0; k < dangerAreas_count; k++) {
                    JSONObject data = new JSONObject();
                    JSONObject jsonDangerArea = jsonDangerAreasList.getJSONObject(k);
                    data.put("id", id++);
                    data.put("provinceName", jsonDangerPro.getString("provinceName"));
                    data.put("cityName", jsonDangerArea.getString("cityName"));
                    data.put("areaName", jsonDangerArea.getString("areaName"));
                    data.put("dangerLevel",dangerLevel);
                    dataList.add(data);
                }
            }
        }
        return dataList;
    }
}
