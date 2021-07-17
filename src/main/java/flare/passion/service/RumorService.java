package flare.passion.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Passage;
import flare.passion.repository.RumorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class RumorService {

    @Autowired
    private RumorRepository rumorRepository;

    public int getRumorsPageCount(int pageSize, String keyword) {
        long recordCount = rumorRepository.countByKeyword(keyword);
        return (int) (recordCount % pageSize == 0 ? recordCount / pageSize : recordCount / pageSize + 1);
    }

    public List<JSONObject> getRumors(int pageNo, int pageSize, String keyword) {
        List<JSONObject> rumorDtoList = new LinkedList<>();
        List<Passage> rumorList = rumorRepository.findTHERUMORS(keyword, pageNo * pageSize, pageSize);

        // now write dtos
        for (Passage rumor : rumorList) {
            JSONObject rumorDto = new JSONObject();
            rumorDto.put("id", rumor.getId());
            rumorDto.put("title", rumor.getTitle());
            rumorDto.put("source", rumor.getSourceAgency());
            rumorDto.put("pubDate", rumor.getPublishDate());
            rumorDto.put("abstract", null);
            rumorDto.put("extId", rumor.getExternalId());
            rumorDto.put("extLink", rumor.getLink());
            rumorDto.put("extObj", rumor.getOriginalJson());
            rumorDto.put("thumbnail", rumor.getThumbnailUrl());
            rumorDtoList.add(rumorDto);
        }

        return rumorDtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void getRumorsFromWebsiteBelow() throws Exception {
        // https://www.piyao.org.cn/2020yqpy/
        // 15 new records on one update
        String url = "https://qcwa.news.cn/nodeart/list?nid=11215616&pgnum=1&cnt=15&attr=63&tp=1&orderby=1";
        RestTemplate restTemplate = new RestTemplate();
        String content = restTemplate.getForObject(url, String.class);
        try {
            content = content.replace("(", "").replace(")", "");
        } catch (NullPointerException e) {
            throw new Exception("actually got nothing");
        }
        JSONObject json = JSON.parseObject(content);
        System.out.println(json);
        JSONObject rumorListWrapper = json.getJSONObject("data");

        JSONArray rumorList = rumorListWrapper.getJSONArray("list");

        Date latestRumorDate;
        try {
            latestRumorDate = rumorRepository.findLatestRumor().getPublishDate();
        } catch (NullPointerException e) {
            latestRumorDate = new Date(0L);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // write to database
        int count = rumorList.size();
        int updateCount = 0;
        for (int i = 0; i < count; i++, updateCount++) {
            JSONObject rumor = rumorList.getJSONObject(i);
            Date publishDate = simpleDateFormat.parse(rumor.getString("PubTime"));
            String externalId = rumor.getString("DocID");
            if (publishDate.before(latestRumorDate) || rumorRepository.existsByExternalIdAndType(externalId, 3)) {
                updateCount--;
                continue;
            }
            Passage rumorModel = new Passage();
            rumorModel.setExternalId(externalId);
            rumorModel.setTitle(rumor.getString("Title"));
            rumorModel.setThumbnailUrl(null);
            rumorModel.setSourceAgency(rumor.getString("SourceName"));
            rumorModel.setLink(rumor.getString("LinkUrl"));
            rumorModel.setOriginalJson(rumor.toJSONString());
            rumorModel.setPublishDate(publishDate);
            rumorModel.setType(3);
            rumorRepository.save(rumorModel);
        }
        System.out.println("updated [" + updateCount + "] records");
    }

}
