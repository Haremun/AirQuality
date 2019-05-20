package com.esp8266collection.airquality.Database;

class SqlQuery {
    public String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + DatabaseStructure.TABLE_NAME + "(" +
                "id integer primary key autoincrement," +
                DatabaseStructure.PM_25 + " INTEGER," +
                DatabaseStructure.PM10 + " INTEGER);";
    }

    public String dropTable() {
        return "DROP TABLE IF EXISTS" + DatabaseStructure.TABLE_NAME;
    }
}
