package diary.wep.mongle.repository;

import diary.wep.mongle.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
