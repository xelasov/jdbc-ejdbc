package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.sql.Types;

import org.xelasov.ejdbc.Parameter;
import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class DBByte extends Parameter<Byte> {

  public DBByte() {
    this(Parameter.Mode.out, null);
  }

  public DBByte(final Byte val) {
    this(Parameter.Mode.in, val);
  }

  public DBByte(Parameter.Mode mode, Byte val) {
    super(mode, val);
  }

  @Override
  public void apply(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isInput())
      stmt.setByteOrNull(pos, val);
    if (isOutput())
      stmt.registerOutParameter(pos, Types.TINYINT);
  }

  @Override
  public void extract(CallableStatementWrapper stmt, int pos) throws SQLException {
    if (isOutput())
      val = stmt.getByteOrNull(pos);
  }

  public byte getValue() {
    return (val == null) ? 0 : val.byteValue();
  }
}
