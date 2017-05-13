package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.ZonedDateTime;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBZonedDateTime extends Parameter<ZonedDateTime> {

  public DBZonedDateTime() {
    this(null, Parameter.Mode.out);
  }

  public DBZonedDateTime(final ZonedDateTime val) {
    this(val, Parameter.Mode.in);
  }

  public DBZonedDateTime(final ZonedDateTime val, final Parameter.Mode t) {
    super(t, val);
  }

  @Override
  public void apply(final CallableStatementWrapper stmt, final int pos) throws SQLException {
    if (isInput())
      stmt.setZonedDateTimeOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.TIMESTAMP);
  }

  @Override
  public void extract(final CallableStatementWrapper stmt, final int pos) throws SQLException {
    if (isOutput())
      val = stmt.getZonedDateTimeOrNull(pos);
  }

  /**
   * @return value; never returns null
   */
  public ZonedDateTime getValue() {
    return (val == null) ? ZonedDateTime.from(Instant.EPOCH) : val;
  }

}
