package nl.futureworks.shopofthefuture.test;

import java.util.HashMap;
import java.util.Map.Entry;

import nl.futureworks.shopofthefuture.domain.ShoppingCart;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import android.test.AndroidTestCase;

public class ShoppingCartAndItemTest extends AndroidTestCase {
	private ShoppingCart cart;
	ShoppingListItem item1;
	ShoppingListItem item2;
	
	public ShoppingCartAndItemTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		item1 = new ShoppingListItem("05012834", "Bread", 1.10);
		item2 = new ShoppingListItem("05018273", "Cookie", 1.50);
		
		HashMap<ShoppingListItem, Integer> map = new HashMap<ShoppingListItem, Integer>();
		map.put(item1, 2);
		ShoppingList list = new ShoppingList(1, 1, "TestList", map);
		cart = list.convertToCart();
	}
	
	public void testAddItem() {
		cart.addItem(item2, 5);
		
		assertTrue("ShoppingList should have two entries", cart.getItems().size() == 2);
		assertTrue("Item2 should exist in ShoppingList", cart.itemExists(item2));
		
		//Test wrong input
		cart.addItem(null, 5);
		assertFalse("Item should not be added to the ShoppingList", cart.getItems().size() == 3);
		
		cart.addItem(item2, -1);
		assertFalse("Item should not be added to the ShoppingList", cart.getItems().size() == 3);
	}
	
	public void testChangeAmount() {
		HashMap<ShoppingListItem, Integer> items = cart.getItems();
		for(Entry<ShoppingListItem, Integer> en: items.entrySet()){
			cart.changeAmount(en.getKey(), 10);
			assertTrue("New amount should be 10", cart.getItemAmount(en.getKey()) == 10);
		}
		
		//Test wrong input
		cart.changeAmount(item1, 0);
		assertTrue("Amount should be unchanged", cart.getItemAmount(item1) == 10);
		cart.changeAmount(null, 5);
		assertTrue("Amount should be unchanged", cart.getItemAmount(item1) == 10);
	}
	
	public void testRemoveItem() {
		cart.removeItem(item1);
		assertFalse("Item1 should be removed", cart.itemExists(item1));
		
		//Test wrong input
		cart.removeItem(null);
	}
	
	public void testModifyItem() {
		item1.setName("Kaas");
		item1.setPrice(20.25);
		
		cart.removeItem(item1);
		assertFalse("Item should have been removed even after modifying", cart.itemExists(item1));
	}
	
}
