package com.sugarcrm.test.campaigns;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Campaigns_19610 extends SugarTest {
    DataSource ds = new DataSource();

    public void setup() throws Exception {
        ds = testData.get(testName);
        sugar().login();
        // create 3 targetlist records
        sugar().targetlists.create(ds);
    }

    /**
     * Newsletter - Campaign Wizard_Verify that only one subscription list, one unsubscription list and one test list can be associated with newsletter.
     *
     * @throws Exception
     */
    @Test
    public void Campaigns_19610_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Define controls for Campaigns
        // TODO: VOOD-1072
        VoodooControl newsLetterTypeCtrl = new VoodooControl("input", "id", "wizardtype_nl");
        VoodooControl startBtnCtrl = new VoodooControl("input", "id", "startbutton");
        VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");
        VoodooControl submitBtnCtrl = new VoodooControl("input", "id", "wiz_submit_finish_button");

        // Get Current Date
        String endDate = VoodooUtils.getCurrentTimeStamp("MM/dd/YYYY");

        // Go to Campaign module and Create Campaign Wizard (Newsletter)
        sugar().navbar.selectMenuItem(sugar().campaigns, "createCampaignWizard");
        VoodooUtils.focusFrame("bwc-frame");
        // select the newsletter
        newsLetterTypeCtrl.click();
        // click on the start button
        startBtnCtrl.click();
        VoodooUtils.waitForReady();

        // Fill in the required fields campaign Header and Navigate to Campaign Subscription Page
        sugar().campaigns.editView.getEditField("name").set(testName);
        sugar().campaigns.editView.getEditField("date_end").set(endDate);
        nextBtnCtrl.click();
        nextBtnCtrl.click();
        nextBtnCtrl.click();
        submitBtnCtrl.click();
        VoodooUtils.waitForReady();

        VoodooControl targetNameCtrl = new VoodooControl("input", "id", "name_advanced");
        VoodooControl searchFormSubmit = new VoodooControl("input", "css", "#search_form_submit");
        VoodooControl listItem = new VoodooControl("a", "css", "tr.oddListRowS1  td:nth-child(1) a");
        VoodooControl campaignTargetsList = new VoodooControl("div", "css", "#campaign_targets");

        // Verify that three target lists records auto-created
        campaignTargetsList.assertContains(ds.get(0).get("description"), true);
        campaignTargetsList.assertContains(ds.get(1).get("description"), true);
        campaignTargetsList.assertContains(ds.get(2).get("description"), true);

        // Go to "Subscription Information" page.
        new VoodooControl("a", "css", "#nav_step4 a").click();
        VoodooUtils.waitForReady();
        VoodooUtils.focusFrame("bwc-frame");
        new VoodooControl("input", "css", "#wiz_step3_subscription_name_button").click();
        VoodooUtils.focusWindow(1);
        // Select different target lists and click "Finish" button.
        targetNameCtrl.set(ds.get(0).get("targetlistName"));
        searchFormSubmit.click();
        listItem.click();

        VoodooUtils.focusWindow(0);
        VoodooUtils.focusFrame("bwc-frame");
        // Select different target lists and click "Finish" button.
        new VoodooControl("input", "css", "#wiz_step3_unsubscription_name_button").click();
        VoodooUtils.focusWindow(1);
        targetNameCtrl.set(ds.get(1).get("targetlistName"));
        searchFormSubmit.click();
        listItem.click();

        VoodooUtils.focusWindow(0);
        VoodooUtils.focusFrame("bwc-frame");
        // Select different target lists and click "Finish" button.
        new VoodooControl("input", "css", "#wiz_step3_test_name_button").click();
        VoodooUtils.focusWindow(1);
        targetNameCtrl.set(ds.get(2).get("targetlistName"));
        searchFormSubmit.click();
        listItem.click();
        VoodooUtils.focusWindow(0);
        VoodooUtils.focusFrame("bwc-frame");
        submitBtnCtrl.click();
        VoodooUtils.waitForReady();

        // Verify Only three target lists are displayed on "Campaign Summary" page.
        campaignTargetsList.assertContains(ds.get(0).get("targetlistName"), true);
        campaignTargetsList.assertContains(ds.get(1).get("targetlistName"), true);
        campaignTargetsList.assertContains(ds.get(2).get("targetlistName"), true);

        campaignTargetsList.assertContains(ds.get(0).get("description"), false);
        campaignTargetsList.assertContains(ds.get(1).get("description"), false);
        campaignTargetsList.assertContains(ds.get(2).get("description"), false);
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}