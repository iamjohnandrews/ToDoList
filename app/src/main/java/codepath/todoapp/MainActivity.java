package codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private final int EDITED_ITEM_REQUEST_CODE = 20;
    int selectedIndexRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
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
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    //region Private Methods
    private void readItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");

        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            System.out.println("todoItems array created in Exception");
            todoItems = new ArrayList<String>();
            todoItems.add("Item 1");
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");

        try {
            FileUtils.writeLines(file, todoItems);
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
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
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

            String newItem = data.getExtras().getString("editedText");
            todoItems.add(selectedIndexRow, newItem);

            updateListViewAndPersistItems();
        }
    }
}
