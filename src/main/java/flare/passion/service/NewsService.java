package flare.passion.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Passage;
import flare.passion.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public int getNewsPageCount(int pageSize, String keyword) {
        long recordCount = newsRepository.countNewsByKeyword(keyword);
        return (int) (recordCount % pageSize == 0 ? recordCount / pageSize : recordCount / pageSize + 1);
    }

    public List<JSONObject> getNews(int pageNo, int pageSize, String keyword) {
        List<JSONObject> newsDtoList = new LinkedList<>();
        List<Passage> newsList = newsRepository.findTHENEWS(keyword, pageNo * pageSize, pageSize);

        // now write dtos
        for (Passage news : newsList) {
            JSONObject newsDto = new JSONObject();
            newsDto.put("id", news.getId());
            newsDto.put("title", news.getTitle());
            newsDto.put("source", news.getSourceAgency());
            newsDto.put("pubDate", news.getPublishDate());
            newsDto.put("thumbnail", news.getThumbnailUrl());
            newsDto.put("extId", news.getExternalId());
            newsDto.put("extLink", news.getLink());
            newsDtoList.add(newsDto);
        }

        return newsDtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void getNewsFromQQNews() throws ParseException {
        // get news list from qq news https://new.qq.com/ch/antip/
        // 30 new records on one update
        String ext = "{\"pool\":[\"high\",\"top\"],\"is_filter\":10,\"check_type\":true}";
        String url = "https://i.news.qq.com/trpc.qqnews_web.kv_srv.kv_srv_http_proxy/list?sub_srv_id=antip&srv_id=pc&offset=0&limit=30&strategy=1&ext={ext}";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class, ext);
        System.out.println("scheduled task [update news] " + json.get("msg"));
        JSONObject newsListWrapper = json.getJSONObject("data");

        JSONArray newsList = newsListWrapper.getJSONArray("list");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // write to database
        int count = newsList.size();
        int updateCount = 0;
        for (int i = 0; i < count; i++, updateCount++) {
            JSONObject news = newsList.getJSONObject(i);
            Date publishDate = simpleDateFormat.parse(news.getString("publish_time"));
            String externalId = news.getString("cms_id");
            if (newsRepository.existsByExternalIdAndType(externalId, 1)) {
                updateCount--;
                continue;
            }
            Passage newsModel = new Passage();
            newsModel.setExternalId(externalId);
            newsModel.setTitle(news.getString("title"));
            newsModel.setThumbnailUrl(news.getString("thumb_nail_2x"));
            newsModel.setSourceAgency(news.getString("media_name"));
            newsModel.setLink(news.getString("url"));
            newsModel.setPublishDate(publishDate);
            newsModel.setType(1);
            newsRepository.save(newsModel);
        }
        System.out.println("updated [" + updateCount + "] records");
    }

    public int getAnnouncementPageCount(int pageSize) {
        long recordCount = newsRepository.countByType(4);
        return (int) (recordCount % pageSize == 0 ? recordCount / pageSize : recordCount / pageSize + 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<JSONObject> getAnnouncement(int pageNo, int pageSize) {
        List<JSONObject> announcementDtoList = new LinkedList<>();
        List<Passage> announcementList = newsRepository.findTHEANNOUNCEMENT(pageNo * pageSize, pageSize);

        for (Passage announcement : announcementList) {
            JSONObject announcementDto = new JSONObject();
            announcementDto.put("title", announcement.getTitle());
            announcementDto.put("pubTime", announcement.getPublishDate());
            announcementDto.put("source", announcement.getSourceAgency());
            announcementDto.put("link", announcement.getLink());
            announcementDto.put("abstract", announcement.getContent());
            announcementDtoList.add(announcementDto);
        }
        return announcementDtoList;
    }

    public void getAnnouncementFromDxy() {
        // get announcement list from dxy api
        String url = "https://file1.dxycdn.com/2020/0130/492/3393874921745912795-115.json";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);

        JSONArray announcementList = json.getJSONArray("data");

        int count = announcementList.size();
        int updateCount = 0;
        for (int i = 0; i < count; i++, updateCount++) {
            JSONObject announcement = announcementList.getJSONObject(i);
            Long timestamp = Long.parseLong(announcement.getString("pubDate"));
            Date pubDate = new Date(timestamp);
            String externalId = announcement.getString("articleId");
            if (newsRepository.existsByExternalIdAndType(externalId, 4)) {
                updateCount--;
                continue;
            }
            Passage announcementModel = new Passage();
            announcementModel.setContent(announcement.getString("summary"));
            announcementModel.setExternalId(announcement.getString("articleId"));
            announcementModel.setLink(announcement.getString("sourceUrl"));
            announcementModel.setTitle(announcement.getString("title"));
            announcementModel.setSourceAgency(announcement.getString("infoSource"));
            announcementModel.setPublishDate(pubDate);
            announcementModel.setType(4);
            newsRepository.save(announcementModel);
        }
        System.out.println("updated [" + updateCount + "] records");
    }
}
