package nl.futureworks.shopofthefuture.test;

import java.util.HashMap;
import java.util.Map.Entry;

import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import android.test.AndroidTestCase;

public class ShoppingListAndItemTest extends AndroidTestCase {
	private ShoppingList list;
	ShoppingListItem item1;
	ShoppingListItem item2;
	
	public ShoppingListAndItemTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		item1 = new ShoppingListItem("05012834", "Bread", 1.10);
		item2 = new ShoppingListItem("05018273", "Cookie", 1.50);
		
		HashMap<ShoppingListItem, Integer> map = new HashMap<ShoppingListItem, Integer>();
		map.put(item1, 2);
		list = new ShoppingList(1, 1, "TestList", map);
	}
	
	public void testAddItem() {
		list.addItem(item2, 5);
		
		assertTrue("ShoppingList should have two entries", list.getItems().size() == 2);
		assertTrue("Item2 should exist in ShoppingList", list.itemExists(item2));
		
		//Test wrong input
		list.addItem(null, 5);
		assertFalse("Item should not be added to the ShoppingList", list.getItems().size() == 3);
		
		list.addItem(item2, -1);
		assertFalse("Item should not be added to the ShoppingList", list.getItems().size() == 3);
	}
	
	public void testChangeAmount() {
		HashMap<ShoppingListItem, Integer> items = list.getItems();
		for(Entry<ShoppingListItem, Integer> en: items.entrySet()){
			list.changeAmount(en.getKey(), 10);
			assertTrue("New amount should be 10", list.getItemAmount(en.getKey()) == 10);
		}
		
		//Test wrong input
		list.changeAmount(item1, 0);
		assertTrue("Amount should be unchanged", list.getItemAmount(item1) == 10);
		list.changeAmount(null, 5);
		assertTrue("Amount should be unchanged", list.getItemAmount(item1) == 10);
	}
	
	public void testRemoveItem() {
		list.removeItem(item1);
		assertFalse("Item1 should be removed", list.itemExists(item1));
		
		//Test wrong input
		list.removeItem(null);
	}
	
	public void testModifyItem() {
		item1.setName("Kaas");
		item1.setPrice(20.25);
		
		list.removeItem(item1);
		assertFalse("Item should have been removed even after modifying", list.itemExists(item1));
	}
	
}
