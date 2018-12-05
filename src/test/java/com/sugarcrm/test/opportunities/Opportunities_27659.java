package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27659 extends SugarTest {
    AccountRecord myAccount;

    public void setup() throws Exception {
        myAccount = (AccountRecord) sugar().accounts.api.create();
        sugar().login();
    }

    /**
     * Verify that Sales Stage rolls up based on open RLIs, unless all are closed, when switch from Opps+RLIs to Opps mode
     *
     * @throws Exception
     */
    @Test
    public void Opportunities_27659_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        DataSource customData = testData.get(testName);
        sugar().opportunities.navToListView();
        sugar().opportunities.listView.create();
        VoodooControl oppName = sugar().opportunities.createDrawer.getEditField("name");
        VoodooControl accName = sugar().opportunities.createDrawer.getEditField("relAccountName");
        VoodooControl rliName = sugar().opportunities.createDrawer.getEditField("rli_name");
        VoodooControl rliExpectedClosedDate = sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date");
        VoodooControl rliLikely = sugar().opportunities.createDrawer.getEditField("rli_likely");
        VoodooControl rliStage = sugar().opportunities.createDrawer.getEditField("rli_stage");

        // TODO: VOOD-1357 and VOOD-609
        VoodooControl addSecondRow = new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(1) .addBtn");
        String secondRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(2)";
        VoodooControl secondRLIName = new VoodooControl("input", "css", secondRLIRecord + " .fld_name.edit input");
        VoodooDate secondRLIExpectedClosedDate = new VoodooDate("input", "css", secondRLIRecord + " .fld_date_closed.edit input");
        VoodooControl secondRLILikely = new VoodooControl("input", "css", secondRLIRecord + " .fld_likely_case.edit input");
        VoodooSelect secondRLIStage = new VoodooSelect("a", "css", secondRLIRecord + " .fld_sales_stage.edit a");

        // First Opportunity with 2 rli's whose stages are "Closed Lost"
        oppName.set(customData.get(0).get("opp_name"));
        accName.set(myAccount.getRecordIdentifier());
        rliName.set(customData.get(0).get("rli_name"));
        rliExpectedClosedDate.set(customData.get(0).get("expected_closed_date"));
        rliLikely.set(customData.get(0).get("likely"));
        rliStage.scrollIntoViewIfNeeded(false);
        rliStage.set(customData.get(0).get("stage"));

        // Add new RLI row and filled with data
        addSecondRow.click();
        VoodooUtils.waitForReady();
        secondRLIName.set(customData.get(1).get("rli_name"));
        secondRLIExpectedClosedDate.set(customData.get(1).get("expected_closed_date"));
        secondRLILikely.set(customData.get(1).get("likely"));
        secondRLIStage.scrollIntoViewIfNeeded(false);
        secondRLIStage.set(customData.get(1).get("stage"));
        sugar().opportunities.createDrawer.save();

        // 2nd Opportunity with 2 rli's whose stages are equal to both "Closed Lost" & "Closed Won"
        sugar().opportunities.listView.create();
        oppName.set(customData.get(1).get("opp_name"));
        accName.set(myAccount.getRecordIdentifier());
        rliName.set(customData.get(0).get("rli_name"));
        rliExpectedClosedDate.set(customData.get(0).get("expected_closed_date"));
        rliLikely.set(customData.get(0).get("likely"));
        rliStage.scrollIntoViewIfNeeded(false);
        rliStage.set(customData.get(0).get("stage"));

        // Add new RLI row and filled with data
        addSecondRow.click();
        VoodooUtils.waitForReady();
        secondRLIName.set(customData.get(2).get("rli_name"));
        secondRLIExpectedClosedDate.set(customData.get(2).get("expected_closed_date"));
        secondRLILikely.set(customData.get(2).get("likely"));
        secondRLIStage.scrollIntoViewIfNeeded(false);
        secondRLIStage.set(customData.get(2).get("stage"));
        sugar().opportunities.createDrawer.save();

        // 3rd Opportunity with 3 rli's whose stages are "Closed Won", "Prospecting", "Perception Analysis"
        sugar().opportunities.listView.create();
        oppName.set(customData.get(2).get("opp_name"));
        accName.set(myAccount.getRecordIdentifier());
        rliName.set(customData.get(2).get("rli_name"));
        rliExpectedClosedDate.set(customData.get(2).get("expected_closed_date"));
        rliLikely.set(customData.get(2).get("likely"));
        rliStage.scrollIntoViewIfNeeded(false);
        rliStage.set(customData.get(2).get("stage"));

        // Add new RLI row and filled with data
        addSecondRow.click();
        VoodooUtils.waitForReady();
        secondRLIName.set(customData.get(3).get("rli_name"));
        secondRLIExpectedClosedDate.set(customData.get(3).get("expected_closed_date"));
        secondRLILikely.set(customData.get(3).get("likely"));
        secondRLIStage.scrollIntoViewIfNeeded(false);
        secondRLIStage.set(customData.get(3).get("stage"));

        // Add new RLI row and filled with data
        new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(2) .addBtn").click();
        String thirdRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tr.single:nth-of-type(3)";
        new VoodooControl("input", "css", thirdRLIRecord + " .fld_name.edit input").set(customData.get(4).get("rli_name"));
        
        new VoodooDate("input", "css", thirdRLIRecord + " .fld_date_closed.edit input").set(customData.get(4).get("expected_closed_date"));
        new VoodooControl("input", "css", thirdRLIRecord + " .fld_likely_case.edit input").set(customData.get(4).get("likely"));
        VoodooSelect thirdRLIStage = new VoodooSelect("a", "css", thirdRLIRecord + " .fld_sales_stage.edit a");
        thirdRLIStage.scrollIntoViewIfNeeded(false);
        thirdRLIStage.set(customData.get(4).get("stage"));
        sugar().opportunities.createDrawer.save();

        // Switch to opportunities view only
        sugar().admin.api.switchToOpportunitiesView();
        sugar().opportunities.navToListView();

        // Verify sales stage field for opp3 is Prospecting
        sugar().opportunities.listView.getDetailField(1, "salesStage").assertEquals(customData.get(3).get("stage"), true);
        // Verify sales stage field for opp2 is Closed Won
        sugar().opportunities.listView.getDetailField(2, "salesStage").assertEquals(customData.get(2).get("stage"), true);
        // Verify sales stage field for opp1 is Closed Lost
        sugar().opportunities.listView.getDetailField(3, "salesStage").assertEquals(customData.get(0).get("stage"), true);

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}