package net.chinzer.seismicincidenttracker;

import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;

public class SeismicIncidentQueryBuilder implements SupportSQLiteQuery {

    private final String select = "SELECT * FROM seismic_incidents";
    private String where = "";
    private String orderby = "";
    private String query = "";
    private Object[] bindArgs;

    public enum Column{
        DATETIME("dateTime"),
        DEPTH("depth"),
        MAGNITUDE("magnitude"),
        SEVERITY("severity"),
        LOCALITY("locality"),
        LATITUDE("latitude"),
        LONGITUDE("longitude"),
        LINK("link");

        private String columnName;

        Column(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    public SeismicIncidentQueryBuilder where(){

        return this;
    }

    public SeismicIncidentQueryBuilder orderBy(Column column, boolean ascending){
        String ascendingDescending;
        if (column != null){
            if (ascending) {
                ascendingDescending = "ASC";
            }
            else{
                ascendingDescending = "DESC";
            }
            orderby = "ORDER BY " + column.getColumnName() + " " + ascendingDescending;
        }
        return this;
    }

    public void compile(){

    }

    //taken from SimpleSQLiteQuery android library class
    //class cannot be inherited from so easier to copy the code and make my own
    //implementation of the interface based on that.

    public String getSql(){
        return query;
    }

    public void bindTo(SupportSQLiteProgram statement) {
        bind(statement, bindArgs);
    }

    public int getArgCount() {
        return bindArgs == null ? 0 : bindArgs.length;
    }

    public void bind(SupportSQLiteProgram statement, Object[] bindArgs) {
        if (bindArgs == null) {
            return;
        }
        int limit = bindArgs.length;
        for (int i = 0; i < limit; i++) {
            Object arg = bindArgs[i];
            bind(statement, i + 1, arg);
        }
    }


    private void bind(SupportSQLiteProgram statement, int index, Object arg) {
        // extracted from android.database.sqlite.SQLiteConnection
        if (arg == null) {
            statement.bindNull(index);
        } else if (arg instanceof byte[]) {
            statement.bindBlob(index, (byte[]) arg);
        } else if (arg instanceof Float) {
            statement.bindDouble(index, (Float) arg);
        } else if (arg instanceof Double) {
            statement.bindDouble(index, (Double) arg);
        } else if (arg instanceof Long) {
            statement.bindLong(index, (Long) arg);
        } else if (arg instanceof Integer) {
            statement.bindLong(index, (Integer) arg);
        } else if (arg instanceof Short) {
            statement.bindLong(index, (Short) arg);
        } else if (arg instanceof Byte) {
            statement.bindLong(index, (Byte) arg);
        } else if (arg instanceof String) {
            statement.bindString(index, (String) arg);
        } else if (arg instanceof Boolean) {
            statement.bindLong(index, ((Boolean) arg) ? 1 : 0);
        } else {
            throw new IllegalArgumentException("Cannot bind " + arg + " at index " + index
                    + " Supported types: null, byte[], float, double, long, int, short, byte,"
                    + " string");
        }
    }
}
