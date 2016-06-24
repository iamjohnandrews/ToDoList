package codepath.todoapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
//        selectedItem = getIntent().getStringExtra(SELECTED_ITEM);
        selectedItem = (Item) getIntent().getSerializableExtra(SELECTED_ITEM);

        if (selectedItem == null) {
            setCurrentDateOnView();
        } else {
            userSelectedEditText = (EditText) findViewById(R.id.userSelectedEditText);
            userSelectedEditText.setText(selectedItem.taskName);
            todoDetails = (EditText) findViewById(R.id.toDoDetails);
            todoDetails.setText(selectedItem.taskNote);
            priorityLevels = (Spinner) findViewById(R.id.levels);
            priorityLevels.setPrompt(selectedItem.priorityLevel);
            itemDatePicker = (DatePicker) findViewById(R.id.datePicker);
        }
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

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            deadlineDate = new Date();
            deadlineDate.year = selectedYear;
            deadlineDate.month = selectedMonth;
            deadlineDate.day = selectedDay;

            // set selected date into datepicker also
//            dpResult.init(year, month, day, null);

        }
    };

    public void saveItem(View view) {
        selectedItem = new Item(userSelectedEditText.getText().toString(), deadlineDate, todoDetails.getText().toString(), String.valueOf(priorityLevels.getSelectedItem()), creationDate);
        saveEditedTextForMainActivity();
    }

    private void saveEditedTextForMainActivity() {
        Intent userUpdatedTask = new Intent();
        userUpdatedTask.putExtra(SELECTED_ITEM, selectedItem);
        setResult(20, userUpdatedTask);
        finish();
    }
}
