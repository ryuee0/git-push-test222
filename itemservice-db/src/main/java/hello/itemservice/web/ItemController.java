package hello.itemservice.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
	
	@GetMapping
	public String items(@ModelAttribute("itemSearch") ItemSearchCond itemSearch, Model model) {
		List<Item> items = itemService.findItems(itemSearch);
		model.addAttribute("items", items);
		
		return "items";
	}
	
	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") long itemId, Model model) {
		Item item = itemService.findById(itemId).get();
		model.addAttribute("item", item);
		
		return "item";
	}
	
	@GetMapping("/add")
	public String addForm() {
		return "addForm";
	}
	
	@PostMapping("/add")
	public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemService.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		
		return "redirect:/items/{itemId}";
	}
	
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemService.findById(itemId).get();
		model.addAttribute("item", item);
		return "editForm";
	}
	
	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable("itemId") Long itemId, 
			@ModelAttribute("itemUpdateDto") ItemUpdateDto updateParam) {
		
		itemService.update(itemId, updateParam);
		
		return "redirect:/items/{itemId}"; 
	}
	
	
}







































