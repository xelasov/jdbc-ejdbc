package org.xelasov.ejdbc.base;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.ServiceLoader;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class SqlUtils {

    private static final Logger                   log   = LoggerFactory.getLogger(SqlUtils.class);

    private static final HashMap<Integer, String> sqlTypes = new HashMap<Integer, String>();

    static {
        for (final Field f : Types.class.getDeclaredFields()) {
            try {
                sqlTypes.put(f.getInt(null), f.getName());
            } catch (final IllegalArgumentException e) {
            } catch (final IllegalAccessException e) {
            }
        }
    }

    public static String getSqlTypeName(int sqlType) {
        return sqlTypes.get(sqlType);
    }
    
    public static void registerDrivers() {
        final ServiceLoader<Driver> sl = ServiceLoader.load(java.sql.Driver.class);
        for (Driver d : sl) {
            try {
                log.info("Registering JDBC driver {}", d.getClass().getName());
                DriverManager.registerDriver(d);
            } catch (SQLException e) {
                log.error("Error registering driver", e); 
            }   
        }   
    }   


    public static String buildFunctionCallString(String name, boolean hasRetVal, int paramCnt) {
        // { ? = call get_cnt(?) }
        final StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        if (hasRetVal)
            sb.append("? = ");
        sb.append("call ").append(name).append("(");
        if (paramCnt > 0)
            sb.append("?");
        if (paramCnt > 1)
            sb.append(Strings.repeat(",?", paramCnt - 1));
        sb.append(") }");
        return sb.toString();
    }

    public static void closeSafely(final CallableStatement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (final SQLException e) {
            log.error("Error closing statement", e);
        }
    }

    public static void closeSafely(final Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (final SQLException e) {
            log.error("Error closing connection", e);
        }
    }

    public static void commitSafely(final Connection conn) {
        try {
            if (conn != null)
                conn.commit();
        } catch (final SQLException e) {
            log.error("Error commiting TX", e);
        }
    }

    public static Connection getConnection(final DataSource ds, final boolean autoCommit) throws SQLException {
        final Connection conn = ds.getConnection();
        if (conn.getAutoCommit() != autoCommit) {
            conn.setAutoCommit(autoCommit);
        }
        return conn;
    }

    public static void rollbackSafely(final Connection conn) {
        try {
            if (conn != null)
                conn.rollback();
        } catch (final SQLException e) {
            log.error("Error rolling back TX", e);
        }
    }

    public static void printResultSet(ResultSetWrapper rsw, PrintStream out) throws SQLException {
        final ResultSetMetaData rsmd = rsw.getResultSet().getMetaData();
        for (int i = 0; rsw.next(); i++) {
            if ((i % 10) == 0)
                printHeaders(rsmd, out);
            printRow(rsw, rsmd.getColumnCount(), out);
        }
    }

    private static void printRow(ResultSetWrapper rsw, int columnCount, PrintStream out) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            out.print(rsw.getStringOrNull(i));
            out.print(" | ");
        }
        out.println("");

    }

    private static void printHeaders(ResultSetMetaData rsmd, PrintStream out) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            sb.append(rsmd.getColumnLabel(i)).append(" | ");
        }
        sb.append('\n');

        for (int i = 0; i < sb.length(); i++)
            out.print('-');
        out.print('\n');
        out.print(sb.toString());
        for (int i = 0; i < sb.length(); i++)
            out.print('-');
        out.print('\n');
    }

}
