package hello.itemservice.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV2Config {
	private final DataSource dataSource;
	
	@Bean
	public ItemService itemService() {
		return new ItemServiceV1(itemRepository());
	}
	
	@Bean
	public ItemRepository itemRepository() {
		return new JdbcTemplateItemRepositoryV2(dataSource);
	}
	
	// ItemRepository 구현체로 JdbcTemplateRepositoryV1이 사용됨
	
	
}












