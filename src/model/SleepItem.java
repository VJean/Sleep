package model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by JeanV on 20/03/2016.
 */
public class SleepItem {
    private int id;
    private LocalDateTime begin;
    private LocalDateTime end;
    private Duration amount;
    private boolean alone;
    private int where;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Duration getAmount() {
        return amount;
    }

    public void setAmount(Duration amount) {
        this.amount = amount;
    }

    public boolean getAlone() {
        return alone;
    }

    public void setAlone(boolean alone) {
        this.alone = alone;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }
}
