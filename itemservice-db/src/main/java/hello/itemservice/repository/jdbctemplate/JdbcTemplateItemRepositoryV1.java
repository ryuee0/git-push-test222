package hello.itemservice.repository.jdbctemplate;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository{
	private final JdbcTemplate template;
	
	public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
// itemName 	
	@Override
	public Item save(Item item) {
		String sql = "insert into item(item_name, price, quantity) values (?, ?, ?)"; 
		
		// h2 데이터베이스 특징
		// pk생성 시 identity(=auto increment)방식을 사용하기 때문에
		// 개발자가 집접 pk값을 지정하지 않고, 비워두고 쿼리를 저장해야 함
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		// 자동키 증가
		template.update(con -> { // 테이블에 Insert, update, Delete 시에 사용
			PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
			ps.setString(1, item.getItemName());
			ps.setInt(2, item.getPrice());
			ps.setInt(3, item.getQuantity());
			return ps;
		}, keyHolder);
		
		long key = keyHolder.getKey().longValue();
		item.setId(key);
		
		return item;
	}

	@Override
	public void update(Long itemId, ItemUpdateDto updateParam) {
		String sql = "update item set item_name= ? , price = ?, quantity = ? where id = ?";
		template.update(sql, 
						updateParam.getItemName(),
						updateParam.getPrice(),
						updateParam.getQuantity(),
						itemId
						);
		
	}

	@Override
	public Optional<Item> findById(Long id) {
		String sql = "select id, item_name , price , quantity  from item where id = ?";
		
		try {
			Item item = template.queryForObject(sql, itemRowMapper(),id);
			return Optional.of(item);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty(); 
			// Opotional 객체 특징
			// 쿼리 결과값(ResultSet 객체)이 없는 경우, 
			// NullPointer exception 발생하지 않도록 Null 처리해주는 empty 메서드 등 제공
		}
	}

	@Override // 검색기능 (검색어 입력 없이 조회하는 경우, 전체 리스트 조회됨)
	public List<Item> findAll(ItemSearchCond cond) {
		String itemName = cond.getItemName();
		Integer maxPrice = cond.getMaxPrice();
		
		String sql = "select id, item_name , price , quantity  from item ";
		
		// 동적쿼리
		if(StringUtils.hasText(itemName) || maxPrice != null) {
			sql += "where";
		}
		
		boolean andFlag = false;
		
		List<Object> param = new ArrayList<>();
		if(StringUtils.hasText(itemName)) {
			sql +=  " item_name like concat('%', ? , '%')";
			param.add(itemName);
			andFlag = true;
		}
		
		if(maxPrice != null) {
			if(andFlag) {
				sql += " and";
			}
			
			sql += " price <= ?";
			param.add(maxPrice);
		}
		
		log.info("sql={}", sql);
		
		return template.query(sql, itemRowMapper(), param.toArray());
	}
	
	
	private RowMapper<Item> itemRowMapper(){
		
		return (rs, rowNum) -> {
			Item item = new Item();
			item.setId(rs.getLong("id"));
			item.setItemName(rs.getString("item_name"));
			item.setPrice(rs.getInt("price"));
			item.setQuantity(rs.getInt("quantity"));
			
			return item;
		};
	}
	
}








