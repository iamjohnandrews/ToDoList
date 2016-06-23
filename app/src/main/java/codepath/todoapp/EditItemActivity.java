package codepath.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Spinner;

public class EditItemActivity extends AppCompatActivity {

    EditText userSelectedEditText;
    String userEditedText;
    EditText todoDetails;
    String userAddedTaskNotes;
    Spinner priorityLevels;
    String userSelectedPriorityLevel;
    public static final String SELECTED_ITEM = "selectedItem";
    public static final String EDITED_TASK_NAME = "editedTaskName";
    public static final String EDITED_TASK_NOTES = "editedTaskNotes";
    public static final String EDITED_PRIORITY_LEVEL = "editedPriorityLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String selectedItem = getIntent().getStringExtra(SELECTED_ITEM);

        userSelectedEditText = (EditText) findViewById(R.id.userSelectedEditText);
        userSelectedEditText.setText(selectedItem);

        todoDetails = (EditText) findViewById(R.id.toDoDetails);
        priorityLevels = (Spinner) findViewById(R.id.levels);
    }



    public void saveItem(View view) {
        userEditedText = userSelectedEditText.getText().toString();
        userAddedTaskNotes = todoDetails.getText().toString();
        userSelectedPriorityLevel = String.valueOf(priorityLevels.getSelectedItem());
        saveEditedTextForMainActivity();
    }

    private void saveEditedTextForMainActivity() {
        Intent userUpdatedTask = new Intent();
        userUpdatedTask.putExtra(EDITED_TASK_NAME, userEditedText);
        userUpdatedTask.putExtra(EDITED_TASK_NOTES, userAddedTaskNotes);
        userUpdatedTask.putExtra(EDITED_PRIORITY_LEVEL, userSelectedPriorityLevel);

        setResult(20, userUpdatedTask);
        finish();
    }
}
