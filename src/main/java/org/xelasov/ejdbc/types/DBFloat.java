package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBFloat extends Parameter<Float> {

  public DBFloat() {
    this(Parameter.Mode.out, null);
  }

  public DBFloat(final Float val) {
    this(Parameter.Mode.in, val);
  }

  public DBFloat(Parameter.Mode mode, Float val) {
    super(mode, val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput())
      stmt.setFloatOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.FLOAT);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput())
      val = stmt.getFloatOrNull(pos);
  }

  public Float getValue() {
    return (val == null) ? 0 : val.floatValue();
  }
}
