package io.rapa.shooongbackend.order.repository;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.order.entity.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByMember(Members member);
    List<Orders> findByScoreIsNotNullOrderByScoreDescTotalFlightTimeAsc(Pageable pageable);

    @Query("""
            select count(o)
            from Orders o
            where o.score is not null
              and (
                    o.score > :score
                    or (o.score = :score and o.totalFlightTime < :totalFlightTime)
              )
            """)
    Long countRankingAhead(
            @Param("score") Double score,
            @Param("totalFlightTime") Long totalFlightTime
    );

    default Orders findByIdOrThrow(Long Orderid){
        return findById(Orderid).orElseThrow(
                ()-> new CustomException(ErrorCode.ORDER_NOT_FOUND)
        );
    }
}
