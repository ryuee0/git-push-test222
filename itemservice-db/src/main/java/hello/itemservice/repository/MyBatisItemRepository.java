package hello.itemservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.mybatis.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository{
	private final ItemMapper itemMapper;

	@Override
	public Item save(Item item) {
		log.info("itemMapper ={}", itemMapper.getClass());
		itemMapper.save(item);
		return item;
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		itemMapper.update(itemId, updateParam);
	}

	@Override
	public Optional<Item> findById(Long id) {
		return itemMapper.findById(id);
	}

	@Override
	public List<Item> findAll(ItemSearchCond cond) {
		
		return itemMapper.findAll(cond);
	}
	
}
