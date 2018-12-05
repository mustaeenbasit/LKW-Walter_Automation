package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_23273 extends SugarTest {
        FieldSet caseRecord;
        CaseRecord myCase;

        public void setup() throws Exception {
        		caseRecord = testData.get("Cases_23273").get(0);
                sugar().login();
                sugar().accounts.api.create();
                myCase = (CaseRecord) sugar().cases.api.create();
                myCase.navToRecord();
        }

        /**
         * Verify that case can be edited when using "Edit" function in "Case" detail view.
         */
        @Test
        public void Cases_23273_execute() throws Exception {
                VoodooUtils.voodoo.log.info("Running " + testName + "...");

                myCase.edit(caseRecord);

                // Verify the modified records is displayed in the detail view
                myCase.verify(caseRecord);

                VoodooUtils.voodoo.log.info(testName + " complete.");
        }

        public void cleanup() throws Exception {}
}
