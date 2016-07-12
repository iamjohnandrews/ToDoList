package codepath.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Item> todoItems;
    ArrayList<String> taskNames;
    ArrayList<String> priorityLevels;
    ArrayList<String> dueDates;
    ArrayAdapter<String> aToDoAdapter;
    ArrayAdapter<String> priorityAdapter;
    ArrayAdapter<String> dueDateAdapter;
    ListView lvItems;
    ListView lvpriorityLevels;
    ListView lvDueDates;
    int selectedIndexRow;
    private final String ITEMS_OBJECT_ARRAY = "persistedItemsArray";
    private final int EDITED_ITEM_REQUEST_CODE = 20;
    private final int NEW_ITEM_REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        setUpAllListViews();
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
        iterateThroughItemsToDisplayObjects();
        bindArraysToListViews();
    }

    //region Private Methods
    private void setUpAllListViews() {
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

        lvDueDates = (ListView) findViewById(R.id.lvDueDates);
        lvDueDates.setAdapter(dueDateAdapter);
    }

    private void readItems() {
        todoItems = Item.listAll(Item.class);
    }

    private void writeItems() {
        for (Item taskItem:todoItems) {
            taskItem.save();
        }
    }

    private void updateListViewAndPersistItems() {
        aToDoAdapter.notifyDataSetChanged();
        priorityAdapter.notifyDataSetChanged();
        dueDateAdapter.notifyDataSetChanged();
        writeItems();
    }

    private void iterateThroughItemsToDisplayObjects() {
        if (taskNames == null) {
            createArrayListsIfNeeded();
        }
        for (Item item:todoItems) {
            taskNames.add(item.taskName);
            dueDates.add(convertDateToString(item.dueDate));
            priorityLevels.add(item.priorityLevel);
        }
    }

    private void createArrayListsIfNeeded() {
        taskNames = new ArrayList<String>();
        priorityLevels = new ArrayList<String>();
        dueDates = new ArrayList<String>();
    }

    private void bindArraysToListViews() {
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskNames);
        dueDateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dueDates);
        priorityAdapter = textColorChangingAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, priorityLevels) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View cell = super.getView(position, convertView, parent);

                TextView selectedPriority = (TextView) cell;

                if (selectedPriority.getText().toString().equalsIgnoreCase("High")) {
                    selectedPriority.setTextColor(Color.RED);
                } else if (selectedPriority.getText().toString().equalsIgnoreCase("Medium")) {
                    selectedPriority.setTextColor(Color.MAGENTA);
                } else {
                    selectedPriority.setTextColor(Color.BLACK);
                }
                return cell;
            }
        });
    }

    private ArrayAdapter<String>  textColorChangingAdapter(ArrayAdapter<String> arrayAdapter) {
        return arrayAdapter;
    }


    private void addItemToArrays(Item item, int index) {
        todoItems.add(index, item);
        taskNames.add(item.taskName);
        dueDates.add(convertDateToString(item.dueDate));
        priorityLevels.add(item.priorityLevel);
    }

    private String convertDateToString(Date date) {
        return date.month.toString() + "/" + date.day.toString() + "/" + date.year.toString();
    }

    private void  removeItemsFromArrays(int index) {
        todoItems.remove(index);
        taskNames.remove(index);
        dueDates.remove(index);
        priorityLevels.remove(index);
    }

    private void createTestItemToAddToArrayList() {
        todoItems = new ArrayList<Item>();

        Date testDate1 = new Date(2016, 11, 13);
        Date testDate2 = new Date(2016, 9, 23);

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
