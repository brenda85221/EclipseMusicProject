package team5.order.model;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailBean, OrderDetailId> {
	@Transactional
    void deleteAllByOrder(OrderBean order); // 根據 OrderBean 刪除所有 OrderDetail
}
