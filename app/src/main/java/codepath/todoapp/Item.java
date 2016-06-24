package codepath.todoapp;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by andrj148 on 6/23/16.
 */

public class Item implements Serializable {
    public String taskName;
    public Date dueDate;
    public String taskNote;
    public String priorityLevel;
    public Date creationDate;

    public Item(String name, Date deadline, String note, String priority, Date creation) {
        taskName = name;
        dueDate = deadline;
        taskNote = note;
        priorityLevel = priority;
        creationDate = creation;
    }

}
