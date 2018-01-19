package org.xelasov.ejdbc;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;
import org.xelasov.ejdbc.base.Assert;
import org.xelasov.ejdbc.base.CallableStatementWrapper;
import org.xelasov.ejdbc.base.SqlUtils;
import org.xelasov.ejdbc.types.DBBigDecimal;
import org.xelasov.ejdbc.types.DBBool;
import org.xelasov.ejdbc.types.DBByte;
import org.xelasov.ejdbc.types.DBDate;
import org.xelasov.ejdbc.types.DBDateTime;
import org.xelasov.ejdbc.types.DBDouble;
import org.xelasov.ejdbc.types.DBFloat;
import org.xelasov.ejdbc.types.DBInteger;
import org.xelasov.ejdbc.types.DBIntegerArray;
import org.xelasov.ejdbc.types.DBLong;
import org.xelasov.ejdbc.types.DBLongArray;
import org.xelasov.ejdbc.types.DBShort;
import org.xelasov.ejdbc.types.DBShortArray;
import org.xelasov.ejdbc.types.DBString;
import org.xelasov.ejdbc.types.DBStringArray;
import org.xelasov.ejdbc.types.DBZonedDateTime;

/**
 * Function class is a simple way to execute a database function-style stored procedure (one that returns a single return value).
 * Output parameters are also supported.
 * <p>
 * For example, to call a sproc that takes user id and password, creates a 'user' record and returns its PK as a long:
 * <p>
 * Long pk = new Function<Long>(new DBLong(), "account.user_add").inString(userId).inString(passwd).execute(dataSource);
 * <p>
 * To call a sproc that takes user PK and returns a single-row ResultSet with all fields in the user record:
 * <p>
 * User user = new Function<User>(new DBBeanResultSet<User>(new UserRowMapper()), "account.user_get").inLong(userPK).execute(dataSource);
 */
public class Function<RetValT> {

  private final String             name;
  private final Parameter<RetValT> retVal;
  private final ParameterList      params;

  public Function(final String name, final Parameter<?>... params) {
    this(null, name, params);
  }

  public Function(final Parameter<RetValT> retVal, final String name, final Parameter<?>... params) {
    this(retVal, name, new ParameterList(params));
  }

  public Function(final String name, ParameterList params) {
    this(null, name, params);
  }

  public Function(Parameter<RetValT> retVal, final String name, ParameterList params) {
    Assert.argument(name != null && !name.isEmpty());
    Assert.argument(retVal == null || retVal.isOutput());
    Assert.argumentNotNull(params);

    this.name = name;
    this.retVal = retVal;
    this.params = params;
  }

  public Function<RetValT> inString(String v) {
    params.addParameter(new DBString(v));
    return this;
  }

  public Function<RetValT> inBool(Boolean v) {
    params.addParameter(new DBBool(v));
    return this;
  }

  public Function<RetValT> inBigDecimal(BigDecimal v) {
    params.addParameter(new DBBigDecimal(v));
    return this;
  }

  public Function<RetValT> inByte(Byte v) {
    params.addParameter(new DBByte(v));
    return this;
  }

  public Function<RetValT> inShort(Short v) {
    params.addParameter(new DBShort(v));
    return this;
  }

  public Function<RetValT> inInteger(Integer v) {
    params.addParameter(new DBInteger(v));
    return this;
  }

  public Function<RetValT> inLong(Long v) {
    params.addParameter(new DBLong(v));
    return this;
  }

  public Function<RetValT> inFloat(Float v) {
    params.addParameter(new DBFloat(v));
    return this;
  }

  public Function<RetValT> inDouble(Double v) {
    params.addParameter(new DBDouble(v));
    return this;
  }

  public Function<RetValT> inDate(LocalDate v) {
    params.addParameter(new DBDate(v));
    return this;
  }

  public Function<RetValT> inDateTime(LocalDateTime v) {
    params.addParameter(new DBDateTime(v));
    return this;
  }

  public Function<RetValT> inZonedDateTime(ZonedDateTime v) {
    params.addParameter(new DBZonedDateTime(v));
    return this;
  }

  public Function<RetValT> inShortArray(Short[] v) {
    params.addParameter(new DBShortArray(v));
    return this;
  }

  public Function<RetValT> inShortArray(List<Short> v) {
    params.addParameter(new DBShortArray(v));
    return this;
  }

  public Function<RetValT> inLongArray(Long[] v) {
    params.addParameter(new DBLongArray(v));
    return this;
  }

  public Function<RetValT> inLongArray(List<Long> v) {
    params.addParameter(new DBLongArray(v));
    return this;
  }

  public Function<RetValT> inIntegerArray(Integer[] v) {
    params.addParameter(new DBIntegerArray(v));
    return this;
  }

  public Function<RetValT> inIntegerArray(List<Integer> v) {
    params.addParameter(new DBIntegerArray(v));
    return this;
  }

  public Function<RetValT> inStringArray(String[] v) {
    params.addParameter(new DBStringArray(v));
    return this;
  }

  public Function<RetValT> inStringArray(List<String> v) {
    params.addParameter(new DBStringArray(v));
    return this;
  }

  public RetValT execute(Connection conn) throws SQLException {
    Assert.argumentNotNull(conn);

    CallableStatement stmt = null;
    try {
      final String sql = SqlUtils.buildFunctionCallString(name, retVal != null, params.getParameterCount());
      stmt = conn.prepareCall(sql);
      final CallableStatementWrapper csw = new CallableStatementWrapper(stmt);
      applyParams(csw);
      stmt.execute();
      extractParams(csw);
      return retVal != null ? retVal.getValueOrNull() : null;
    } finally {
      SqlUtils.closeSafely(stmt);
    }
  }

  public RetValT execute(DataSource ds) throws SQLException {
    Assert.argumentNotNull(ds);

    final Connection conn = SqlUtils.getConnection(ds, false);
    try {
      final RetValT rv = execute(conn);
      SqlUtils.commitSafely(conn);
      return rv;
    } catch (SQLException e) {
      SqlUtils.rollbackSafely(conn);
      throw e;
    } finally {
      SqlUtils.closeSafely(conn);
    }
  }

  private void extractParams(CallableStatementWrapper stmt) throws SQLException {
    int pos = 1;
    if (retVal != null)
      retVal.extract(stmt, pos++);
    params.extractParams(stmt, pos);
  }

  private void applyParams(CallableStatementWrapper stmt) throws SQLException {
    int pos = 1;
    if (retVal != null)
      retVal.apply(stmt, pos++);
    params.applyParams(stmt, pos);
  }

}
