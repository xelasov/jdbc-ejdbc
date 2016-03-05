package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBShort extends Parameter<Short> {

  public DBShort() {
    this(Parameter.Mode.out, null);
  }

  public DBShort(final Short val) {
    this(Parameter.Mode.in, val);
  }

  public DBShort(Parameter.Mode mode, Short val) {
    super(mode, val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput())
      stmt.setShortOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.SMALLINT);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput())
      val = stmt.getShortOrNull(pos);
  }

  public Short getValue() {
    return (val == null) ? 0 : val.shortValue();
  }
}
