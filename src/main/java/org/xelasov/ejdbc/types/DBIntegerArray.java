package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBIntegerArray extends Parameter<Integer[]> {

  private static final Integer[] EMPTY_ARRAY = new Integer[0];

  public DBIntegerArray() {
    this(Parameter.Mode.out, null);
  }

  public DBIntegerArray(final List<Integer> val) {
    this(toArray(val));
  }

  public DBIntegerArray(final Integer[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBIntegerArray(Parameter.Mode mode, Integer[] val) {
    super(mode, val);
  }

  private static Integer[] toArray(List<Integer> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Integer[] val) throws SQLException {
    return (val == null) ? null : stmt.getCallableStatement().getConnection().createArrayOf("int", val);
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
      val = (arr == null) ? null : (Integer[]) arr.getArray();
      arr.free();
    }
  }

  public Integer[] getValue() {
    return (val == null) ? new Integer[0] : val;
  }
}
