package codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.Serializable;


public class MainActivity extends AppCompatActivity {

    ArrayList<Item> todoItems;
    ArrayAdapter<Item> aToDoAdapter;
    ListView lvItems;
    int selectedIndexRow;
    private final String ITEMS_OBJECT_ARRAY = "persistedItemsArray";
    private final int EDITED_ITEM_REQUEST_CODE = 20;
    public final int NEW_ITEM_REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();


        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                todoItems.remove(i);
                updateListViewAndPersistItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateToEditActivity(position);
            }
        });
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    //region Private Methods
    private void readItems() {
//        File fileDir = getFilesDir();
//        File file = new File(fileDir, "todo.txt");

        try {
            FileInputStream inStream = openFileInput(ITEMS_OBJECT_ARRAY);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            int count = objectInStream.readInt();

            todoItems = (ArrayList<Item>) objectInStream.readObject());
            objectInStream.close();

//            todoItems = new ArrayList<Item>(FileUtils.readLines(file));
        } catch (IOException e) {
            System.out.println("todoItems array created in Exception");
            todoItems = new ArrayList<Item>();

            Date testDate1 = new Date();
            testDate1.year = 2016;
            testDate1.month = 5;
            testDate1.day = 13;
            Date testDate2 = new Date();
            testDate2.year = 2016;
            testDate2.month = 9;
            testDate2.day = 23;

            Item testItem = new Item("Item 1", testDate1, "Item1 notes", "High", testDate2);
            todoItems.add(testItem);
        }
    }

    private void writeItems() {
//        File fileDir = getFilesDir();
//        File file = new File(fileDir, "todo.txt");

        try {
            FileOutputStream outStream = openFileOutput(ITEMS_OBJECT_ARRAY, MODE_PRIVATE);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeInt(todoItems.size());
            for (Item taskItem:todoItems)
                objectOutStream.writeObject(taskItem);
            objectOutStream.close();

//            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            System.out.println("Exception thrown in WRITEItems WTF");
            e.printStackTrace();
        }
    }

    private void updateListViewAndPersistItems() {
        aToDoAdapter.notifyDataSetChanged();
        writeItems();
    }
    //endregion

    public void onAddItem(View view) {
        System.out.println("onAddItem called WTF");

    }


    public void navigateToEditActivity(int index) {
        selectedIndexRow = index;
        Intent transitionToEdit = new Intent(MainActivity.this, EditItemActivity.class);
        transitionToEdit.putExtra(EditItemActivity.SELECTED_ITEM,todoItems.get(index).toString());
        startActivityForResult(transitionToEdit, EDITED_ITEM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult called");
        if (requestCode == EDITED_ITEM_REQUEST_CODE && requestCode == EDITED_ITEM_REQUEST_CODE) {
            todoItems.remove(selectedIndexRow);

            Item newItem = (Item) data.getExtras().getSerializable(EditItemActivity.SELECTED_ITEM);
            todoItems.add(selectedIndexRow, newItem);

            updateListViewAndPersistItems();
        }
    }
}
