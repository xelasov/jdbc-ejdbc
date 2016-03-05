package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBInteger extends Parameter<Integer> {

    public DBInteger() {
        this(Parameter.Mode.out, null);
    }

    public DBInteger(final Integer val) {
        this(Parameter.Mode.in, val);
    }

    public DBInteger(Parameter.Mode mode, Integer val) {
        super(mode, val);
    }

    @Override
    public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isInput())
            stmt.setIntegerOrNull(pos, val);
        if (isOutput())
            stmt.registerOutParameter(pos, Types.INTEGER);
    }

    @Override
    public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isOutput())
            val = stmt.getIntegerOrNull(pos);
    }

    public int getValue() {
        return (val == null) ? 0 : val.intValue();
    }
}
