package edu.mirea.candy_shop.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "cart_id")
    private CustomerEntity customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id")
    private Set<CartProductEntity> productsInCart = new HashSet<>();

    public CartEntity(Long cartId, CustomerEntity customer) {
        this.cartId = cartId;
        this.customer = customer;
    }
}