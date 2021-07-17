package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Passage;
import flare.passion.model.User;
import flare.passion.repository.ArticleRepository;
import flare.passion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserRepository userRepository;

    public Passage createArticle(int id, String source, String thumbnail, String title,
                                 int type, String content, String rumorExtObj) throws Exception {
        Passage passage;
        if (id == 0) {
            passage = new Passage();
        } else {
            passage = articleRepository.findById(id);
        }
        if (passage != null) {
            passage.setSourceAgency(source);
            passage.setThumbnailUrl(thumbnail);
            passage.setTitle(title);
            passage.setType(type);
            passage.setContent(content);
            passage.setOriginalJson(rumorExtObj);
            long curTimeStamp = System.currentTimeMillis();
            passage.setPublishDate(new Date(curTimeStamp));
            articleRepository.save(passage);
        } else {
            throw new Exception("未找到id为" + id + "的文章");
        }
        return passage;
    }

    public JSONObject getAnArticle(int id) {
        JSONObject articleDto = new JSONObject();
        Passage passage = articleRepository.findById(id);
        articleDto.put("id", passage.getId());
        articleDto.put("content", passage.getContent());
        articleDto.put("thumbnail", passage.getThumbnailUrl());
        articleDto.put("source", passage.getSourceAgency());
        articleDto.put("pubTime", passage.getPublishDate());
        articleDto.put("title", passage.getTitle());
        articleDto.put("extObj", passage.getOriginalJson());
        articleDto.put("type", passage.getType());
        return articleDto;
    }

    public void banAnArticle(int id) {
        Passage article = articleRepository.findById(id);
        article.setBanned(true);
        articleRepository.save(article);
    }

    public List<JSONObject> getOfficialAccounts() {
        List<JSONObject> officialAccountsDto = new LinkedList<>();
        List<User> officialAccounts = userRepository.findAllByType(2);
        for (User officialAccount : officialAccounts) {
            JSONObject officialAccountDto = new JSONObject();
            officialAccountDto.put("id", officialAccount.getId());
            officialAccountDto.put("name", officialAccount.getName());
            officialAccountDto.put("avatar", officialAccount.getAvatar());
            officialAccountsDto.add(officialAccountDto);
        }
        return officialAccountsDto;
    }

}
