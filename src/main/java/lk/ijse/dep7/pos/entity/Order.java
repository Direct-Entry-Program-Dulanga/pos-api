package lk.ijse.dep7.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@Table(name = "`order`")
@Entity
public class Order implements SuperEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false )
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;


    public Order(String id, Date date, Customer customer, Set<OrderDetail>  orderDetails) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.orderDetails = orderDetails;
        this.orderDetails.forEach(od -> od.getOrderDetailPK().setOrderId(id));
    }

}
