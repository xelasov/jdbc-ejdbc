package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBLongArray extends Parameter<Long[]> {

  private static final Long[] EMPTY_ARRAY = new Long[0];

  public DBLongArray() {
    this(Parameter.Mode.out, null);
  }

  public DBLongArray(final List<Long> val) {
    this(toArray(val));
  }

  public DBLongArray(final Long[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBLongArray(Parameter.Mode mode, Long[] val) {
    super(mode, val);
  }

  private static Long[] toArray(List<Long> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput()) {
      final Array arr = stmt.getCallableStatement().getConnection().createArrayOf("int8", val);
      stmt.setArray(pos, arr);
    }
    if (isOutput())
      stmt.registerOutParameter(pos, Types.ARRAY);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput()) {
      final Array arr = stmt.getArrayOrNull(pos);
      val = (arr == null) ? null : (Long[]) arr.getArray();
      arr.free();
    }
  }

  public Long[] getValue() {
    return (val == null) ? new Long[0] : val;
  }
}
