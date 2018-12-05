package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AppModelTests extends SugarTest {
    public void setup() throws Exception {
        sugar().login();
    }

    @Test
    public void getCurrentUserTest() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        assertTrue("Wrong admin user name", sugar().getCurrentUser().equals("Administrator"));

        sugar().logout();

        sugar().login(sugar().users.getQAUser());

        assertTrue("Wrong qauser user name", sugar().getCurrentUser().equals("qauser"));

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {
        sugar().logout();
    }
}
