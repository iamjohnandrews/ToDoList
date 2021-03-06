package codepath.todoapp;

import java.io.Serializable;

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
