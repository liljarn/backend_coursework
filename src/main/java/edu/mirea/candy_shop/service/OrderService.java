package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CartEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.entity.OrderEntity;
import edu.mirea.candy_shop.dao.entity.link_tables.OrderProductEntity;
import edu.mirea.candy_shop.dao.entity.link_tables.OrderProductId;
import edu.mirea.candy_shop.dao.repository.CartProductRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.OrderDto;
import edu.mirea.candy_shop.dto.OrderProductDto;
import edu.mirea.candy_shop.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CartProductRepository cartProductRepository;
    private final PictureService pictureService;

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        return customer.getOrders()
                .stream()
                .map(order -> new OrderDto(
                        order.getProductInOrder()
                                .stream()
                                .map(orderProduct ->
                                        new OrderProductDto(
                                                orderProduct.getProduct().getProductName(),
                                                orderProduct.getProduct().getPrice() * orderProduct.getQuantity(),
                                                orderProduct.getQuantity(),
                                                URI.create(pictureService.getLinkOnPicture(
                                                        orderProduct.getProduct().getProductName()
                                                )))
                                )
                                .toList(),
                        order.getPrice())
                ).toList();
    }

    @Transactional
    public void makeOrder(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CartEntity cart = customer.getCart();
        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        customer.getOrders().add(order);
        order.setProductInOrder(cart.getProductsInCart().stream().map(entity -> {
            OrderProductEntity orderProduct = new OrderProductEntity(new OrderProductId(
                    order.getOrderId(), entity.getProduct().getProductId()
            ));
            orderProduct.setProduct(entity.getProduct());
            orderProduct.setOrder(order);
            orderProduct.setQuantity(entity.getQuantity());
            entity.getProduct().getOrders().add(orderProduct);
            order.getProductInOrder().add(orderProduct);
            order.setPrice(order.getPrice() + entity.getQuantity() * entity.getProduct().getPrice());
            cartProductRepository.delete(entity);
            return orderProduct;
        }).toList());
    }

}
