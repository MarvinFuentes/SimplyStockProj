package com.example.simplystockproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simply_stock.db";
    private static final int DATABASE_VERSION = 1;

    //First version of the Users table with its corresponding columns.
    public static final String TABLE_USERS = "users";
    public static final String ID_COL = "id";
    public static final String FIRSTNAME_COL = "first_name";
    public static final String LASTNAME_COL = "last_name";
    public static final String EMAIL_COL = "email";
    public static final String PASSWORD_COL = "password";

    //First version of the Organization table with it's corresponding columns
    public static final String TABLE_ORGANIZATIONS = "organizations";
    public static final String BUSINESS_ID_COL = "business_id";
    public static final String OWNER_ID_COL = "owner_id";
    public static final String BUSINESS_NAME_COL = "business_name";
    public static final String PIN_COL = "pin";
    public static final String MANAGER_PIN_COL = "manager_pin";
    public static final String CITY_COL = "city";
    public static final String STATE_COL = "state";

    //First version of the Items table with it's corresponding columns.
    public static  final  String TABLE_ITEMS = "items";
    public static  final  String ITEM_ID_COL = "item_id";
    public static  final  String ITEM_BUSINESS_ID_COL = "business_id";
    public static  final  String ITEM_DESCRIPTION_COL = "description";
    public static  final  String ITEM_URL_COL = "url";
    public static  final  String ITEM_CATEGORY_COL = "category";
    public static  final  String ITEM_IMG_URI_COL = "image_uri";
    public static  final String ITEM_AVAILABILITY_COL = "availability";
    public static final String ITEM_LOW_STOCK = "low_stock";

    //First version of the Checkout table with it's corresponding columns.
    public static final String TABLE_CHECKOUT = "checkouts";
    public static final String CHECKOUT_ID_COL = "checkout_id";
    public static final String CHECKOUT_USER_ID_COL = "user_id";
    public static final String CHECKOUT_BUSINESS_ID_COL = "business_id";
    public static final String CHECKOUT_ITEM_ID_COL = "item_id";
    public static final String CHECKOUT_QUANTITY_COL = "quantity";
    public static final String CHECKOUT_DATE_COL = "date";


    public Database(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table for users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIRSTNAME_COL + " TEXT NOT NULL, "
                + LASTNAME_COL + " TEXT NOT NULL, "
                + EMAIL_COL + " TEXT UNIQUE NOT NULL, "
                + PASSWORD_COL + " TEXT NOT NULL"
                + ");";

        // Table for businesses
        String createBusinessTable = "CREATE TABLE " + TABLE_ORGANIZATIONS + " ("
                + BUSINESS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OWNER_ID_COL + " INTEGER NOT NULL, "
                + BUSINESS_NAME_COL + " TEXT NOT NULL, "
                + PIN_COL + " TEXT NOT NULL, "
                + MANAGER_PIN_COL + " TEXT NOT NULL, "
                + CITY_COL + " TEXT NOT NULL, "
                + STATE_COL + " TEXT NOT NULL"
                + ");";

        //Table for items
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " ("
                + ITEM_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_BUSINESS_ID_COL + " INTEGER NOT NULL, "
                + ITEM_DESCRIPTION_COL + " TEXT NOT NULL, "
                + ITEM_URL_COL + " TEXT, "
                + ITEM_CATEGORY_COL + " TEXT NOT NULL, "
                + ITEM_IMG_URI_COL + " TEXT, "
                + ITEM_AVAILABILITY_COL + " INTEGER NOT NULL, "
                + ITEM_LOW_STOCK + " INTEGER NOT NULL"
                + ");";

        //Table for checkouts
        String createCheckoutsTable = "CREATE TABLE " + TABLE_CHECKOUT + " ("
                + CHECKOUT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CHECKOUT_USER_ID_COL + " INTEGER NOT NULL, "
                + CHECKOUT_BUSINESS_ID_COL + " INTEGER NOT NULL, "
                + CHECKOUT_ITEM_ID_COL + " INTEGER NOT NULL, "
                + CHECKOUT_QUANTITY_COL + " INTEGER NOT NULL, "
                + CHECKOUT_DATE_COL + " DATE"
                + ");";

        // Execute all create table statements
        db.execSQL(createUsersTable);
        db.execSQL(createBusinessTable);
        db.execSQL(createItemsTable);
        db.execSQL(createCheckoutsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORGANIZATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKOUT);
        onCreate(db);
    }

    public User getUserByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{ID_COL, FIRSTNAME_COL, LASTNAME_COL, EMAIL_COL, PASSWORD_COL},
                EMAIL_COL + "=?", new String[]{email}, null, null, null);

        User user = null;

        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL));
            String e = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COL));
            String fN = cursor.getString(cursor.getColumnIndexOrThrow(FIRSTNAME_COL));
            String lN = cursor.getString(cursor.getColumnIndexOrThrow(LASTNAME_COL));
            String p = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_COL));
            user = new User(id, fN, lN, e, p);
        }
        cursor.close();
        return user;
    }

    public boolean ifEmailExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{ID_COL}, EMAIL_COL + "=?", new String[]{email},
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean createNewUser(String firstName, String lastName, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIRSTNAME_COL, firstName);
        values.put(LASTNAME_COL, lastName);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean createNewBusiness(int ownerId, String businessName, String pin, String managerPin, String city, String state){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OWNER_ID_COL, ownerId);
        values.put(BUSINESS_NAME_COL, businessName);
        values.put(PIN_COL, pin);
        values.put(MANAGER_PIN_COL, managerPin);
        values.put(CITY_COL, city);
        values.put(STATE_COL, state);

        long result = db.insert(TABLE_ORGANIZATIONS, null, values);
        return result != -1;
    }

    public boolean createNewItem(int businessId, String description, String url,  String category, String imgUri, int availability, int lowStock){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_BUSINESS_ID_COL, businessId);
        values.put(ITEM_DESCRIPTION_COL, description);
        values.put(ITEM_URL_COL, url);
        values.put(ITEM_CATEGORY_COL, category);
        values.put(ITEM_IMG_URI_COL, imgUri);
        values.put(ITEM_AVAILABILITY_COL, availability);
        values.put(ITEM_LOW_STOCK, lowStock);

        long result = db.insert(TABLE_ITEMS, null, values);
        return  result != -1;
    }

    public boolean createCheckout(int userId, int businessId, int itemId, int quantity, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CHECKOUT_USER_ID_COL, userId);
        values.put(CHECKOUT_BUSINESS_ID_COL, businessId);
        values.put(CHECKOUT_ITEM_ID_COL, itemId);
        values.put(CHECKOUT_QUANTITY_COL, quantity);
        values.put(CHECKOUT_DATE_COL, date);

        long result = db.insert(TABLE_CHECKOUT, null, values);
        return result != -1;
    }

    public boolean updateItemAvailability(int itemId, int newAvailability){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_AVAILABILITY_COL, newAvailability);

        int rowsUpdated = db.update(TABLE_ITEMS, values, ITEM_ID_COL + "=?", new String []{String.valueOf(itemId)});
        return rowsUpdated > 0;
    }

    public boolean updatePassword(String email, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PASSWORD_COL, newPassword);

        int rows = db.update(TABLE_USERS, values, EMAIL_COL + "=?", new String[]{email});
        return rows > 0;
    }

    public boolean deleteUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_USERS, ID_COL + "=?", new String[]{String.valueOf(userId)});
        return rowsDeleted > 0;
    }

    public boolean updateItemInfo(int itemId, String category, String description, String url, int lowStock){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY_COL, category);
        values.put(ITEM_DESCRIPTION_COL, description);
        values.put(ITEM_URL_COL, url);
        values.put(ITEM_LOW_STOCK, lowStock);

        int rowsUpdated = db.update(TABLE_ITEMS, values, ITEM_ID_COL + "=?", new String[]{String.valueOf(itemId)});
        return rowsUpdated > 0;
    }

    public boolean deleteItem(int itemId){
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_ITEMS, ITEM_ID_COL + "=?", new String[]{String.valueOf(itemId)});
        return rowsDeleted > 0;
    }
}
