package io.rapa.shooongbackend.member;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.rapa.shooongbackend.common.entity.BaseEntity;
import io.rapa.shooongbackend.order.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Members extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long memberId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String loginId;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<>();

    @Builder
    public Members(
            String name,
            String loginId,
            String password
    ) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public void addOrder(Orders order){
        this.orders.add(order);
    }
}
