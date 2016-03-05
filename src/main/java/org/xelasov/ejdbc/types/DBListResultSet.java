package org.xelasov.ejdbc.types;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xelasov.ejdbc.RowMapper;
import org.xelasov.ejdbc.base.ResultSetWrapper;

/**
 * Converts ResultSet into a List of BeanT objects
 * 
 * @author alex
 * @param <BeanT>
 */
public class DBListResultSet<BeanT> extends DBResultSet<List<BeanT>> {

    private final RowMapper<BeanT> mapper;

    public DBListResultSet(RowMapper<BeanT> mapper) {
        this.mapper = mapper;
        val = new ArrayList<BeanT>();
    }

    @Override
    protected void consumeResultSet(ResultSetWrapper rsw) throws SQLException {
        val = new ArrayList<BeanT>();
        for (int i = 0; rsw.next(); i++) {
            val.add(mapper.mapRow(i, rsw));
        }
    }
}
