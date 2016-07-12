package codepath.todoapp;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by andrj148 on 6/23/16.
 */
public class Date implements Serializable {
    public Integer year;
    public Integer month;
    public Integer day;

    public Date(int year, int month, int day ) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
