package flare.passion.repository;

import flare.passion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    User findById(int id);

    List<User> findAllByType(int type);

}
