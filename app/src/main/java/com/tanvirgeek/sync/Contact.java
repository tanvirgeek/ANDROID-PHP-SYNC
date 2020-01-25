package com.tanvirgeek.sync;

public class Contact {

    private String name;
    private int syncStatus;

    public Contact(String name, int syncStatus) {
        this.name = name;
        this.syncStatus = syncStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSyncstatus() {
        return syncStatus;
    }

    public void setSyncstatus(int syncstatus) {
        this.syncStatus = syncStatus;
    }
}
