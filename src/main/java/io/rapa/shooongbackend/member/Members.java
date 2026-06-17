package io.rapa.shooongbackend.member;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.rapa.shooongbackend.common.entity.BaseEntity;
import io.rapa.shooongbackend.order.Orders;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Members extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<>();

    @Builder
    public Members(
            String name,
            String userId,
            String password
    ) {
        this.name = name;
        this.userId = userId;
        this.password = password;
    }

    public void addOrder(Orders order){
        this.orders.add(order);
    }
}
