package com.sugarcrm.test.profiles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Profiles_28333 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify admin can remove or add teams via access tab in My Profile
	 * 
	 * @throws Exception
	 */
	@Test
	public void Profiles_28333_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		FieldSet globalTeamData = testData.get(testName).get(0);
		String qaUsername = sugar().users.getQAUser().get("userName");

		// Profile page
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563
		VoodooControl accessTab = new VoodooControl("a", "id", "tab3");

		// Access Tab
		accessTab.click();

		// Select and Add qauser team
		new VoodooControl("input", "css", "#UsersDetailView input[type='button']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "team_name_input").set(qaUsername);
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "tr.oddListRowS1 a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		accessTab.click();

		// TODO: VOOD-563
		// XPath used only for team name contains in table records
		// Verify qauser team added
		new VoodooControl("tr", "xpath", "//*[@id='user_detailview_tabs']/div/div[3]/table[2]/tbody/tr[contains(.,'" + qaUsername + "')]").assertVisible(true);

		// Remove Global team and cancel
		// XPath used to find global in table records
		VoodooControl globalTeamRemoveCtrl = new VoodooControl("a", "xpath", "//*[@id='user_detailview_tabs']/div/div[3]/table[2]/tbody/tr[contains(.,'" + globalTeamData.get("globalTeam") + "')]/td[3]/a[4]");
		VoodooControl globalTeam = new VoodooControl("a", "xpath", "//*[@id='user_detailview_tabs']/div/div[3]/table[2]/tbody/tr[contains(.,'" + globalTeamData.get("globalTeam") + "')]");
		globalTeamRemoveCtrl.click();
		VoodooUtils.dismissDialog();

		// Verify Global team remains as it is
		globalTeam.assertVisible(true);

		// Remove Global team and confirm
		globalTeamRemoveCtrl.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusFrame("bwc-frame");
		accessTab.click();

		// Verify Global team is removed
		globalTeam.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}