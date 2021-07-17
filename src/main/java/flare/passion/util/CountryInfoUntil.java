package flare.passion.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.util.APIRequestUtil;

import java.io.*;


/*
* @Author: Hyphen
* 此工具类用于修改CountryInfo字典，请勿改动以防bug
* */


public class CountryInfoUntil {
    public static String getCountryNameFromISOCode(String iSOCode) throws IOException {
        String countryName=null;
        String url="https://restcountries.eu/rest/v2/alpha/"+iSOCode;
        JSONObject data=APIRequestUtil.getRequestFromUrl(url).getJSONObject(0);
        countryName=data.getString("name");
        return countryName;
    }
    private String getFromFile(File file) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

    public void foo() throws IOException {
        JSONArray jsonArray=(JSONArray) JSON.parse(getFromFile(new File("src/main/resources/CountryInfo/countryInfo.json")));
        JSONObject ret=new JSONObject();
        int len=jsonArray.size();
        for (int i=0;i<len;i++){
            JSONObject tmp=jsonArray.getJSONObject(i);
            String index;
            if(tmp.getString("province")==null){
                index=tmp.getString("country")+" "+tmp.getString("province");
            }else {
                index = tmp.getString("country") + " " + tmp.getString("province").toLowerCase();
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("province",tmp.getString("province"));
            jsonObject.put("coordinates",tmp.getJSONObject("coordinates"));
            ret.put(index,jsonObject);
        }
        String write= JSON.toJSONString(ret);
        FileWriter fw = new FileWriter("src/main/resources/CountryInfo/countryInfo.json");
        PrintWriter out = new PrintWriter(fw);
        out.write(write);
        fw.close();
        out.close();
        return;
    }
}
