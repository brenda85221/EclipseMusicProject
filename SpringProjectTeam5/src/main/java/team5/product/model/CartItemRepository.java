package team5.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemBean, CartItemId> {
    List<CartItemBean> findByIdAcct(String acct);
    void deleteByIdAcct(String acct);
}
