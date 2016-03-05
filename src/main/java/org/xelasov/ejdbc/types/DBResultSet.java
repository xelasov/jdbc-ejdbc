package org.xelasov.ejdbc.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;
import org.xelasov.ejdbc.base.ResultSetWrapper;

import static com.google.common.base.Preconditions.checkState;

public abstract class DBResultSet<BeanT> extends Parameter<BeanT> {

    protected DBResultSet() {
        super(Parameter.Mode.out, null);
    }

    @Override
    public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
        checkState(isOutput());
        stmt.registerOutParameter(pos, Types.OTHER);
    }

    @Override
    public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
        checkState(isOutput());
        final ResultSet rs = stmt.getObject(pos);
        if (rs != null) {
            final ResultSetWrapper rsw = new ResultSetWrapper(rs);
            consumeResultSet(rsw);
        }
    }

    protected abstract void consumeResultSet(ResultSetWrapper rsw) throws SQLException;

}
