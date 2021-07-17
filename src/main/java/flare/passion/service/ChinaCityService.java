package flare.passion.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.classgraph.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.service.Response;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class ChinaCityService {

    public JSONObject getChinaCityDiseaseDataFromSina() {
        String url = "https://interface.sina.cn/news/wap/fymap2020_data.d.json";
        RestTemplate restTemplate = new RestTemplate();
        // original data
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);
        JSONObject jsonData = json.getJSONObject("data");
        JSONArray jsonProvinceDataList = jsonData.getJSONArray("list");

        //national total data
        JSONObject totalData = new JSONObject();
        totalData.put("deathNum", jsonData.getString("deathtotal"));
        totalData.put("cureNum", jsonData.getString("curetotal"));
        totalData.put("value", jsonData.getString("gntotal"));
        totalData.put("vaccinationNum", 1318417000);

        JSONObject data = new JSONObject();
        List<JSONObject> cityDataList = new LinkedList<>();

        int province_count = jsonProvinceDataList.size();
        for (int i = 0; i < province_count; i++) {
            JSONObject jsonProvinceData = jsonProvinceDataList.getJSONObject(i);
            JSONArray jsonCityDataList = jsonProvinceData.getJSONArray("city");

            String province_name = jsonProvinceData.getString("name");
            //city or district data
            int city_count = jsonCityDataList.size();
            for (int j = 0; j < city_count; j++) {
                JSONObject jsonCityData = jsonCityDataList.getJSONObject(j);
                JSONObject cityData = new JSONObject();
                cityData.put("name", jsonCityData.get("name"));
                cityData.put("econNum", jsonCityData.get("econNum"));
                cityData.put("value", jsonCityData.get("conNum"));
                String asymptomNum = jsonCityData.getString("asymptomNum");
                if (asymptomNum == "") {
                    asymptomNum = "0";
                }
                cityData.put("asymptomNum", asymptomNum);
                cityData.put("deathNum", jsonCityData.get("deathNum"));
                cityData.put("cureNum", jsonCityData.get("cureNum"));
                cityData.put("province", province_name);
                cityDataList.add(cityData);
            }

            // province total data
            JSONObject cityData = new JSONObject();
            cityData.put("name", "总计");
            cityData.put("econNum", jsonProvinceData.get("econNum"));
            cityData.put("value", jsonProvinceData.get("value"));
            String asymptomNum = jsonProvinceData.getString("asymptomNum");
            if (asymptomNum == "") {
                asymptomNum = "0";
            }
            cityData.put("asymptomNum", asymptomNum);
            cityData.put("deathNum", jsonProvinceData.get("deathNum"));
            cityData.put("cureNum", jsonProvinceData.get("cureNum"));
            cityData.put("province", province_name);
            cityDataList.add(cityData);
        }

        data.put("totaldata", totalData);
        data.put("citydata", cityDataList);

        return data;
    }


    public JSONObject getChinaCityDataFrom163() {
        String url = "https://c.m.163.com/ug/api/wuhan/app/data/list-total";

        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, entity, JSONObject.class);
            json = response.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JSONObject jsonData = json.getJSONObject("data");
        JSONObject jsonChinaTotal = jsonData.getJSONObject("chinaTotal");
        JSONObject jsonTotal = jsonChinaTotal.getJSONObject("total");
        JSONObject jsonExtData = jsonChinaTotal.getJSONObject("extData");
        JSONObject jsonToday = jsonChinaTotal.getJSONObject("today");

        JSONObject total = new JSONObject();
        total.put("total", jsonTotal.getString("confirm"));
        total.put("heal", jsonTotal.getString("heal"));
        total.put("dead", jsonTotal.getString("dead"));
        total.put("input", jsonTotal.getString("input"));
        total.put("noSymptom", jsonExtData.getString("noSymptom"));
        total.put("confirm", jsonTotal.getIntValue("confirm") -
                jsonTotal.getIntValue("dead") -
                jsonTotal.getIntValue("heal"));
        total.put("addConfirm", jsonToday.getString("storeConfirm"));
        total.put("addTotal", "+" + jsonToday.getString("confirm"));
        total.put("addHeal", "+" + jsonToday.getString("heal"));
        total.put("addDead", "+" + jsonToday.getString("dead"));
        total.put("addInput", "+" + jsonToday.getString("input"));
        total.put("addNoSymptom", "+" + jsonExtData.getString("incrNoSymptom"));

        data.put("total", total);
        return data;
    }

}
