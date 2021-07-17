package flare.passion.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportService {

    @Value("${files.path}")
    private String filePath;

    private List<String> riskCities = new ArrayList<>();
    private final RiskAreaService riskAreaService = new RiskAreaService();
    private final JSONArray riskArea = riskAreaService.getRiskAreaDataFromDxy().getJSONArray("data");

    private List<String> getRiskCities() {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < riskArea.size(); i++) {
            JSONArray dangerPros = riskArea.getJSONObject(i).getJSONArray("dangerPros");
            for (int j = 0; j < dangerPros.size(); i++) {
                JSONArray dangerAreas = dangerPros.getJSONObject(i).getJSONArray("dangerAreas");
                for (int k = 0; k < dangerAreas.size(); k++) {
                    if (!ret.contains(dangerAreas.getJSONObject(k).getString("cityName"))) {
                        ret.add(dangerAreas.getJSONObject(k).getString("cityName"));
                    }
                }
            }
        }
        return ret;
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


    public void saveFlightInfo() throws IOException {
        riskCities = getRiskCities();
        for (int i = 0; i < riskCities.size(); i++) {
            JSONArray departure = new JSONArray();
            JSONArray arrival = new JSONArray();
            String city = riskCities.get(i);
            /*
             * Todo:获取城市航班信息
             * */

            String write = JSON.toJSONString(departure);
            FileWriter fw = new FileWriter(filePath + "/flightInfo/" + city + "1.json");
            PrintWriter out = new PrintWriter(fw);
            out.write(write);
            write = JSON.toJSONString(arrival);
            fw = new FileWriter(filePath + "/flightInfo/" + city + "0.json");
            out = new PrintWriter(fw);
            out.write(write);
            fw.close();
            out.close();
        }
        return;
    }


    public void saveTrainInfo() throws IOException {
        riskCities = getRiskCities();
        for (int i = 0; i < riskCities.size(); i++) {
            JSONArray departure = new JSONArray();
            JSONArray arrival = new JSONArray();
            String city = riskCities.get(i);
            /*
             * Todo:获取城市火车信息
             * */

            String write = JSON.toJSONString(departure);
            FileWriter fw = new FileWriter(filePath + "/trainInfo/" + city + "1.json");
            PrintWriter out = new PrintWriter(fw);
            out.write(write);
            write = JSON.toJSONString(arrival);
            fw = new FileWriter(filePath + "/trainInfo/" + city + "0.json");
            out = new PrintWriter(fw);
            out.write(write);
            fw.close();
            out.close();
        }
        return;
    }

    /*
     * type:1为出发地departure，0为降落地arrival
     * */
    public JSONArray getFlightInfoByCity(String city, boolean type) {
        File file = new File(filePath + "/flightInfo/-" + city + type + ".json");
        String laststr = getFromFile(file);
        return (JSONArray) JSON.parse(laststr);
    }

    public JSONArray getFlightInfo() {
        JSONArray ret = new JSONArray();
        riskCities = getRiskCities();
        for (int i = 0; i < riskCities.size(); i++) {
            JSONObject tmp = new JSONObject();
            String city = riskCities.get(i);
            tmp.put("city", city);
            tmp.put("departure", getFlightInfoByCity(city, true));
            tmp.put("arrival", getFlightInfoByCity(city, false));
            ret.add(tmp);
        }
        return ret;
    }

    public JSONArray getTrainInfoByCity(String city, boolean type) {
        File file = new File(filePath + "/trainInfo/-" + city + type + ".json");
        String laststr = getFromFile(file);
        return (JSONArray) JSON.parse(laststr);
    }

    public JSONArray getTrainInfo() {
        JSONArray ret = new JSONArray();
        riskCities = getRiskCities();
        for (int i = 0; i < riskCities.size(); i++) {
            JSONObject tmp = new JSONObject();
            String city = riskCities.get(i);
            tmp.put("city", city);
            tmp.put("departure", getTrainInfoByCity(city, true));
            tmp.put("arrival", getTrainInfoByCity(city, false));
            ret.add(tmp);
        }
        return ret;
    }
}
