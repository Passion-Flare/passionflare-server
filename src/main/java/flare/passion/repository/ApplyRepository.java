package flare.passion.repository;

import flare.passion.model.Apply;
import flare.passion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, String> {

    Apply findById(int id);

    List<Apply> findByUserOrderByIdDesc(User user);

    Boolean existsByUserIdAndStatus(int id, int status);

}
