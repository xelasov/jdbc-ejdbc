package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBDate extends Parameter<LocalDate> {

  public DBDate() {
    this(null, Parameter.Mode.out);
  }

  public DBDate(final LocalDate val) {
    this(val, Parameter.Mode.in);
  }

  public DBDate(final LocalDate val, final Parameter.Mode t) {
    super(t, val);
  }

  @Override
  public void apply(final CallableStatementWrapper stmt, final int pos) throws SQLException {
    if (isInput())
      stmt.setDateOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.DATE);
  }

  @Override
  public void extract(final CallableStatementWrapper stmt, final int pos) throws SQLException {
    if (isOutput())
      val = stmt.getDateOrNull(pos);
  }

  /**
   * @return value; never returns null
   */
  public LocalDate getValue() {
    return (val == null) ? LocalDate.from(Instant.EPOCH) : val;
  }

}
