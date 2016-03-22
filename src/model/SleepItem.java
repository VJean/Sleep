package model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by JeanV on 20/03/2016.
 */
public class SleepItem {
    int id;
    LocalDateTime begin;
    LocalDateTime end;
    Duration amount;
    boolean alone;
    int where;

    SleepItem(){

    }

    public SleepItem(int id, String beginStr, String endStr, String amountStr, boolean alone, int where) {
        this.id = id;
        this.alone = alone;
        this.where = where;
        if (amountStr.isEmpty() || amountStr == null)
            this.amount = null;
        else
            this.amount = Duration.parse(amountStr);
        this.begin = LocalDateTime.parse(beginStr);
        this.end = LocalDateTime.parse(endStr);
    }
}
