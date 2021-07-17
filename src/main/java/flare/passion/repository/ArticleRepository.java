package flare.passion.repository;

import flare.passion.model.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Passage, String> {

    Passage findById(int id);

    void deleteById(int id);
}
