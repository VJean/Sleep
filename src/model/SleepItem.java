package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

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
    private BooleanProperty alone = new SimpleBooleanProperty();
    private int where;

    public SleepItem(){

    }

    public SleepItem(int id, String beginStr, String endStr, String amountStr, boolean alone, int where) {
        this.id = id;
        this.alone.setValue(alone);
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

    public BooleanProperty aloneProperty() { return alone; }

    public boolean getAlone() {
        return alone.get();
    }

    public void setAlone(boolean alone) {
        this.alone.setValue(alone);
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    @Override
    public String toString() {
        // just the date
        return "(id" + String.valueOf(this.id) + ") " + this.end.toLocalDate().toString();
    }
}
