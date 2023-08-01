package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

//@SpringBootTest // 하지만 이것을 더 선호한다
@ActiveProfiles("test")
@DataJpaTest // springboottest보다 가볍다. jpa 관련 bean들만 띄운다
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다")
    @Test
    void findAllBySellingStatusIn() {
        // Given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("개밥" )
                .price(10000)
                .build();
        repository.saveAll(List.of(product1, product2, product3));

        // When
        List<Product> products = repository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // Then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
}