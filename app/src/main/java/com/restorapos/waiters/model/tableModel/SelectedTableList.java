package com.restorapos.waiters.model.tableModel;

public class SelectedTableList {
    private String tableId;
    private String  persons;

    public SelectedTableList(String tableId, String  persons) {
        this.tableId = tableId;
        this.persons = persons;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }
}
