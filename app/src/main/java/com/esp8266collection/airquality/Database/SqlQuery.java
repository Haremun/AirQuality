package com.esp8266collection.airquality.Database;

class SqlQuery {
    public String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + DatabaseStructure.TABLE_NAME + "(" +
                "id integer primary key autoincrement," +
                DatabaseStructure.TABLE_NAME_TEMPERATURE + " INTEGER," +
                DatabaseStructure.TABLE_NAME_AIRQ + " INTEGER," +
                DatabaseStructure.TABLE_NAME_PM_25 + " INTEGER," +
                DatabaseStructure.TABLE_NAME_PM_10 + " INTEGER," +
                DatabaseStructure.TABLE_NAME_TIME + " INTEGER);";
    }

    public String dropTable() {
        return "DROP TABLE IF EXISTS" + DatabaseStructure.TABLE_NAME;
    }
}
