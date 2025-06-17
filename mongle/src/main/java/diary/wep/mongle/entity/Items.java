package diary.wep.mongle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "item_price")
    private Integer itemPrice;

    @Builder
    public Items(String itemName, String itemDescription, String imageUrl, Integer itemPrice) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.imageUrl = imageUrl;
        this.itemPrice = itemPrice;
    }
}
