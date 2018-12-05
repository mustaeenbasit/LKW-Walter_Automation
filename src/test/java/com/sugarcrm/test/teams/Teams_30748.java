package com.sugarcrm.test.teams;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Teams_30748 extends SugarTest {
    public void setup() throws Exception {
        sugar().login();
    }

    /**
     * Verify the Assigned to Team should show in the Team field by default when assigned to person is not in the Team
     *
     * @throws Exception
     */
    @Test
    public void Teams_30748_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        FieldSet fs = new FieldSet();
        fs.put("relTeam", sugar().users.getQAUser().get("userName"));
        // Create a Lead record and set the following that Assigned to person is not in the Team field.
        sugar().leads.create(fs);

        //  Open the record to observe the "Teams" field.
        sugar().leads.listView.clickRecord(1);
        sugar().leads.recordView.getDetailField("relAssignedTo").assertEquals(sugar().leads.getDefaultData().get("relAssignedTo"), true);
        sugar().leads.recordView.getDetailField("relTeam").scrollIntoViewIfNeeded(false);

        FieldSet teamRecord = testData.get(testName).get(0);
        // TODO: VOOD-1746
        VoodooControl teamCtrl = new VoodooControl("span", "css", ".fld_team_name.detail");
        // Verify that "qauser" (primary) and "Administrator" Teams displayed in the Team field.
        teamCtrl.assertContains(sugar().users.getQAUser().get("userName") + " " + teamRecord.get("team_status"), true);
        sugar().leads.recordView.getDetailField("relTeam").assertContains(sugar().leads.getDefaultData().get("relAssignedTo"), true);

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}