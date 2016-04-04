package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by JeanV on 20/03/2016.
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class SleepItem implements Comparable<SleepItem> {
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

    public boolean isBefore(SleepItem other) {
        return this.end.isBefore(other.begin);
    }

    public boolean isAfter(SleepItem other) {
        return this.begin.isAfter(other.end);
    }

    public boolean overlaps(SleepItem other) {
        return (this.begin.isAfter(other.begin) && this.begin.isBefore(other.end))
                || (this.end.isAfter(other.begin) && this.end.isBefore(other.end));
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(SleepItem o) {
        return this.begin.compareTo(o.begin);
    }
}
