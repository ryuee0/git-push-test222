package hello.itemservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.SpringDataJpaItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {
	
	private final SpringDataJpaItemRepository repository;
	
	@Bean
	public ItemService itemService() {
		return new ItemServiceV1(itemRepository());
	}
	
	
	@Bean
	public ItemRepository itemRepository() {
		return new JpaItemRepositoryV2(repository);
	}
	
}
