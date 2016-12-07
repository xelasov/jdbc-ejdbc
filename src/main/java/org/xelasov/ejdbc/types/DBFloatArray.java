package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBFloatArray extends Parameter<Float[]> {

  private static final Float[] EMPTY_ARRAY = new Float[0];

  private static final String defaultTypeName = "FLOAT";

  private static String typeName = defaultTypeName;

  public DBFloatArray() {
    this(Parameter.Mode.out, null);
  }

  public DBFloatArray(final List<Float> val) {
    this(toArray(val));
  }

  public DBFloatArray(final Float[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBFloatArray(Parameter.Mode mode, Float[] val) {
    super(mode, val);
  }

  public static void setTypeName(String v) {
    if (v != null && v.length() > 0) {
      typeName = v;
    }
  }

  private static Float[] toArray(List<Float> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Float[] val) throws SQLException {
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
      val = (arr == null) ? null : (Float[]) arr.getArray();
      arr.free();
    }
  }

  public Float[] getValue() {
    return (val == null) ? EMPTY_ARRAY : val;
  }
}