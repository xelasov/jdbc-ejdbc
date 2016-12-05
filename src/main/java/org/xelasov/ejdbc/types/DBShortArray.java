package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBShortArray extends Parameter<Short[]> {

  private static final Short[] EMPTY_ARRAY = new Short[0];

  public DBShortArray() {
    this(Parameter.Mode.out, null);
  }

  public DBShortArray(final List<Short> val) {
    this(toArray(val));
  }

  public DBShortArray(final Short[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBShortArray(Parameter.Mode mode, Short[] val) {
    super(mode, val);
  }

  private static Short[] toArray(List<Short> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Short[] val) throws SQLException {
    return (val == null) ? null : stmt.getCallableStatement().getConnection().createArrayOf("smallint", val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput()) {
      stmt.setArrayOrNull(pos, makeArray(stmt, val));
    }
    if (isOutput())
      stmt.registerOutParameter(pos, Types.ARRAY);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput()) {
      final Array arr = stmt.getArrayOrNull(pos);
      val = (arr == null) ? null : (Short[]) arr.getArray();
      arr.free();
    }
  }

  public Short[] getValue() {
    return (val == null) ? new Short[0] : val;
  }
}
