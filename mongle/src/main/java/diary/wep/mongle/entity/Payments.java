package diary.wep.mongle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "status")
    private String status;

    @Column(name = "method")
    private String method;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "kakao_id")
    private String kakaoTid;

    @Column(name = "paid_at")
    private LocalDate paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    @Builder
    public Payments(String status, String method, Integer amount, String kakaoTid, LocalDate paidAt) {
        this.status = status;
        this.method = method;
        this.amount = amount;
        this.kakaoTid = kakaoTid;
        this.paidAt = paidAt;
    }

}
