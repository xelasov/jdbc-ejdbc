package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBLong extends Parameter<Long> {

  public DBLong() {
    this(Parameter.Mode.out, null);
  }

  public DBLong(final Long val) {
    this(Parameter.Mode.in, val);
  }

  public DBLong(Parameter.Mode mode, Long val) {
    super(mode, val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput())
      stmt.setLongOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.BIGINT);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput())
      val = stmt.getLongOrNull(pos);
  }

  public Long getValue() {
    return (val == null) ? 0 : val.longValue();
  }
}
