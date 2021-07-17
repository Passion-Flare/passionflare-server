package flare.passion.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProxyService {

    private final RestTemplate restTemplate = new RestTemplate();

    public synchronized Object request(String url) {
        return restTemplate.getForObject(url, Object.class);
    }

}
