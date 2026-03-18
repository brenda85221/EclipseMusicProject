package team5.order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<OrderBean, Integer> {
	List<OrderBean> findByAcct(String acct);
}
