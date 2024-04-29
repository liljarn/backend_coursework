package edu.mirea.candy_shop.dao.repository;

import edu.mirea.candy_shop.dao.entity.CartProductEntity;
import edu.mirea.candy_shop.dao.entity.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, CartProductId> {
}
