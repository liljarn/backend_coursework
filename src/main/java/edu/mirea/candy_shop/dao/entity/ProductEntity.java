package edu.mirea.candy_shop.dao.entity;

import edu.mirea.candy_shop.dto.ProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    private long amount = 0;

    public ProductEntity(String productName, String description, int price, long amount) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
}
