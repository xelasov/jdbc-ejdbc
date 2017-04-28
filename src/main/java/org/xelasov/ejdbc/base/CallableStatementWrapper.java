package org.xelasov.ejdbc.base;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CallableStatementWrapper extends PreparedStatementWrapper {

  private final CallableStatement cstmt;

  public CallableStatementWrapper(CallableStatement cstmt) {
    super(cstmt);
    this.cstmt = cstmt;
  }

  public byte getByte(final int pos) throws SQLException {
    return cstmt.getByte(pos);
  }

  public Byte getByteOrNull(final int pos) throws SQLException {
    final byte rv = cstmt.getByte(pos);
    return cstmt.wasNull() ? null : new Byte(rv);
  }
  
  public BigDecimal getBigDecimal(final int pos) throws SQLException {
    final BigDecimal rv = cstmt.getBigDecimal(pos);
    return cstmt.wasNull() ? BigDecimal.ZERO : rv;
  }
  
  public BigDecimal getBigDecimalOrNull(final int pos) throws SQLException {
    return cstmt.getBigDecimal(pos);
  }

  public Boolean getBoolOrNull(final int pos) throws SQLException {
    final boolean rv = cstmt.getBoolean(pos);
    return cstmt.wasNull() ? null : new Boolean(rv);
  }

  public boolean getBool(final int pos) throws SQLException {
    return cstmt.getBoolean(pos);
  }

  public CallableStatement getCallableStatement() {
    return cstmt;
  }

  public LocalDate getDate(final int pos) throws SQLException {
    final java.sql.Date rv = cstmt.getDate(pos);
    return cstmt.wasNull() || rv == null ? LocalDate.from(Instant.EPOCH) : rv.toLocalDate();
  }

  public LocalDate getDateOrNull(final int pos) throws SQLException {
    final java.sql.Date rv = cstmt.getDate(pos);
    return cstmt.wasNull() || rv == null ? null : rv.toLocalDate();
  }

  public LocalDateTime getDateTime(final int pos) throws SQLException {
    final java.sql.Timestamp rv = cstmt.getTimestamp(pos);
    return cstmt.wasNull() || rv == null ? LocalDateTime.from(Instant.EPOCH) : rv.toLocalDateTime();
  }

  public LocalDateTime getDateTimeOrNull(final int pos) throws SQLException {
    final java.sql.Timestamp rv = cstmt.getTimestamp(pos);
    return cstmt.wasNull() || rv == null ? null : rv.toLocalDateTime();
  }

  public double getDouble(final int pos) throws SQLException {
    return cstmt.getDouble(pos);
  }

  public Double getDoubleOrNull(final int pos) throws SQLException {
    final double rv = cstmt.getDouble(pos);
    return cstmt.wasNull() ? null : new Double(rv);
  }

  public double getFloat(final int pos) throws SQLException {
    return cstmt.getFloat(pos);
  }

  public Float getFloatOrNull(final int pos) throws SQLException {
    final float rv = cstmt.getFloat(pos);
    return cstmt.wasNull() ? null : new Float(rv);
  }

  public int getInteger(final int pos) throws SQLException {
    return cstmt.getInt(pos);
  }

  public Integer getIntegerOrNull(final int pos) throws SQLException {
    final int rv = cstmt.getInt(pos);
    return cstmt.wasNull() ? null : new Integer(rv);
  }

  public Array getArray(final int pos) throws SQLException {
    return cstmt.getArray(pos);
  }

  public Array getArrayOrNull(final int pos) throws SQLException {
    return cstmt.getArray(pos);
  }

  public long getLong(final int pos) throws SQLException {
    return cstmt.getLong(pos);
  }

  public Long getLongOrNull(final int pos) throws SQLException {
    final long rv = cstmt.getLong(pos);
    return cstmt.wasNull() ? null : new Long(rv);
  }

  public short getShort(final int pos) throws SQLException {
    return cstmt.getShort(pos);
  }

  public Short getShortOrNull(final int pos) throws SQLException {
    final short rv = cstmt.getShort(pos);
    return cstmt.wasNull() ? null : new Short(rv);
  }

  public String getString(final int pos) throws SQLException {
    final String rv = cstmt.getString(pos);
    return cstmt.wasNull() ? "" : rv;
  }

  public String getStringOrNull(final int pos) throws SQLException {
    return cstmt.getString(pos);
  }

  public boolean getStringToBool(final int pos) throws SQLException {
    final String s = getStringOrNull(pos);
    return "Y".equals(s);
  }

  public Boolean getStringToBoolOrNull(final int pos) throws SQLException {
    final String s = getStringOrNull(pos);
    return s == null ? null : new Boolean("Y".equals(s));
  }

  public void registerOutParameter(final int pos, final int type) throws SQLException {
    cstmt.registerOutParameter(pos, type);
  }

  @SuppressWarnings("unchecked")
  public <T> T getObject(int pos) throws SQLException {
    return (T) cstmt.getObject(pos);
  }
}
