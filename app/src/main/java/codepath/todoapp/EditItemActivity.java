package codepath.todoapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Spinner;
import java.io.Serializable;
import java.util.Calendar;


public class EditItemActivity extends AppCompatActivity {

    EditText userSelectedEditText;
    EditText todoDetails;
    Spinner priorityLevels;
    public static final String SELECTED_ITEM = "selectedItem";
    private final int EDITED_ITEM_REQUEST_CODE = 20;
    private final int NEW_ITEM_REQUEST_CODE = 10;
    Item selectedItem;
    DatePicker itemDatePicker;
    Date creationDate;
    Date deadlineDate;
    int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        selectedItem = (Item) getIntent().getSerializableExtra(SELECTED_ITEM);

        if (selectedItem == null) {
            setCurrentDateOnView();
            requestCode = NEW_ITEM_REQUEST_CODE;
        } else {
            requestCode = EDITED_ITEM_REQUEST_CODE;
            deadlineDate = selectedItem.dueDate;
        }
        accessViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        saveItem();
        return true;
    }

    private void setCurrentDateOnView() {
        final Calendar today = Calendar.getInstance();
        creationDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        itemDatePicker = (DatePicker) findViewById(R.id.datePicker);
        itemDatePicker.init(creationDate.year, creationDate.month, creationDate.day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                deadlineDate = new Date(i, 1 + i1, i2);
            }
        });
    }

    private void captureDataFromViews() {
        selectedItem.taskName = userSelectedEditText.getText().toString();
        selectedItem.taskNote = todoDetails.getText().toString();
        selectedItem.priorityLevel = String.valueOf(priorityLevels.getSelectedItem());
        selectedItem.dueDate = deadlineDate;
    }

    private void accessViews() {
        userSelectedEditText = (EditText) findViewById(R.id.userSelectedEditText);
        todoDetails = (EditText) findViewById(R.id.toDoDetails);
        priorityLevels = (Spinner) findViewById(R.id.levels);
        itemDatePicker = (DatePicker) findViewById(R.id.datePicker);
        if (requestCode == EDITED_ITEM_REQUEST_CODE) {
            setTextForViews();
        }
    }

    private void setTextForViews() {
        userSelectedEditText.setText(selectedItem.taskName);
        todoDetails.setText(selectedItem.taskNote);
        ArrayAdapter<String> arraySpinner = (ArrayAdapter<String>) priorityLevels.getAdapter();
        priorityLevels.setSelection(arraySpinner.getPosition(selectedItem.priorityLevel));

        itemDatePicker.init(selectedItem.dueDate.year, selectedItem.dueDate.month - 1, selectedItem.dueDate.day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                deadlineDate = new Date(i, 1 + i1, i2);
            }
        });
    }

    public void saveItem() {
        if (requestCode == NEW_ITEM_REQUEST_CODE) {
            selectedItem = new Item(userSelectedEditText.getText().toString(), deadlineDate, todoDetails.getText().toString(), String.valueOf(priorityLevels.getSelectedItem()), creationDate);
        } else {
            captureDataFromViews();
        }
        saveItemForMainActivity(requestCode);
    }

    private void saveItemForMainActivity(int index) {
        Intent userUpdatedTask = new Intent(EditItemActivity.this, MainActivity.class);
        userUpdatedTask.putExtra(SELECTED_ITEM, selectedItem);
        setResult(index, userUpdatedTask);
        finish();
    }
}
