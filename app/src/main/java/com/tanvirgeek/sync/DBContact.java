package com.tanvirgeek.sync;

public class DBContact {
    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static final String SERVER_URL =  "http://192.168.0.107/syncdemo/synchinfo.php";
    public static final String DATABASE_NAME = "contactdb";
    public static final String TABLE_NAME = "contactinfo";
    public static final String COL_NAME = "name";
    public static final String COL_SYNC_STATUS = "syncstatus";

}
