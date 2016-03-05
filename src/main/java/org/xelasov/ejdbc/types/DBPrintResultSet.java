package org.xelasov.ejdbc.types;

import java.io.PrintStream;
import java.sql.SQLException;

import org.xelasov.ejdbc.base.ResultSetWrapper;
import org.xelasov.ejdbc.base.SqlUtils;

public class DBPrintResultSet extends DBResultSet<Void> {

  private final PrintStream out;

  public DBPrintResultSet(PrintStream out) {
    this.out = out;
  }

  @Override
  protected void consumeResultSet(ResultSetWrapper rsw) throws SQLException {

    SqlUtils.printResultSet(rsw, out);
  }

}
