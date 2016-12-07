package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBDoubleArray extends Parameter<Double[]> {

  private static final Double[] EMPTY_ARRAY = new Double[0];

  private static final String defaultTypeName = "DOUBLE";

  private static String typeName = defaultTypeName;

  public DBDoubleArray() {
    this(Parameter.Mode.out, null);
  }

  public DBDoubleArray(final List<Double> val) {
    this(toArray(val));
  }

  public DBDoubleArray(final Double[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBDoubleArray(Parameter.Mode mode, Double[] val) {
    super(mode, val);
  }

  public static void setTypeName(String v) {
    if (v != null && v.length() > 0) {
      typeName = v;
    }
  }

  private static Double[] toArray(List<Double> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Double[] val) throws SQLException {
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
      val = (arr == null) ? null : (Double[]) arr.getArray();
      arr.free();
    }
  }

  public Double[] getValue() {
    return (val == null) ? EMPTY_ARRAY : val;
  }
}