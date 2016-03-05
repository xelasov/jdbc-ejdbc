package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBDateTime extends Parameter<Date> {

    public DBDateTime() {
        this(null, Parameter.Mode.out);
    }

    public DBDateTime(final Date val) {
        this(val, Parameter.Mode.in);
    }

    public DBDateTime(final Date val, final Parameter.Mode t) {
        super(t, val);
    }

    public DBDateTime(final long val) {
        this(new Date(val));
    }

    @Override
    public void apply(final CallableStatementWrapper stmt, final int pos) throws SQLException {
        if (isInput())
            stmt.setDateTimeOrNull(pos, val);
        if (isOutput())
            stmt.registerOutParameter(pos, Types.TIMESTAMP);
    }

    @Override
    public void extract(final CallableStatementWrapper stmt, final int pos) throws SQLException {
        if (isOutput())
            val = stmt.getDateOrNull(pos);
    }

    /**
     * @return value; never returns null
     */
    public Date getValue() {
        return (val == null) ? new Date(0) : val;
    }

}
