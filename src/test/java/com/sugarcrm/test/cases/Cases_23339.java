package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Dmitry Todarev <dtodarev@sugarcrm.com>
 */
public class Cases_23339 extends SugarTest {

    CaseRecord myCase;

    public void setup() throws Exception {
        sugar().login();
        sugar().accounts.api.create();
        myCase = (CaseRecord)sugar().cases.create();
    }

    /**
     * Test Case 23339: Delete Case_Verify that case is not deleted when clicking "Cancel" button in the pop up confirm dialog box.
     * */
    @Test
    public void Cases_23339_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        myCase.navToRecord();

        // Click Delete
        sugar().cases.recordView.delete();

        // And cancel deletion
        sugar().alerts.getAlert().cancelAlert();

        // Verify that record hasn't been changed
        myCase.verify();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}
