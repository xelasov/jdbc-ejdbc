package org.xelasov.ejdbc.types;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBBigDecimal extends Parameter<BigDecimal> {

  public DBBigDecimal() {
    this(Parameter.Mode.out, null);
  }

  public DBBigDecimal(final BigDecimal val) {
    this(Parameter.Mode.in, val);
  }

  public DBBigDecimal(Parameter.Mode mode, BigDecimal val) {
    super(mode, val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput())
      stmt.setBigDecimalOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.NUMERIC);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput())
      val = stmt.getBigDecimalOrNull(pos);
  }

  public BigDecimal getValue() {
    return (val == null) ? BigDecimal.ZERO : val;
  }
}
