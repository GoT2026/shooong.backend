package io.rapa.shooongbackend.order.repository;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByMember(Members member);

    default Orders findByIdOrThrow(Long Orderid){
        return findById(Orderid).orElseThrow(
                ()-> new CustomException(ErrorCode.ORDER_NOT_FOUND)
        );
    }
}
