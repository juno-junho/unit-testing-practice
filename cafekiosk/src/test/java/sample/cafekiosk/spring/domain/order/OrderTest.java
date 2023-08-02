package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // Given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );
        // When
        Order order = Order.create(products, LocalDateTime.now());
        // Then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다")
    @Test
    void init() {
        // Given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );
        // When
        Order order = Order.create(products, LocalDateTime.now());
        // Then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }


    @DisplayName("주문 생성 시 등록 시간을 기록 한다.")
    @Test
    void registerDateTime() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // When
        Order order = Order.create(products, registeredDateTime);

        // Then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }


    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}
