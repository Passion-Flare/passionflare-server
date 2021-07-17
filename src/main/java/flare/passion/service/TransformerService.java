package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Transformer;
import flare.passion.repository.TransformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransformerService {

    @Autowired
    private TransformerRepository transformerRepository;

    public int addATransformerToCollection(String name, String type) {
        Transformer transformer = new Transformer();
        transformer.setName(name);
        transformer.setType(type);
        transformerRepository.save(transformer);
        return transformer.getId();
    }

    public Transformer getATransformer(String id) {
        JSONObject ret = new JSONObject();
        Optional<Transformer> optionalTransformer = transformerRepository.findById(id);
        return optionalTransformer.orElse(null);
    }

}
