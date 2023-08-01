package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * SELECT *
     * FROM PRODUCT
     * WHERE SELLING_STATUS IN ('SELLING', 'HOLD')
     *
     * 제대로된 쿼리가 날라갈 것인가에 대한 테스트
     * 미래에 어떻게 변화될지 모른다
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
}

