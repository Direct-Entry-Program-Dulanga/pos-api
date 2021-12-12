package lk.ijse.dep7.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "item")
@Entity
public class Item implements SuperEntity {

    @Id
    private String code;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private int qtyOnHand;

}
