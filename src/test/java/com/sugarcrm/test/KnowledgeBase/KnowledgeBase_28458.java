package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28458 extends SugarTest {
    UserRecord myUser;

    public void setup() throws Exception {
        // Login as Admin user
        sugar().login();

        // Enable KB module
        sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

        // Create a regular user
        // TODO: VOOD-1200
        myUser = (UserRecord) sugar().users.create();

        // Logout from admin user and login as QAUser
        sugar().logout();
        sugar().login(sugar().users.getQAUser());

        // QAUser create a new KB that is not published; make sure assigned user Team is QAUser itself
        // TODO: VOOD-444
        sugar().knowledgeBase.navToListView();
        sugar().knowledgeBase.listView.create();
        sugar().knowledgeBase.createDrawer.showMore();
        sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
        String qauser = sugar().users.getQAUser().get("userName");
        sugar().knowledgeBase.createDrawer.getEditField("relAssignedTo").set(qauser);
        sugar().knowledgeBase.createDrawer.getEditField("relTeam").set(qauser);
        sugar().knowledgeBase.createDrawer.save();
        if (sugar().alerts.getSuccess().queryVisible())
            sugar().alerts.getSuccess().closeAlert();
    }

    /**
     * Verify that non-publised & non-owned KB should NOT be searched/viewed/edited by non-owner 
     * 
     * @throws Exception
     */
    @Test
    public void KnowledgeBase_28458_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Define Controls for global search
        VoodooControl searchResultsCtrl = sugar().navbar.search.getControl("searchResults");

        // QAUser log in and search for "QAUser" in global search window
        sugar().navbar.setGlobalSearch(testName);

        // Verify that the "QAUser new KB record" is appears in the found list
        searchResultsCtrl.assertContains(testName, true);

        // Click on the found "QAUser new KB" link to open the KB record view
        searchResultsCtrl.click();

        // Verify that the KB record view is appears
        sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName, true);

        // QAUser log out and login as custom user
        sugar().logout();
        myUser.login();

        // Custom user search for "QAUser new KB" record in global search window
        sugar().navbar.setGlobalSearch(testName);

        FieldSet noDataFoundFS = testData.get(testName).get(0);

        // Verify that the "QAUser new KB record" is appears in the found list
        searchResultsCtrl.assertContains(testName, false);
        searchResultsCtrl.assertContains(noDataFoundFS.get("noDataFound"), true);

        // Cancel the search
        sugar().navbar.search.getControl("cancelSearch").click();

        // Navigate to KB list view
        sugar().knowledgeBase.navToListView();

        // Verify that KB list view is empty
        sugar().knowledgeBase.listView.assertIsEmpty();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}