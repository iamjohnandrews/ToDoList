package codepath.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class EditItemActivity extends AppCompatActivity {

    EditText userSelectedEditText;
    String userEditedText;
    public static final String SELECTED_ITEM = "selectedItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String selectedItem = getIntent().getStringExtra(SELECTED_ITEM);

        userSelectedEditText = (EditText) findViewById(R.id.userSelectedEditText);
        userSelectedEditText.setText(selectedItem);
    }



    public void saveItem(View view) {
        userEditedText = userSelectedEditText.getText().toString();
        saveEditedTextForMainActivity();
    }

    private void saveEditedTextForMainActivity() {
        Intent editedText = new Intent();
        editedText.putExtra("editedText", userEditedText);
        setResult(20, editedText);
        finish();
    }
}
