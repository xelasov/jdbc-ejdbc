package org.xelasov.ejdbc;


import java.sql.SQLException;

import org.xelasov.ejdbc.base.ResultSetWrapper;

public interface RowMapper<BeanT> {

    /** 
     * @param rowIndex
     *            - 0-based index of the current row in the result set
     * @param rsw
     *            - allows safe reading of the underlying ResultSet
     * @return instance of BeanT created from fields in the current row of the RSWrapperT object
     * @throws SQLException
     */
    BeanT mapRow(final int rowIndex, final ResultSetWrapper rsw) throws SQLException;

}
