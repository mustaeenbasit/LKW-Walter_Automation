package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21466 extends SugarTest {
	
	public void setup() throws Exception {
		
		// Initialize test data
		FieldSet dataFS = testData.get(testName).get(0);
		
		// Create a KB record
		KBRecord chrisEastKBRecord = (KBRecord) sugar().knowledgeBase.api.create();
		
		// Create a User record - chris
		sugar().users.api.create();
		
		// Login as admin
		sugar().login();
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Update KB record user = chris, team = East
		FieldSet kbEditData = new FieldSet();
		kbEditData.put("relTeam", dataFS.get("teamEast"));
		kbEditData.put("relAssignedTo", sugar().users.getDefaultData().get("userName"));
		chrisEastKBRecord.edit(kbEditData);
		
		// Assign chris to East team
		// Navigate to Team ListView, update East, add user chris
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// Search the East team
		VoodooUtils.focusDefault();
		sugar().teams.listView.basicSearch("East");
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-518
		// Assign chris to East team
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", ".list.view tr:nth-child(4) td:nth-child(1) input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Assign qaUser to West team
		// Navigate to Team ListView, search West, add user qaUser
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// Search the West team
		VoodooUtils.focusDefault();
		sugar().teams.listView.basicSearch(dataFS.get("teamWest"));
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-518
		// Assign qaUser to West team
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", ".list.view tr:nth-child(5) td:nth-child(1) input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * KB security_team To ensure that non-admin user cannot view other teams' articles.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21466_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Logout as admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// Verify that no KB records are shown
		sugar().knowledgeBase.navToListView();
		Assert.assertTrue("KB list view items count is not 0", sugar().knowledgeBase.listView.countRows() == 0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}