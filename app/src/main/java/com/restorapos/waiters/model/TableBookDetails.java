package com.restorapos.waiters.model;

public class TableBookDetails {
    private int table_num,person;

    public TableBookDetails() {
    }

    public TableBookDetails(int table_num, int person) {
        this.table_num = table_num;
        this.person = person;
    }

    public int getTable_num() {
        return table_num;
    }

    public void setTable_num(int table_num) {
        this.table_num = table_num;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }
}
