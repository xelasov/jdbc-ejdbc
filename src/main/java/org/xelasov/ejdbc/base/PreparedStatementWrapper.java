package org.xelasov.ejdbc.base;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PreparedStatementWrapper {

  private final PreparedStatement stmt;

  public PreparedStatementWrapper(PreparedStatement stmt) {
    Assert.argumentNotNull(stmt);
    this.stmt = stmt;
  }

  public void setBool(final int pos, final boolean val) throws SQLException {
    stmt.setBoolean(pos, val);
  }

  public void setBoolOrNull(final int pos, final Boolean val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.BOOLEAN);
    else
      setBool(pos, val.booleanValue());
  }

  public void setByte(final int pos, final byte val) throws SQLException {
    stmt.setByte(pos, val);
  }

  public void setByteOrNull(final int pos, final Byte val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.TINYINT);
    else
      setByte(pos, val.byteValue());
  }

  public void setDate(final int pos, final LocalDate val) throws SQLException {
    setDateOrNull(pos, val == null ? LocalDate.from(Instant.EPOCH) : val);
  }

  public void setDateOrNull(final int pos, final LocalDate val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.DATE);
    else
      stmt.setDate(pos, java.sql.Date.valueOf(val));
  }

  public void setDateTime(final int pos, final LocalDateTime val) throws SQLException {
    setDateTimeOrNull(pos, val == null ? LocalDateTime.from(Instant.EPOCH) : val);
  }

  public void setDateTimeOrNull(final int pos, final LocalDateTime val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.TIMESTAMP);
    else
      stmt.setTimestamp(pos, java.sql.Timestamp.valueOf(val));
  }

  public void setDouble(final int pos, final double v) throws SQLException {
    stmt.setDouble(pos, v);
  }

  public void setDoubleOrNull(final int pos, final Double v) throws SQLException {
    if (v == null)
      stmt.setNull(pos, Types.DOUBLE);
    else
      stmt.setDouble(pos, v.doubleValue());
  }

  public void setFloat(final int pos, final float v) throws SQLException {
    stmt.setFloat(pos, v);
  }

  public void setFloatOrNull(final int pos, final Float v) throws SQLException {
    if (v == null)
      stmt.setNull(pos, Types.FLOAT);
    else
      stmt.setFloat(pos, v.floatValue());
  }

  public void setInteger(final int pos, final int v) throws SQLException {
    stmt.setInt(pos, v);
  }

  public void setIntegerOrNull(final int pos, final Integer val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.INTEGER);
    else
      stmt.setInt(pos, val.intValue());
  }

  public void setArray(final int pos, final Array v) throws SQLException {
    stmt.setArray(pos, v);
  }

  public void setArrayOrNull(final int pos, final Array val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.ARRAY);
    else
      stmt.setArray(pos, val);
  }

  public void setLong(final int pos, final long v) throws SQLException {
    stmt.setLong(pos, v);
  }

  public void setLongOrNull(final int pos, final Long val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.BIGINT);
    else
      stmt.setLong(pos, val.longValue());
  }

  public void setShort(final int pos, final short v) throws SQLException {
    stmt.setShort(pos, v);
  }

  public void setShortOrNull(final int pos, final Short v) throws SQLException {
    if (v == null)
      stmt.setNull(pos, Types.SMALLINT);
    else
      stmt.setShort(pos, v.shortValue());
  }

  public void setString(final int pos, final String val) throws SQLException {
    setStringOrNull(pos, val == null ? "" : val);
  }

  public void setStringOrNull(final int pos, final String val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.VARCHAR);
    else
      stmt.setString(pos, val);
  }

  public void setTime(final int pos, final LocalTime val) throws SQLException {
    setTimeOrNull(pos, val == null ? LocalTime.from(Instant.EPOCH) : val);
  }

  public void setTimeOrNull(final int pos, final LocalTime val) throws SQLException {
    if (val == null)
      stmt.setNull(pos, Types.TIME);
    else
      stmt.setTime(pos, java.sql.Time.valueOf(val));
  }

}
