package hello.itemservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity // JPA가 사용하는 객체를 뜻함
public class Item {

	@Id // 테이블에 PK와 매핑함 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	// 객체의 필드를 테이블의 컬럼과 매핑
	@Column(name = "item_name", length = 10)
	private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
