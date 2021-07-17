package flare.passion.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static flare.passion.util.APIRequestUtil.getRequestFromUrl;


@Service
public class HistoricalDataService {
    private final String url = "https://corona.lmao.ninja/v2/historical?lastdays=390";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
    private final String date = simpleDateFormat.format(new Date());
    @Value("${files.path}")
    private String filePath;
    private JSONArray data;

    public void saveHistoricalDataToLocal() throws IOException {
        data = getRequestFromUrl(url);
        String write = JSON.toJSONString(data);
        FileWriter fw = new FileWriter(filePath + "/historicaldata/data-" + date + ".json");
        PrintWriter out = new PrintWriter(fw);
        out.write(write);
        fw.close();
        out.close();
        System.out.println("history data saved");
        return;
    }


    public JSONArray getHistoryDataFromLocal() {
        File file = new File(filePath + "/historicaldata/data-" + date + ".json");
        String laststr = getFromFile(file);
        return (JSONArray) JSON.parse(laststr);
    }

    private JSONObject getCountryCoordinates(String countryIndex) throws IOException {
        JSONObject ret = new JSONObject();
        String laststr = CharStreams.toString(new InputStreamReader(new ClassPathResource("countryInfo.json").getInputStream(), Charsets.UTF_8));
        JSONObject jsonObject = (JSONObject) JSON.parse(laststr);
        ret.put("coordinates", jsonObject.getJSONObject(countryIndex).getJSONObject("coordinates"));
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


    public JSONArray createHistoricalDataByWeek(int n) throws IOException {
        JSONArray sourceData = getHistoryDataFromLocal();
        JSONArray ret = new JSONArray();
        int len = sourceData.size();
        for (int i = 0; i < len; i++) {
            JSONObject singleCountry = sourceData.getJSONObject(i);
            JSONObject retSingleCountry = new JSONObject();
            retSingleCountry.put("country", singleCountry.getString("country"));
            if (singleCountry.containsKey("province")) {
                retSingleCountry.put("province", singleCountry.getString("province"));
            } else retSingleCountry.put("province", null);

            String countryIndex = retSingleCountry.getString("country") + " " + retSingleCountry.getString("province");
            retSingleCountry.put("coordinates", getCountryCoordinates(countryIndex).getJSONObject("coordinates"));

            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -2);
            String basedate = sdf.format(calendar.getTime());
            retSingleCountry.put("basedate", basedate);

            JSONObject timeline = new JSONObject();
            List<Integer> recovered = new ArrayList<>();
            List<Integer> deaths = new ArrayList<>();
            List<Integer> cases = new ArrayList<>();
            JSONObject recoveredAll = singleCountry.getJSONObject("timeline").getJSONObject("recovered");
            JSONObject deathsAll = singleCountry.getJSONObject("timeline").getJSONObject("deaths");
            JSONObject casesAll = singleCountry.getJSONObject("timeline").getJSONObject("cases");

            for (int j = 0; j <= n; j++) {
                calendar.add(Calendar.DATE, -7);
                String dateNow = sdf.format(calendar.getTime());
                recovered.add(Integer.parseInt(recoveredAll.getString(dateNow)));
                deaths.add(Integer.parseInt(deathsAll.getString(dateNow)));
                cases.add(Integer.parseInt(casesAll.getString(dateNow)));
            }

            timeline.put("recovered", recovered);
            timeline.put("deaths", deaths);
            timeline.put("cases", cases);

            retSingleCountry.put("timeline", timeline);

            ret.add(retSingleCountry);
        }

        return ret;
    }

    public void saveHistoricalDataByWeek() throws IOException {
        for (int i = 0; i <= 52; i++) {
            FileWriter fw = new FileWriter(filePath + "/historicaldatabyweek/week-" + i + ".json");
            JSONArray jsonArray = createHistoricalDataByWeek(i);
            String write = JSON.toJSONString(jsonArray);
            PrintWriter out = new PrintWriter(fw);
            out.write(write);
            fw.close();
            out.close();
        }
        System.out.println("weekly history data saved");
        return;
    }

    public JSONArray getHistoricalDataByWeek(int n) {
        String filepath = filePath + "/historicaldatabyweek/week-" + n + ".json";
        String laststr = getFromFile(new File(filepath));
        return (JSONArray) JSON.parse(laststr);
    }
}
