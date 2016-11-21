package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBStringArray extends Parameter<String[]> {

  private static final String[] EMPTY_ARRAY = new String[0];

  public DBStringArray() {
    this(Parameter.Mode.out, null);
  }

  public DBStringArray(final List<String> val) {
    this(toArray(val));
  }

  public DBStringArray(final String[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBStringArray(Parameter.Mode mode, String[] val) {
    super(mode, val);
  }

  private static String[] toArray(List<String> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, String[] val) throws SQLException {
    return (val == null) ? null : stmt.getCallableStatement().getConnection().createArrayOf("text", val);
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
      val = (arr == null) ? null : (String[]) arr.getArray();
      arr.free();
    }
  }

  public String[] getValue() {
    return (val == null) ? new String[0] : val;
  }
}