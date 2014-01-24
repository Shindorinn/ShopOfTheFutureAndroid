package nl.futureworks.shopofthefuture.sqlite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	private int dbVersion;
	private String dbName;
	
	private Context context;
	
	private static DatabaseHandler db;
	     
	private DatabaseHandler(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		
		this.dbName = dbName;
		this.dbVersion = dbVersion;
		this.context = context;
	}
	
	public static DatabaseHandler getInstance(Context context, String dbName, int dbVersion){
		if (db == null){
			dbName = (dbName+".s3db");
			db = new DatabaseHandler(context, dbName, dbVersion);
			return db;
		}
		else{
			return db;
		}
	}
	
	/**
	 * Creates, if not already exists, tables rides and joins
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		try{
			Log.d("Sqlite Database", "Creating DB");
					
			database.execSQL("" +
			"CREATE TABLE user " +
			"( " +
			"id INT NOT NULL PRIMARY KEY, " +
			"name VARCHAR(255)NOT NULL , " +
			"email VARCHAR(255) NOT NULL, " +
			"telnr NUMERIC(50) NOT NULL, " +
			"password VARCHAR(50) NOT NULL " +
			");");
			
			database.execSQL("" +
			"CREATE TABLE shoppinglist " +
			"( " +
			"id INT NOT NULL PRIMARY KEY, " +
			"user_id INT NOT NULL, " +
			"name VARCHAR(255) NOT NULL, " +
			
			"FOREIGN KEY(user_id) REFERENCES user(id) " +
			");");
			
			database.execSQL("" +
			"CREATE TABLE item " +
			"( " +
			"barcode VARCHAR(255) NOT NULL PRIMARY KEY, " +
			"name VARCHAR(255) NOT NULL, " +
			"price DOUBLE NOT NULL " +
			");");
			
			database.execSQL("" +
			"CREATE TABLE shoppinglistitem " +
			"( " +
			"shoppinglist_id INT NOT NULL, " +
			"barcode VARCHAR(255) NOT NULL, " +
			"quantity INT NOT NULL, " +
			
			"PRIMARY KEY (shoppinglist_id, barcode), " +
			"CONSTRAINT shoppinglistitem_fk_shoppinglistid FOREIGN KEY (shoppinglist_id) REFERENCES shoppinglist(id), " +
			"CONSTRAINT shoppinglistitem_fk_barcode FOREIGN KEY (barcode) REFERENCES item(barcode) " +
			");");
			
			Log.d("Sqlite Database", "Database created");
		}
		catch (SQLException e){
			Log.e("Sqlite Database", "Cannot create database: " + e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		return;	
	}
	
	/**
	 * Executes a SQL query without returning a result (For Inserts, Deletes and Updates)
	 * 
	 * @param query, SQL query to be executed
	 */
	public void executeQuery(String query){
		try{
			SQLiteDatabase database = this.getReadableDatabase();
			database.execSQL(query);
		}
		
		catch(SQLException e){
			Log.e("Sqlite Database", "Query was not succesful: " + e);
		}
	}
	
	/**
	 * Checks if database file is created
	 * 
	 * @return boolean, dbFileExists
	 */
	public boolean databaseExists() {
		 File dbFile = context.getDatabasePath(dbName);
		 return dbFile.exists();
	}
	
	/**
	 * Returns the name of the sqlite database (Also works for API 14 and lower)
	 * @return String, dbName
	 */
	public String getDBName(){
		return dbName;	
	}
	
	/**
	 * Returns the version of the database
	 * @return int, dbVersion
	 */
	public int getDBVersion(){
		return dbVersion;
	}
	
	/**
	 * Send a query to the database which returns a List with results from the database or null when no results are found
	 * When a parameter is not needed, use null instead
	 * 
	 * @param table, table where query is executed
	 * @param colomNames, String array with used coloms
	 * @param where, where clause
	 * @param selection, selection clause, joins etc
	 * @param groupBy, group by clause
	 * @param having, having clause
	 * @param orderBy, order by colom
	 * @param limit, limit results
	 * 
	 * @return List<HashMap<String colomName, String colomValue>>, null if no results were found
	 */
	public List<HashMap<String, String>> sendQuery(String table, String[] colomNames, String where, String[] selection, String groupBy, String having, String orderBy, String limit){
		List<HashMap<String, String>> resultArray = new ArrayList<HashMap<String, String>>();
		
		try{
			SQLiteDatabase database = this.getReadableDatabase();
			Cursor recordSet = database.query(table, colomNames, where, selection, groupBy, having, orderBy, limit);
			
			
			
			while (recordSet.moveToNext()){
				HashMap<String, String> rowMap = new HashMap<String, String>();
				
				for (int i=0; i < recordSet.getColumnCount(); i++){
					rowMap.put(recordSet.getColumnName(i), recordSet.getString(i));
				}
				
				resultArray.add(rowMap);
			}
		}
		
		catch(SQLException e){
			Log.e("Sqlite Database", "Query was not succesful: " + e);
		}
		
		if (resultArray.isEmpty()){
			return null;
			
		} 
		else{
			return resultArray;
		}
		
	}

}
