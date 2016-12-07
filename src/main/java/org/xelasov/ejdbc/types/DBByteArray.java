package org.xelasov.ejdbc.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBByteArray extends Parameter<Byte[]> {

  private static final Byte[] EMPTY_ARRAY = new Byte[0];

  private static final String defaultTypeName = "BYTE";

  private static String typeName = defaultTypeName;

  public DBByteArray() {
    this(Parameter.Mode.out, null);
  }

  public DBByteArray(final List<Byte> val) {
    this(toArray(val));
  }

  public DBByteArray(final Byte[] val) {
    this(Parameter.Mode.in, val);
  }

  public DBByteArray(Parameter.Mode mode, Byte[] val) {
    super(mode, val);
  }

  public static void setTypeName(String v) {
    if (v != null && v.length() > 0) {
      typeName = v;
    }
  }

  private static Byte[] toArray(List<Byte> list) {
    return (list == null) ? null : list.toArray(EMPTY_ARRAY);
  }

  private static Array makeArray(CallableStatementWrapper stmt, Byte[] val) throws SQLException {
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
      val = (arr == null) ? null : (Byte[]) arr.getArray();
      arr.free();
    }
  }

  public Byte[] getValue() {
    return (val == null) ? EMPTY_ARRAY : val;
  }
}