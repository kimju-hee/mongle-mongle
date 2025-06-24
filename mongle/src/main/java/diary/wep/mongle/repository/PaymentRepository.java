package diary.wep.mongle.repository;

import diary.wep.mongle.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
    @Query("SELECT p FROM Payments p JOIN FETCH p.item WHERE p.user.userId = :userId")
    List<Payments> findAllWithItemByUserId(@Param("userId") Long userId);
}
