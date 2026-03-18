package team5.order.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import team5.product.model.CartItemBean;
import team5.product.model.CartItemRepository;
import team5.product.model.ProductBean;
import team5.product.model.ProductRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository oRepos;

    @Autowired
    private OrderDetailRepository odRepos;
    
    @Autowired
    private CartItemRepository cartRepos;

    public List<OrderDTO> getAllOrder() {
        List<OrderBean> orders = oRepos.findAll();
        return orders.stream()
                     .map(OrderDTO::new)  // 使用 OrderDTO 的建構子轉換實體
                     .collect(Collectors.toList());
    }

    public OrderBean getOrderById(Integer orderId) {
        Optional<OrderBean> order = oRepos.findById(orderId);
        return order.orElse(null);
    }

    public void deleteOrder(Integer orderId) {
        oRepos.deleteById(orderId);
    }
    
    //前台用
    public List<OrderDTO> seleteByAcct(String acct) {
        List<OrderBean> orders = oRepos.findByAcct(acct);
        return orders.stream()
                     .map(OrderDTO::new)  // 使用 OrderDTO 的建構子轉換實體
                     .collect(Collectors.toList());
    }
    
 // 更新訂單
    public OrderDTO updateOrder(int orderID, OrderDTO updatedOrder) {
        Optional<OrderBean> existingOrderOpt = oRepos.findById(orderID);

        if (existingOrderOpt.isPresent()) {
            OrderBean order = existingOrderOpt.get();
            order.setOrderState(updatedOrder.getOrderState());
            order.setPaymentStatus(updatedOrder.getPaymentStatus());
            order.setCouponID(updatedOrder.getCouponID());
            order.setOrderDiscountValue(updatedOrder.getOrderDiscountValue());
            order.setTotalAmount(updatedOrder.getTotalAmount());

            oRepos.save(order);
            return new OrderDTO(order);
        } else {
            return null; // 訂單不存在
        }
    }
    
    @Transactional
    public OrderBean createOrder(String acct) {
        List<CartItemBean> cartItems = cartRepos.findByIdAcct(acct);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("購物車中沒有商品");
        }

        double totalAmount = cartItems.stream()
            .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
            .sum();

        OrderBean order = new OrderBean();
        order.setAcct(acct);
        order.setTotalAmount(totalAmount);
        order.setOrderState("未完成");
        order.setPaymentStatus("未付款");

        // ✅ 先存入 OrderBean，確保 orderID 生成
        order = oRepos.save(order);

        // ✅ 確保 OrderBean 先存入後，再建立 OrderDetailBean
        for (CartItemBean cartItem : cartItems) {
            OrderDetailBean orderDetail = new OrderDetailBean(
                order, // ✅ 直接傳入已存入的 OrderBean
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
            );
            odRepos.save(orderDetail);
        }

        // 清空購物車
        cartRepos.deleteByIdAcct(acct);
//        cartRepos.deleteAll(cartItems);
        System.err.println("hiiiiiii");

        return order;
    }


    



    
    
}

