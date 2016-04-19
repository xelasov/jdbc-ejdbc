package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBDateTime extends Parameter<LocalDateTime> {

  public DBDateTime() {
    this(null, Parameter.Mode.out);
  }

  public DBDateTime(final LocalDateTime val) {
    this(val, Parameter.Mode.in);
  }

  public DBDateTime(final LocalDateTime val, final Parameter.Mode t) {
    super(t, val);
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
      val = stmt.getDateTimeOrNull(pos);
  }

  /**
   * @return value; never returns null
   */
  public LocalDateTime getValue() {
    return (val == null) ? LocalDateTime.from(Instant.EPOCH) : val;
  }

}
