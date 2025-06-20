package diary.wep.mongle.repository;

import diary.wep.mongle.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Items, Long> {

}
