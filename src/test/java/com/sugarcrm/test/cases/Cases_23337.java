package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Dmitry Todarev <dtodarev@sugarcrm.com>
 */
public class Cases_23337 extends SugarTest {

    AccountRecord myAcc;
    CaseRecord myCase;
    FieldSet caseData;
    FieldSet customData;

    public void setup() throws Exception {
        caseData = sugar().cases.getDefaultData();
        customData = testData.get(testName).get(0);
        sugar().login();
        myAcc = (AccountRecord)sugar().accounts.api.create();
        myCase = (CaseRecord)sugar().cases.create();
    }

    /**
     * Test Case 23337: Edit Case_Verify that case is not changed when using "Cancel" function in "Case" edit view.
     * */
    @Test
    public void Cases_23337_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        myCase.navToRecord();

        // Click Edit button
        sugar().cases.recordView.edit();

        // Set case subject empty and try to save a case
        sugar().cases.recordView.setFields(customData);

        // And cancel edition
        sugar().cases.recordView.cancel();

        // Verify that record hasn't been changed
        myCase.verify(caseData);

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}
