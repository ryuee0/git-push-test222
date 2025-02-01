package hello.itemservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import hello.itemservice.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV1 implements ItemRepository {
	private final EntityManager em;
	
	public JpaItemRepositoryV1(EntityManager em) {
		this.em = em;
	}

	@Override
	public Item save(Item item) {
		em.persist(item);
		// JPA에서 객체를 테이블에 저장할때는 EntityManager 객체가 제공하는 persist()사용할 것
		return item;
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		Item findItem = em.find(Item.class, itemId);
		findItem.setItemName(updateParam.getItemName());
		findItem.setPrice(updateParam.getPrice());
		findItem.setQuantity(updateParam.getQuantity());
	}

	@Override
	public Optional<Item> findById(Long id) {
		Item item = em.find(Item.class, id);
		return Optional.ofNullable(item);
	}

	@Override
	public List<Item> findAll(ItemSearchCond cond) {
		String jpql = "select i from Item as i";
		                
		Integer maxPrice = cond.getMaxPrice();
		String itemName = cond.getItemName();
		
		if(StringUtils.hasText(itemName) || maxPrice != null) {
			jpql += " where";
		}
		
		boolean andFlag = false;
		
		if(StringUtils.hasText(itemName)) {
			jpql += " i.itemName like concat('%', :itemName ,'%')";
			andFlag = true;
		}
		
		if (maxPrice != null) {
			if(andFlag) {
				jpql += " and";
			}
			
			jpql += " i.price <= :maxPrice";
		}
		
		log.info("jpql={}" , jpql);
		
		TypedQuery<Item> query = em.createQuery(jpql, Item.class);
		
		if(StringUtils.hasText(itemName)) {
			query.setParameter("itemName", itemName);
		}
		
		if(maxPrice != null) {
			query.setParameter("maxPrice", maxPrice);
		}
		
		return query.getResultList();
	}
	
}



















