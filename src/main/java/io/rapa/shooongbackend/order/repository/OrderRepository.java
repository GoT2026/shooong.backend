package io.rapa.shooongbackend.order.repository;

import io.rapa.shooongbackend.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
