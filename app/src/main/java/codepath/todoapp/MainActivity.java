package codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Item> todoItems;
    ArrayList<String> taskNames;
    ArrayList<String> priorityLevels;
    ArrayAdapter<String> aToDoAdapter;
    ArrayAdapter<String> priorityAdapter;
    ListView lvItems;
    ListView lvpriorityLevels;
    int selectedIndexRow;
    private final String ITEMS_OBJECT_ARRAY = "persistedItemsArray";
    private final String TASK_NAME = "taskName";
    private final String PRIORITY_LEVEL = "priority";
    private final int EDITED_ITEM_REQUEST_CODE = 20;
    private final int NEW_ITEM_REQUEST_CODE = 10;



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
                removeItemsFromArrays(i);
                updateListViewAndPersistItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndexRow = position;
                navigateToEditActivity(EDITED_ITEM_REQUEST_CODE);
            }
        });
        lvpriorityLevels = (ListView) findViewById(R.id.lvpriorityLevels);
        lvpriorityLevels.setAdapter(priorityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        navigateToEditActivity(NEW_ITEM_REQUEST_CODE);
        return true;
    }

    public void populateArrayItems() {
        readItems();
        taskNames = retrieveStringsFromItemObjects(TASK_NAME);
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskNames);

        priorityLevels = retrieveStringsFromItemObjects(PRIORITY_LEVEL);
        priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, priorityLevels);
    }

    //region Private Methods
    private void readItems() {
        todoItems = new ArrayList<>();

        try {
            FileInputStream inStream = openFileInput(ITEMS_OBJECT_ARRAY);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            int count = objectInStream.readInt();
            for (int i = 0; i < count; i++)
                try {
                    todoItems.add((Item) objectInStream.readObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            objectInStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItems() {
        try {
            FileOutputStream outStream = openFileOutput(ITEMS_OBJECT_ARRAY, MODE_PRIVATE);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeInt(todoItems.size());
            for (Item taskItem:todoItems) {
                objectOutStream.writeObject(taskItem);
            }
            objectOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateListViewAndPersistItems() {
        aToDoAdapter.notifyDataSetChanged();
        priorityAdapter.notifyDataSetChanged();
        writeItems();
    }

    private ArrayList<String> retrieveStringsFromItemObjects(String itemType) {
        ArrayList<String> arrayOfStrings = new ArrayList<String>();

        if (itemType.equalsIgnoreCase(TASK_NAME)) {
            for (Item item:todoItems) {
                arrayOfStrings.add(item.taskName);
            }
        } else if (itemType.equalsIgnoreCase(PRIORITY_LEVEL))
            for (Item item:todoItems) {
                arrayOfStrings.add(item.priorityLevel);
            }
        return arrayOfStrings;
    }

    private void addItemToArrays(Item item, int index) {
        todoItems.add(index, item);
        taskNames.add(item.taskName);
        priorityLevels.add(item.priorityLevel);
    }

    private void  removeItemsFromArrays(int index) {
        todoItems.remove(index);
        taskNames.remove(index);
        priorityLevels.remove(index);
    }

    private void createTestItemToAddToArrayList() {
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
    //endregion

    public void navigateToEditActivity(int requestCode) {
        Intent transitionToEdit = new Intent(MainActivity.this, EditItemActivity.class);

        if (requestCode == EDITED_ITEM_REQUEST_CODE) {
            transitionToEdit.putExtra(EditItemActivity.SELECTED_ITEM, todoItems.get(selectedIndexRow));
        }
        startActivityForResult(transitionToEdit, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Item newItem = (Item) data.getExtras().getSerializable(EditItemActivity.SELECTED_ITEM);

        if (requestCode == EDITED_ITEM_REQUEST_CODE) {
            removeItemsFromArrays(selectedIndexRow);
            addItemToArrays(newItem, selectedIndexRow);
        } else if (requestCode == NEW_ITEM_REQUEST_CODE) {
            addItemToArrays(newItem, todoItems.size());
        }
        updateListViewAndPersistItems();
    }
}
