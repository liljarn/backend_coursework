package edu.mirea.candy_shop.dto;

import java.util.List;

public record OrderDto(List<OrderProductDto> orderProduct, long price) {
}
