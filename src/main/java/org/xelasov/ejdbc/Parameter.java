package org.xelasov.ejdbc;

import java.sql.SQLException;

import org.xelasov.ejdbc.base.CallableStatementWrapper;

public abstract class Parameter<ValueT> {

  public enum Mode {
    in,
    out,
    inout
  }

  private final Mode   mode;
  protected     ValueT val;

  protected Parameter(Mode mode, ValueT val) {
    this.mode = mode;
    this.val = val;
  }

  public abstract void apply(CallableStatementWrapper stmt, int pos) throws SQLException;

  public abstract void extract(CallableStatementWrapper stmt, int pos) throws SQLException;

  public boolean isOutput() {
    return mode == Mode.out || mode == Mode.inout;
  }

  public boolean isInput() {
    return mode == Mode.in || mode == Mode.inout;
  }

  public ValueT getValueOrNull() {
    return val;
  }

  public Mode getMode() {
    return mode;
  }

}
