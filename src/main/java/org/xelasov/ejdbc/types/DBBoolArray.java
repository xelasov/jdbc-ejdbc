package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBBoolArray extends Parameter<Boolean[]> {

  private static final Boolean[] EMPTY_ARRAY = new Boolean[0];

  private static final String defaultTypeName = "BOOLEAN";

  private static String typeName = defaultTypeName;

  public DBBoolArray() {
    this(Parameter.Mode.out, null);
  }

  public DBBoolArray(final List<Boolean> val) {
    this(toArray(val));
  }

  public DBBoolArray(final Boolean[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBBoolArray(Parameter.Mode mode, Boolean[] val) {
    super(mode, val);
  }

  public static void setTypeName(String v) {
    if (v != null && v.length() > 0) {
      typeName = v;
    }
  }

  private static Boolean[] toArray(List<Boolean> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Boolean[] val) throws SQLException {
    return (val == null) ? null : stmt.getCallableStatement().getConnection().createArrayOf(typeName, val);
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
      val = (arr == null) ? null : (Boolean[]) arr.getArray();
      arr.free();
    }
  }

  public Boolean[] getValue() {
    return (val == null) ? EMPTY_ARRAY : val;
  }
}
