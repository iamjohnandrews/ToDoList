import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import java.util.ArrayList;

/**
 * Created by andrj148 on 6/20/16.
 */
public class ItemsDatabaseHelper extends SQLiteOpenHelper {
    private static ItemsDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_ITEMS = "items";

    // Post Table Columns
    private static final int KEY_ITEM_ID = 0;
    private static final String KEY_ITEM_NAME = "id";
    private static final String KEY_ITEM_DUEDATE = "id";
    private static final String KEY_ITEM_TASKNOTES = "id";
    private static final int KEY_ITEM_PRIORITYLEVEL = 0;
    private static final boolean KEY_ITEM_STATUS = false;
    private static final String KEY_ITEM_CREATIONDATE = "id";

    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_NAME + "," +
                KEY_ITEM_DUEDATE + "," +
                KEY_ITEM_TASKNOTES + "," +
                KEY_ITEM_PRIORITYLEVEL + "," +
                KEY_ITEM_STATUS + "," +
                KEY_ITEM_CREATIONDATE + "," +
                ")";

        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Insert a item into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            long itemId = addOrUpdateUser(item.id);

            ContentValues values = new ContentValues();

            values.put(KEY_ITEM_ID, userId);
            values.put(KEY_ITEM_NAME, item.name);
            values.put(KEY_ITEM_DUEDATE, item.dueDate);
            values.put(KEY_ITEM_TASKNOTES, item.taskNote);
            values.put(KEY_ITEM_PRIORITYLEVEL, item.priorityLevel);
            values.put(KEY_ITEM_STATUS, item.status);
            values.put(KEY_ITEM_CREATIONDATE, item.creationDate);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Item> getAllPosts() { //not finished
        List<Item> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_POSTS,
                        TABLE_USERS,
                        TABLE_POSTS, KEY_POST_USER_ID_FK,
                        TABLE_USERS, KEY_USER_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));

                    Post newPost = new Post();
                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
                    newPost.user = newUser;
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println("Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

}

