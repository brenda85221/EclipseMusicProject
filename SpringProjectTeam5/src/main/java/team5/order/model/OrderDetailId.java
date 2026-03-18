package team5.order.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderDetailId implements Serializable{
	private int orderID;
    private int productID;

 // equals 和 hashCode 必须實現??
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailId that = (OrderDetailId) o;
        return orderID == that.orderID && productID == that.productID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, productID);
    }

}
