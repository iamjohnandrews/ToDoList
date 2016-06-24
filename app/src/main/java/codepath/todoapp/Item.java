package codepath.todoapp;

import java.io.Serializable;

/**
 * Created by andrj148 on 6/23/16.
 */

public class Item implements Serializable {
    public String taskName;
    public Date dueDate;
    public String taskNote;
    public String priorityLevel;
    public Date creationDate;
}
