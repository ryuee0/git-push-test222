package hello.itemservice.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.itemservice.domain.Item;

// 스프링데이터 jpa가 제공하는 jpaRepository 인터페이스를 상속받아 기본적인 crud 기능 사용가능

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByItemNameLike(String itemName);
	
	List<Item> findByPriceLessThanEqual(Integer price);
	
	// 쿼리메서드 
	List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);
	
	// 쿼리 직접 작성
	@Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
	List<Item> findItems(@Param("itemName")String itemName, @Param("price") Integer price);
}
