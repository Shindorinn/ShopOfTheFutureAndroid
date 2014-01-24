package nl.futureworks.shopofthefuture.test;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.test.AndroidTestCase;
import nl.futureworks.shopofthefuture.sqlite.DatabaseHandler;

public class DBTest extends AndroidTestCase {
	private static final String DB_NAME = "shopofthefuturetest";
	private static final int DB_VERSION = 1;
	
	private Context context = null;
	private DatabaseHandler db = null;
	
	public DBTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		context = this.getContext();
		db = DatabaseHandler.getInstance(context, DB_NAME, DB_VERSION);
	}
	
	public void testDatabaseCreation() { 
		assertFalse("Context should not be null", context == null);
		assertFalse("Database should not be null", db == null);
		
		//Ensure db is created (Lazy initialization)
		db.sendQuery("user", null, null, null, null, null, null, null);
		assertTrue("Database should exist", db.databaseExists());
	}
	
	public void testDatabaseInsertion() { 
		assertFalse("Database test", db == null);
		
		db.executeQuery("INSERT INTO user VALUES ('" + 1 + "', 'Arjan', 'arjan_strijland@hotmail.com', '0611739622', '1234')");
		List<HashMap<String, String>> list = db.sendQuery("user", null, null, null, null, null, null, null);
		
		assertTrue("Size should be 1", list.size() == 1);
		
		String name = list.get(0).get("name");
		String email = list.get(0).get("email");
		
		assertTrue("Name should be 'Arjan'", name.equals("Arjan"));
		assertTrue("E-mail should be 'arjan_strijland@hotmail.com'", email.equals("arjan_strijland@hotmail.com"));
	} 
	
	public void testDatabaseWhereClause() {
		db.executeQuery("INSERT INTO shoppinglist VALUES ('" + 1 + "', '1', 'Shoppinglist1')");
		List<HashMap<String, String>> list = db.sendQuery("shoppinglist", null, "user_id = '1'", null, null, null, null, null);
		
		assertTrue("Size should be 1", list.size() == 1);
		
		String id = list.get(0).get("id");
		String name = list.get(0).get("name");
		
		assertTrue("ID should be '1'", id.equals("1"));
		assertTrue("Name should be 'Shoppinglist1'", name.equals("Shoppinglist1"));
	}
	
	public void testDatabaseDeletion() { 
		assertTrue("DatabaseList should exist", db.databaseExists());
		
		String name = db.getDBName();
		context.deleteDatabase(name);
		
		assertFalse("DatabaseList should not exist anymore", db.databaseExists());
		
		db.close();
	}

}
