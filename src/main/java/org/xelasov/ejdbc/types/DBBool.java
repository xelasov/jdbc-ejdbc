package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBBool extends Parameter<Boolean> {

    public DBBool() {
        this(Parameter.Mode.out, null);
    }

    public DBBool(final Boolean val) {
        this(Parameter.Mode.in, val);
    }

    public DBBool(Parameter.Mode mode, Boolean val) {
        super(mode, val);
    }

    @Override
    public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isInput())
            stmt.setBoolOrNull(pos, val);
        if (isOutput())
            stmt.registerOutParameter(pos, Types.TINYINT);
    }

    @Override
    public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isOutput())
            val = stmt.getBoolOrNull(pos);
    }

    public boolean getValue() {
        return (val == null) ? false : val.booleanValue();
    }
}
