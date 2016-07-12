package codepath.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andrj148 on 7/12/16.
 */
class TextColorArrayAdapter extends ArrayAdapter {

    public TextColorArrayAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
    }

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
}
