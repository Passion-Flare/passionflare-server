package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Passage;
import flare.passion.repository.KnowledgeRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    public int getKnowledgePageCount(int pageSize, String keyword) {
        long recordCount = knowledgeRepository.countByKeyword(keyword);
        return (int) (recordCount % pageSize == 0 ? recordCount / pageSize : recordCount / pageSize + 1);
    }

    public List<JSONObject> getKnowledge(int pageNo, int pageSize, String keyword) {
        List<JSONObject> knowledgeDtoList = new LinkedList<>();
        List<Passage> knowledgeList = knowledgeRepository.findTHEQnAs(keyword, pageNo * pageSize, pageSize);

        // now write dtos
        for (Passage knowledge : knowledgeList) {
            JSONObject knowledgeDto = new JSONObject();
            knowledgeDto.put("id", knowledge.getId());
            knowledgeDto.put("title", knowledge.getTitle());
            knowledgeDto.put("source", knowledge.getSourceAgency());
            knowledgeDto.put("pubDate", knowledge.getPublishDate());
            knowledgeDtoList.add(knowledgeDto);
        }

        return knowledgeDtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void getKnowledgeFromWHO() throws ParseException, IOException {
        // WHO 关于COVID-19和相关健康主题的问答 https://www.who.int/zh/emergencies/diseases/novel-coronavirus-2019/question-and-answers-hub
        String url = "https://www.who.int/zh/emergencies/diseases/novel-coronavirus-2019/question-and-answers-hub";
        Document document = Jsoup.connect(url).get();
        Element body = document.body();
        Elements topics = body.select("a.sf-list-vertical__item");

        // now that we have the topics, it's time to iterate through topic list and get their contents
        int updateCount = 0;
        for (Element topic : topics) {
            String title = topic.select("span.full-title").text();

            Passage passage = knowledgeRepository.findByTitleAndType(title, 2);

            // insert a new record if doesn't exist, update (overwrite) if exists
            if (passage == null) {
                passage = new Passage();
            }

            String link = topic.attr("abs:href");

            System.out.println(title + "\t" + link);

            passage.setType(2);
            passage.setSourceAgency("WHO");
            passage.setTitle(title);
            passage.setLink(link);

            getAndWriteQnA(passage);
            updateCount++;
        }
        System.out.println("updated [" + updateCount + "] records");
    }

    @Transactional(rollbackFor = Exception.class)
    // scrap article content, get publish date and body, write QnA
    private void getAndWriteQnA(Passage passage) throws IOException, ParseException {
        Date publishDate;
        String content;

        // start scraping
        String url = passage.getLink();
        Document document = Jsoup.connect(url).get();
        Element body = document.body();
        Elements header = body.select("div.qa-details__header");

        // extract publish date and assign to passage
        String originalDate = header.select("span").text().replace(" ", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
        publishDate = simpleDateFormat.parse(getDate(originalDate));
        passage.setPublishDate(publishDate);

        // extract content and assign to passage
        Element contentElement = body.select("div.sf-accordion").first();
        Elements subTopics = contentElement.select("div.sf-accordion__panel");
        StringBuilder sb = new StringBuilder();
        // iterate through subtopics, construct their corresponding content and connect them
        for (Element subTopic : subTopics) {
            sb.append(constructSubtopicContent(subTopic));
        }
        content = sb.toString();
        passage.setContent(content);

        knowledgeRepository.save(passage);
    }

    private String getDate(String originalDate) {
        StringBuilder sb = new StringBuilder();
        int len = originalDate.length();
        int lapCounter = 0;
        for (int i = 0; i < len; i++) {
            char c = originalDate.charAt(i);
            if (c == '年') {
                sb.append(" ");
                lapCounter = 0;
            } else if (c == '月') {
                if (lapCounter == 1) {
                    sb.setLength(sb.length() - 1);
                    sb.append("0").append(originalDate.charAt(i - 1)).append(" ");
                } else {
                    sb.append(" ");
                }
                lapCounter = 0;
            } else if (c == '日') {
                if (lapCounter == 1) {
                    sb.setLength(sb.length() - 1);
                    sb.append("0").append(originalDate.charAt(i - 1));
                }
                break;
            } else if (c >= '0' && c <= '9') {
                sb.append(originalDate.charAt(i));
                lapCounter++;
            }
        }
        return sb.toString();
    }

    private String constructSubtopicContent(Element subTopic) {
        StringBuilder sb = new StringBuilder();
        String title = subTopic.select("a.sf-accordion__link").text();
        sb.append("<h2>").append(title).append("</h2>");
        Element contentElement = subTopic.select("div.sf-accordion__content").first();
        Elements paragraphs = contentElement.select("p");
        for (Element paragraph : paragraphs) {
            String paragraphText = paragraph.toString();
            // simulate pointer operation to append strings
            StringBuilder psb = new StringBuilder();
            Elements possibleLinks = paragraph.select("a");
            int startIndex = 0;
            for (Element possibleLink : possibleLinks) {
                int endIndex = startIndex + paragraphText.substring(startIndex).indexOf(possibleLink.toString());
                psb.append(paragraphText, startIndex, endIndex);
                String relativeLink = possibleLink.attr("href");
                String absoluteLink = possibleLink.attr("abs:href");
                psb.append(possibleLink.toString().replace(relativeLink, absoluteLink));
                startIndex = endIndex + possibleLink.toString().length();
            }
            psb.append(paragraphText, startIndex, paragraphText.length());
            sb.append(psb);
        }
        return sb.toString();
    }

}
