package org.xelasov.ejdbc.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

public class ResultSetWrapper {

  private final ResultSet rs;

  public ResultSetWrapper(final ResultSet rs) {
    checkArgument(rs != null);
    this.rs = rs;
  }

  public boolean getBool(final int pos) throws SQLException {
    return rs.getBoolean(pos);
  }

  public Boolean getBoolOrNull(final int pos) throws SQLException {
    final boolean rv = rs.getBoolean(pos);
    return rs.wasNull() ? null : new Boolean(rv);
  }

  public byte getByte(final int pos) throws SQLException {
    return rs.getByte(pos);
  }

  public Byte getByteOrNull(final int pos) throws SQLException {
    final byte rv = rs.getByte(pos);
    return rs.wasNull() ? null : new Byte(rv);
  }

  public boolean getByteBool(final int pos) throws SQLException {
    final byte b = getByte(pos);
    return 1 == b;
  }

  public Boolean getByteBoolOrNull(final int pos) throws SQLException {
    final Byte b = getByteOrNull(pos);
    if (b == null)
      return null;
    else
      return new Boolean(1 == b.byteValue());
  }

  public Date getDate(final int pos) throws SQLException {
    final java.sql.Date rv = rs.getDate(pos);
    return rs.wasNull() || rv == null ? new Date(0) : new Date(rv.getTime());
  }

  public Date getDateOrNull(final int pos) throws SQLException {
    final java.sql.Date rv = rs.getDate(pos);
    return rs.wasNull() || rv == null ? null : new Date(rv.getTime());
  }

  public Date getDateTime(final int pos) throws SQLException {
    final java.sql.Timestamp rv = rs.getTimestamp(pos);
    return rs.wasNull() || rv == null ? new Date(0) : new Date(rv.getTime());
  }

  public Date getDateTimeOrNull(final int pos) throws SQLException {
    final java.sql.Timestamp rv = rs.getTimestamp(pos);
    return rs.wasNull() || rv == null ? null : new Date(rv.getTime());
  }

  public double getDouble(final int pos) throws SQLException {
    return rs.getDouble(pos);
  }

  public Double getDoubleOrNull(final int pos) throws SQLException {
    final double rv = rs.getDouble(pos);
    return rs.wasNull() ? null : new Double(rv);
  }

  public float getFloat(final int pos) throws SQLException {
    return rs.getFloat(pos);
  }

  public Float getFloatOrNull(final int pos) throws SQLException {
    final float rv = rs.getFloat(pos);
    return rs.wasNull() ? null : new Float(rv);
  }

  public int getInteger(final int pos) throws SQLException {
    return rs.getInt(pos);
  }

  public Integer getIntegerOrNull(final int pos) throws SQLException {
    final int rv = rs.getInt(pos);
    return rs.wasNull() ? null : new Integer(rv);
  }

  public long getLong(final int pos) throws SQLException {
    return rs.getLong(pos);
  }

  public Long getLongOrNull(final int pos) throws SQLException {
    final long rv = rs.getLong(pos);
    return rs.wasNull() ? null : new Long(rv);
  }

  public ResultSet getResultSet() {
    return rs;
  }

  public boolean next() throws SQLException {
    return rs.next();
  }

  public short getShort(final int pos) throws SQLException {
    return rs.getShort(pos);
  }

  public Short getShortOrNull(final int pos) throws SQLException {
    final short rv = rs.getShort(pos);
    return rs.wasNull() ? null : new Short(rv);
  }

  public String getString(final int pos) throws SQLException {
    final String rv = rs.getString(pos);
    return rs.wasNull() || rv == null ? "" : rv;
  }

  public String getStringOrNull(final int pos) throws SQLException {
    return rs.getString(pos);
  }

  public Date getTime(final int pos) throws SQLException {
    final java.sql.Time rv = rs.getTime(pos);
    return rs.wasNull() || rv == null ? new Date(0) : new Date(rv.getTime());
  }

  public Date getTimeOrNull(final int pos) throws SQLException {
    final java.sql.Time rv = rs.getTime(pos);
    return rs.wasNull() || rv == null ? null : new Date(rv.getTime());
  }

}
