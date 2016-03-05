package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import com.google.common.base.Strings;
import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBString extends Parameter<String> {

    public DBString() {
        this(Parameter.Mode.out, null);
    }

    public DBString(final String val) {
        this(Parameter.Mode.in, val);
    }

    public DBString(Parameter.Mode mode, String val) {
        super(mode, val);
    }

    @Override
    public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isInput())
            stmt.setStringOrNull(pos, val);
        if (isOutput())
            stmt.registerOutParameter(pos, Types.VARCHAR);
    }

    @Override
    public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
        if (isOutput())
            val = stmt.getStringOrNull(pos);
    }

    public String getValue() {
        return Strings.nullToEmpty(val);
    }
}
