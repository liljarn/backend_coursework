package edu.mirea.candy_shop.dao.entity;

import edu.mirea.candy_shop.dao.entity.link_tables.OrderProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    private long price = 0L;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderProductEntity> productInOrder = new ArrayList<>();

    public OrderEntity(CustomerEntity customer, long price, List<OrderProductEntity> productInOrder) {
        this.customer = customer;
        this.price = price;
        this.productInOrder = productInOrder;
    }
}
