package net.chinzer.seismicincidenttracker;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;

public class SeismicIncidentQueryBuilder implements SupportSQLiteQuery {

    private String select = "SELECT * FROM seismic_incidents";
    private List<String> where = new ArrayList<String>();
    private String orderBy = "";
    private String query = "";
    private String limit = "";
    private List<String> bindArgsWorking = new ArrayList<String>();
    private Object[] bindArgs;

    public SeismicIncidentQueryBuilder limit(int numberOfRows){
        limit = String.format("LIMIT %s", numberOfRows);
        return this;
    }

    public SeismicIncidentQueryBuilder whereLocality(String locality){
        if (locality != null) {
            addWhereStatment(String.format("%s LIKE ?", SeismicIncidentColumnName.LOCALITY.getColumnName()), "%" + locality + "%");
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereDay(OffsetDateTime dateTimeStart){
        if(dateTimeStart != null){
            addWhereStatment(String.format("date(%s) = date(?)", SeismicIncidentColumnName.DATETIME.getColumnName()), dateTimeStart);
        }

        return this;
    }

    public SeismicIncidentQueryBuilder whereDay(OffsetDateTime dateTimeStart, OffsetDateTime dateTimeEnd){
        if(dateTimeStart != null & dateTimeEnd != null){
            addWhereStatment(String.format("date(%s) BETWEEN date(?) AND date(?)", SeismicIncidentColumnName.DATETIME.getColumnName()), dateTimeStart, dateTimeEnd);
        }

        return this;
    }

    public SeismicIncidentQueryBuilder whereTime(OffsetTime timeStart){
        if(timeStart != null){
            addWhereStatment(String.format("time(%s) LIKE time(?)", SeismicIncidentColumnName.DATETIME.getColumnName()), timeStart);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereTime(OffsetTime timeStart, OffsetTime timeEnd){
        if(timeStart != null & timeEnd != null){
            addWhereStatment(String.format("time(%s) BETWEEN time(?) AND time(?)", SeismicIncidentColumnName.DATETIME.getColumnName()), timeStart, timeEnd);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereMagnitude(Double magnitudeStart){
        if(magnitudeStart != null){
            //making like but should probably have a conditional for precision
            addWhereStatment(String.format("%s = ?", SeismicIncidentColumnName.MAGNITUDE.getColumnName()), magnitudeStart);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereMagnitude(Double magnitudeStart, Double magnitudeEnd){
        if(magnitudeStart != null & magnitudeEnd != null){
            addWhereStatment(String.format("%s BETWEEN ? AND ?", SeismicIncidentColumnName.MAGNITUDE.getColumnName()), magnitudeStart, magnitudeEnd);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereDepth(Integer depthStart){
        if(depthStart != null){
            addWhereStatment(String.format("%s = ?", SeismicIncidentColumnName.MAGNITUDE.getColumnName()), depthStart);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereDepth(Integer depthStart, Integer depthEnd){
        if(depthStart != null & depthEnd != null){
            addWhereStatment(String.format("%s BETWEEN ? AND ?", SeismicIncidentColumnName.DEPTH.getColumnName()), depthStart, depthEnd);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder whereSeverity(String severity){
        if(severity != null){
            addWhereStatment(String.format("%s = ?", SeismicIncidentColumnName.SEVERITY.getColumnName()), severity);
        }
        return this;
    }

    public SeismicIncidentQueryBuilder orderBy(SeismicIncidentColumnName column, boolean ascending){
        String ascendingDescending;
        if (column != null){
            if (ascending) {
                ascendingDescending = "ASC";
            }
            else{
                ascendingDescending = "DESC";
            }
            orderBy = String.format("ORDER BY %s %s", column.getColumnName(), ascendingDescending);
        }
        return this;
    }

    public boolean addWhereStatment(String whereQuery, Object... inputVaribles){
        int numberOfInputVaribles = inputVaribles.length;
        int numberOfInputVariblesInQuery = whereQuery.length() - whereQuery.replace("?", "").length();
        if(numberOfInputVaribles != numberOfInputVariblesInQuery){
            return false;
        }
        List<String> inputVariblesConverted = new ArrayList<>();
        for (Object inputVarible : inputVaribles) {
            String inputVaribleString = "";
            if(inputVarible instanceof String){
                inputVaribleString = (String)inputVarible;
            }
            else if (inputVarible instanceof Double){
                inputVaribleString = String.valueOf(inputVarible);
            }
            else if (inputVarible instanceof Integer){
                inputVaribleString = String.valueOf(inputVarible);
            }
            else if (inputVarible instanceof OffsetDateTime){
                inputVaribleString = DateTimeTypeConverters.fromOffsettoDatetime((OffsetDateTime)inputVarible);
            }
            else if (inputVarible instanceof OffsetTime){
                inputVaribleString = DateTimeTypeConverters.fromOffsettoTime((OffsetTime) inputVarible);
            }
            else{
                return false;
            }
            inputVariblesConverted.add(inputVaribleString);
        }
        where.add(whereQuery);
        for(String inputVaribleConverted : inputVariblesConverted){
            bindArgsWorking.add(inputVaribleConverted);
        }
        return true;

    }

    public SeismicIncidentQueryBuilder compile(){
        String whereCompiled = compileWhere();
        query = String.format("%s %s %s %s", select, whereCompiled, orderBy, limit);
        bindArgs = new Object[bindArgsWorking.size()];
        bindArgsWorking.toArray(bindArgs);
        return this;
    }

    private String compileWhere(){
        String whereCopiled = "";
        boolean firstItem = true;
        for(String statment : where){
            if(firstItem == true){
                whereCopiled = String.format("WHERE %s", statment);
                firstItem = false;
            }
            else {
                whereCopiled = String.format("%s AND %s", whereCopiled, statment);
            }
        }
        return whereCopiled;
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
