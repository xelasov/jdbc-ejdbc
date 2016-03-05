package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBDouble extends Parameter<Double> {

    public DBDouble() {
        this(Parameter.Mode.out, null);
    }

    public DBDouble(final Double val) {
        this(Parameter.Mode.in, val);
    }

    public DBDouble(Parameter.Mode mode, Double val) {
        super(mode, val);
    }

    @Override
    public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isInput())
            stmt.setDoubleOrNull(pos, val);
        if (isOutput())
            stmt.registerOutParameter(pos, Types.DOUBLE);
    }

    @Override
    public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isOutput())
            val = stmt.getDoubleOrNull(pos);
    }

    public Double getValue() {
        return (val == null) ? 0 : val.doubleValue();
    }
}
