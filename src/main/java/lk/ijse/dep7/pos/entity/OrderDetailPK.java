package lk.ijse.dep7.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderDetailPK implements Serializable {
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "item_code")
    private String itemCode;


}
