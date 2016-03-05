package org.xelasov.ejdbc.base;

import org.junit.Assert;
import org.junit.Test;

public class SqlUtilsTest {

    @Test
    public void testBuildSqlString() {
        Assert.assertEquals("{ ? = call get_cnt(?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 1));
        Assert.assertEquals("{ ? = call get_cnt(?,?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 2));
        Assert.assertEquals("{ ? = call get_cnt(?,?,?) }", SqlUtils.buildFunctionCallString("get_cnt", true, 3));

        Assert.assertEquals("{ call get_cnt(?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 1));
        Assert.assertEquals("{ call get_cnt(?,?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 2));
        Assert.assertEquals("{ call get_cnt(?,?,?) }", SqlUtils.buildFunctionCallString("get_cnt", false, 3));

    }

}
