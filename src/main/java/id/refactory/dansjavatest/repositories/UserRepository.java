package id.refactory.dansjavatest.repositories;

import id.refactory.dansjavatest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByUsername(String username);
}
