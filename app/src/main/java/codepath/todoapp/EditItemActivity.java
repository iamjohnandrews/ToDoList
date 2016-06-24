package codepath.todoapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
            requestCode = 10;
        } else {
            requestCode = 20;
            captureDataFromViews();
        }
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
        creationDate = new Date();
        final Calendar today = Calendar.getInstance();
        creationDate.year = today.get(Calendar.YEAR);
        creationDate.month = today.get(Calendar.MONTH);
        creationDate.day = today.get(Calendar.DAY_OF_MONTH);

        itemDatePicker = (DatePicker) findViewById(R.id.datePicker);
        itemDatePicker.init(creationDate.year, creationDate.month, creationDate.day, null);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            deadlineDate = new Date();
            deadlineDate.year = selectedYear;
            deadlineDate.month = selectedMonth;
            deadlineDate.day = selectedDay;
        }
    };

    private void captureDataFromViews() {
        userSelectedEditText = (EditText) findViewById(R.id.userSelectedEditText);
        todoDetails = (EditText) findViewById(R.id.toDoDetails);
        priorityLevels = (Spinner) findViewById(R.id.levels);
        itemDatePicker = (DatePicker) findViewById(R.id.datePicker);

        if (requestCode == 20) {
            setTextForViews();
        }
    }

    private void setTextForViews() {
        userSelectedEditText.setText(selectedItem.taskName);
        todoDetails.setText(selectedItem.taskNote);
        priorityLevels.setPrompt(selectedItem.priorityLevel);
    }

    public void saveItem() {
        selectedItem = new Item(userSelectedEditText.getText().toString(), deadlineDate, todoDetails.getText().toString(), String.valueOf(priorityLevels.getSelectedItem()), creationDate);
        saveItemForMainActivity(requestCode);
    }

    private void saveItemForMainActivity(int index) {
        Intent userUpdatedTask = new Intent();
        userUpdatedTask.putExtra(SELECTED_ITEM, selectedItem);
        setResult(index, userUpdatedTask);
        finish();
    }
}
