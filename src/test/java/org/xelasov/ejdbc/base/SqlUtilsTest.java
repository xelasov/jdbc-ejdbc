package org.xelasov.ejdbc.base;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class SqlUtilsTest {

    @Test
    public void testBuildSqlString() {
        assertEquals("{ ? = call get_cnt(?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 1));
        assertEquals("{ ? = call get_cnt(?,?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 2));
        assertEquals("{ ? = call get_cnt(?,?,?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 3));

        assertEquals("{ call get_cnt(?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 1));
        assertEquals("{ call get_cnt(?,?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 2));
        assertEquals("{ call get_cnt(?,?,?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 3));

    }

}
