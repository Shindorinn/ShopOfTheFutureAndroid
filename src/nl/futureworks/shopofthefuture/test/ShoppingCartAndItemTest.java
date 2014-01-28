package nl.futureworks.shopofthefuture.test;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import nl.futureworks.shopofthefuture.domain.ShoppingCart;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import nl.futureworks.shopofthefuture.exception.ShoppingListModificationException;
import android.test.AndroidTestCase;
import android.util.Log;

public class ShoppingCartAndItemTest extends AndroidTestCase {
	private ShoppingCart cart;
	private final ShoppingListItem item1;
	private final ShoppingListItem item2;
	
	public ShoppingCartAndItemTest() {
		super();
		item1 = new ShoppingListItem("05012834", "Bread", 1.10);
		item2 = new ShoppingListItem("05018273", "Cookie", 1.50);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		ConcurrentHashMap<ShoppingListItem, Integer> map = new ConcurrentHashMap<ShoppingListItem, Integer>();
		map.put(item1, 2);
		ShoppingList list = new ShoppingList(1, 1, "TestList", map);
		cart = list.convertToCart();
	}
	
	public void testAddItem() {
		try {
			cart.addItem(item2, 5);
		} catch (ShoppingListModificationException e) {
			e.printStackTrace();
		}
		Log.wtf("plspls3", item2.toString());
		
		assertTrue("ShoppingList should have two entries", cart.getItems().size() == 2);
		assertTrue("Item2 should exist in ShoppingList", cart.itemExists(item2));
			
		//Test wrong input
		try {
			cart.addItem(null, 5);
		} catch (ShoppingListModificationException e) {
			e.printStackTrace();
		}
		
		assertFalse("Item should not be added to the ShoppingList", cart.getItems().size() == 3);
			
		try {
			cart.addItem(item1, -1);
		} catch (ShoppingListModificationException e) {
			e.printStackTrace();
		}
		
		assertFalse("Item should not be added to the ShoppingList", cart.getItems().size() == 3);
	}
	
	public void testChangeAmount() {
		try {
			ConcurrentHashMap<ShoppingListItem, Integer> items = cart.getItems();
			for(Entry<ShoppingListItem, Integer> en: items.entrySet()){
				cart.changeAmount(en.getKey(), 10);
				assertTrue("New amount should be 10", cart.getItemAmount(en.getKey()) == 10);
			}
		} catch (ShoppingListModificationException e) {
			e.printStackTrace();
		}
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
