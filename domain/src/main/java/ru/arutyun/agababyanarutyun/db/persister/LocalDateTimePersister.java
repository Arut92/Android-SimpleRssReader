package ru.arutyun.agababyanarutyun.db.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;


/**
 * Custom persister for object DateTime
 */
public final class LocalDateTimePersister extends BaseDataType {

    private static final LocalDateTimePersister SINGLETON = new LocalDateTimePersister();

    private static final String DATE_TIME_FORMAT     = "yyyy-MM-dd HH:mm:ss";
    private DateTimeFormatter formatter;

    //////////////////////////

    private LocalDateTimePersister() {
        super(SqlType.STRING, new Class[]{LocalDateTime.class});
        formatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
    }

    //////////////////////////

    @SuppressWarnings("unused")
    @NotNull
    public static LocalDateTimePersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return null;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg == null) {
            return null;
        }

        final LocalDateTime dateTime = formatter.parseLocalDateTime((String) sqlArg);
        return dateTime;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        return formatter.print((LocalDateTime) javaObject);
    }

}

