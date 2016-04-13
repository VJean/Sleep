package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by JeanV on 20/03/2016.
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class SleepItem implements Comparable<SleepItem> {
    private LocalDateTime begin;
    private LocalDateTime end;
    private Duration amount;
    private BooleanProperty alone = new SimpleBooleanProperty();
    private StringProperty where = new SimpleStringProperty();

    public SleepItem(){

    }

    public SleepItem(String beginStr, String endStr, String amountStr, boolean alone, String where) {
        this.setAlone(alone);
        this.setWhere(where);
        this.setBegin(LocalDateTime.parse(beginStr));
        this.setEnd(LocalDateTime.parse(endStr));
        if (amountStr.isEmpty() || amountStr == null)
            this.amount = null;
        else
            this.amount = Duration.parse(amountStr);
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
        if (amount != null)
            return amount;

        return Duration.between(begin, end);
    }

    public void setAmount(Duration amount) {
        this.amount = amount;
    }

    public boolean hasCustomAmount() {
        return getAmount() != null;
    }

    public BooleanProperty aloneProperty() { return alone; }

    public boolean getAlone() {
        return alone.get();
    }

    public void setAlone(boolean alone) {
        this.alone.setValue(alone);
    }

    public StringProperty whereProperty() { return where; }

    public String getWhere() {
        return where.get();
    }

    public void setWhere(String where) {
        this.where.set(where);
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm", Locale.getDefault());
        return "from "
                + begin.format(f)
                + " to "
                + end.format(f);
    }

    public String detailsToString() {
        String details = "(";

        details += amountAsHM();

        details += " at " + getWhere() + ", ";

        if (!getAlone())
            details += "not ";
        details += "alone";

        return details;
    }

    private String amountAsHM() {
        Long h = getAmount().toHours();
        Long m = getAmount().minusHours(h).toMinutes();
        return h.toString() + "h" + String.format("%02d", m) + "m";
    }

    public boolean isBefore(SleepItem other) {
        return this.end.isBefore(other.begin);
    }

    public boolean isAfter(SleepItem other) {
        return this.begin.isAfter(other.end);
    }

    public boolean overlaps(SleepItem other) {
        return (this.begin.isAfter(other.begin) && this.begin.isBefore(other.end))
                || (this.end.isAfter(other.begin) && this.end.isBefore(other.end))
                || (this.begin.isBefore(other.begin) && this.end.isAfter(other.end))
                || (this.begin.isAfter(other.begin) && this.end.isBefore(other.end));
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
