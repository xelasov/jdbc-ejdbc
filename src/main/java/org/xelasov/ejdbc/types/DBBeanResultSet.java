package org.xelasov.ejdbc.types;

import java.sql.SQLException;

import org.xelasov.ejdbc.RowMapper;
import org.xelasov.ejdbc.base.ResultSetWrapper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * processes ResultSet that contains 0 or 1 rows and converts it to null or an object of type BeanT
 *
 * @author alex
 */
public class DBBeanResultSet<BeanT> extends DBResultSet<BeanT> {

  private final RowMapper<BeanT> mapper;

  public DBBeanResultSet(final RowMapper<BeanT> mapper) {
    checkNotNull(mapper);
    this.mapper = mapper;
  }

  @Override
  protected void consumeResultSet(ResultSetWrapper rsw) throws SQLException {
    if (rsw.next())
      val = mapper.mapRow(1, rsw);
  }

}
