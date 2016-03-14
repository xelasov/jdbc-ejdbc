package org.xelasov.ejdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.xelasov.ejdbc.base.ResultSetWrapper;
import org.xelasov.ejdbc.base.SqlUtils;
import org.xelasov.ejdbc.types.DBResultSet;

public class Query<RetValT> {
  private final String               sql;
  private final DBResultSet<RetValT> retVal;

  public Query(final DBResultSet<RetValT> retVal, final String sql) {
    this.sql = sql;
    this.retVal = retVal;
  }

  public RetValT execute(Connection conn) throws SQLException {

    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      final ResultSet        rs  = stmt.executeQuery(this.sql);
      final ResultSetWrapper rsw = new ResultSetWrapper(rs);
      retVal.consumeResultSet(rsw);
      return retVal.getValueOrNull();
    } finally {
      SqlUtils.closeSafely(stmt);
    }
  }

  public RetValT execute(DataSource ds) throws SQLException {
    final Connection conn = SqlUtils.getConnection(ds, false);
    try {
      final RetValT rv = execute(conn);
      SqlUtils.commitSafely(conn);
      return rv;
    } catch (SQLException e) {
      SqlUtils.rollbackSafely(conn);
      throw e;
    } finally {
      SqlUtils.closeSafely(conn);
    }
  }
}
