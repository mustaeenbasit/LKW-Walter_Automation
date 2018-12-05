package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21459 extends SugarTest {
	String qaUser = new String();
	
	public void setup() throws Exception {
		// Create a KB record with name 'Aperture Laboratories FAQ' 
		sugar().knowledgeBase.api.create();
		
		// Create a KB record with name as the TestName
		FieldSet kbData = new FieldSet();
		kbData.put("name", testName);
		KBRecord qaTeamKB = (KBRecord) sugar().knowledgeBase.api.create(kbData);
		kbData.clear();
		
		// Login as admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB module and put the KB record with name as testname in 'qauser' team
		qaUser = sugar().users.getQAUser().get("userName");
		kbData.put("relTeam", qaUser);
		qaTeamKB.edit(kbData);
		
		// Adding 'Teams' field to Knowledge Base > Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on studio link  
		// TODO: VOOD-1509 Support Studio Module Search Layout View
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Click on KB in studio panel 
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();

		// Click on layout
		new VoodooControl("a", "css", "#layoutsBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Click on search
		new VoodooControl("a", "css", "#searchBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Click on search
		new VoodooControl("a", "css", "#FilterSearchBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Dragging and dropping 'Teams' field from hidden to default 
		new VoodooControl("li", "css", "[data-name='team_name']").dragNDrop(new VoodooControl("li", "css", "[data-name='status']"));
		VoodooUtils.waitForReady();

		// Saving settings
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * To ensure that articles can be searched by 'Team' field
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21459_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		DataSource customData = testData.get(testName);
		
		// Navigate to KB Module
		sugar().knowledgeBase.navToListView();
		
		// Verify that 2 KB records are displayed before applying filter
		Assert.assertTrue("Row count is not equal to 2, when it should.", sugar().knowledgeBase.listView.countRows() == 2);
		
		// Create a filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Setting up field in filter in list view of KB module
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customData.get(0).get("rowName"));
		
		// Setting the operator
		new VoodooSelect("a", "css", ".detail.fld_filter_row_operator .select2-choice").set(customData.get(0).get("rowOperator"));
		
		// Setting team name as 'Global' in filter
		VoodooSelect teamNameInputBox = new VoodooSelect("a", "css", ".detail.fld_team_name .select2-container a");
		teamNameInputBox.set(customData.get(0).get("teamName"));
		VoodooUtils.waitForReady();
		
		// Verify that only one KB record is returned
		Assert.assertTrue("Row count is greater than 1, when it should not", sugar().knowledgeBase.listView.countRows() == 1);
		
		// Verify that the KB record displayed is the one with Global as team
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(sugar().knowledgeBase.defaultData.get("name"), true);
		
		// Setting team name as 'qauser' in filter
		teamNameInputBox.set(qaUser);
		VoodooUtils.waitForReady();

		// Verify that only one KB record is returned
		Assert.assertTrue("Row count is greater than 1, when it should not", sugar().knowledgeBase.listView.countRows() == 1);

		// Verify that the KB record displayed is the one with qauser as team
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}