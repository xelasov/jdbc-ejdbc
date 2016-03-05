package org.xelasov.ejdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import org.xelasov.ejdbc.base.CallableStatementWrapper;

public class ParameterList {

  private ArrayList<Parameter<?>> params = new ArrayList<Parameter<?>>();

  public ParameterList(Parameter<?>... params) {
    for (Parameter<?> p : params) {
      addParameter(p);
    }
  }

  public void addParameter(Parameter<?> p) {
    Objects.requireNonNull(p);
    params.add(p);
  }

  public void applyParams(CallableStatementWrapper stmt, int startPos) throws SQLException {
    for (Parameter<?> p : params) {
      p.apply(stmt, startPos++);
    }
  }

  public void extractParams(CallableStatementWrapper stmt, int startPos) throws SQLException {
    for (Parameter<?> p : params) {
      p.extract(stmt, startPos++);
    }
  }

  public int getParameterCount() {
    return params.size();
  }

}
