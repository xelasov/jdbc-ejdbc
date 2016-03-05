package org.xelasov.ejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.google.common.base.Strings;
import org.xelasov.ejdbc.base.CallableStatementWrapper;
import org.xelasov.ejdbc.base.SqlUtils;
import org.xelasov.ejdbc.types.DBBool;
import org.xelasov.ejdbc.types.DBByte;
import org.xelasov.ejdbc.types.DBLong;
import org.xelasov.ejdbc.types.DBString;

import static com.google.common.base.Preconditions.checkArgument;

public class Function<T> {

    private final String        name;
    private final Parameter<T>  retVal;
    private final ParameterList params;

    public Function(final Parameter<T> retVal, final String name, final Parameter<?>... params) {
        this(retVal, name, new ParameterList(params));
    }

    public Function(Parameter<T> retVal, final String name, ParameterList params) {
        checkArgument(!Strings.isNullOrEmpty(name));
        checkArgument(retVal == null || retVal.isOutput());
        checkArgument(params != null);

        this.name = name;
        this.retVal = retVal;
        this.params = params;
    }

    public Function<T> inLong(Long v) {
        params.addParameter(new DBLong(v));
        return this;
    }

    public Function<T> inString(String v) {
        params.addParameter(new DBString(v));
        return this;
    }

    public Function<T> inBool(Boolean v) {
        params.addParameter(new DBBool(v));
        return this;
    }

    public Function<T> inByte(Byte v) {
        params.addParameter(new DBByte(v));
        return this;
    }
    
    

    public T execute(Connection conn) throws SQLException {
        checkArgument(conn != null);

        CallableStatement stmt = null;
        try {
            final String sql = SqlUtils.buildFunctionCallString(name, retVal != null, params.getParameterCount());
            stmt = conn.prepareCall(sql);
            final CallableStatementWrapper csw = new CallableStatementWrapper(stmt);
            applyParams(csw);
            stmt.execute();
            extractParams(csw);
            return retVal.getValueOrNull();
        } finally {
            SqlUtils.closeSafely(stmt);
        }
    }

    public T execute(DataSource ds) throws SQLException {
        checkArgument(ds != null);

        final Connection conn = SqlUtils.getConnection(ds, false);
        try {
            final T rv = execute(conn);
            SqlUtils.commitSafely(conn);
            return rv;
        } catch (SQLException e) {
            SqlUtils.rollbackSafely(conn);
            throw e;
        } finally {
            SqlUtils.closeSafely(conn);
        }
    }

    private void extractParams(CallableStatementWrapper stmt) throws SQLException {
        int pos = 1;
        if (retVal != null)
            retVal.extract(stmt, pos++);
        params.extractParams(stmt, pos);
    }

    private void applyParams(CallableStatementWrapper stmt) throws SQLException {
        int pos = 1;
        if (retVal != null)
            retVal.apply(stmt, pos++);
        params.applyParams(stmt, pos);
    }

}
