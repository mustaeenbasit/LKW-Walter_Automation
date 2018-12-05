package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class KnowledgeBase_28782 extends SugarTest {
	FieldSet role = new FieldSet();	
	UserRecord chrisUser;

	public void setup() throws Exception {
		role = testData.get(testName).get(0);
		FieldSet kbStatus = new FieldSet();
		kbStatus.put("status", role.get("status"));
		sugar().knowledgeBase.api.create(kbStatus);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create New user Chris
		// TODO:VOOD-1200
		chrisUser = (UserRecord) sugar().users.create();
		AdminModule.createRole(role);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Edit Permission to "None" for KB
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_edit div select").set(role.get("permission"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign Role to User Chris
		AdminModule.assignUserToRole(chrisUser);
		sugar().logout();
	}

	/**
	 * Verify that it has correct behavior when KB Edit=None in role  
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28782_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().login(chrisUser);
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// Verifying Edit button is not visible for record in listView
		sugar().knowledgeBase.listView.getControl("edit01").assertVisible(false);

		// Adding Dashlet KBCategory & Useful Published Knowledge Base Articles in the listView
		// TODO: VOOD-960,VOOD-670
		VoodooControl searchCtrl = new VoodooControl("input", "css", ".span4.search");
		VoodooControl dashletCtrl = new VoodooControl("a", "css", "tr.single .fld_title a");
		VoodooControl saveDashlet = new VoodooControl("a", "css", ".active .fld_save_button a");
		sugar().knowledgeBase.dashboard.clickCreate();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.dashboard.getControl("title").set(testName);
		for(int i = 1; i <= 2; i++) {
			sugar().knowledgeBase.dashboard.addRow();
			sugar().knowledgeBase.dashboard.addDashlet(i, 1);
			if (i == 1) 
				searchCtrl.set(role.get("listViewDashlet1"));
			else 
				searchCtrl.set(role.get("listViewDashlet2"));

			VoodooUtils.waitForReady();
			dashletCtrl.click();
			VoodooUtils.waitForReady();
			saveDashlet.click();
			VoodooUtils.waitForReady(30000);
		}
		sugar().knowledgeBase.dashboard.save();

		// Verify dashlets exists on the listView of KB
		// TODO: VOOD-960,VOOD-670
		VoodooControl dashlet = new VoodooControl("h4", "css", ".dashlet-row .row-fluid:nth-child(1) .dashlet-title");
		dashlet.assertEquals(role.get("listViewDashlet1"),true);
		new VoodooControl("h4", "css", ".dashlet-row .row-fluid:nth-child(2) .dashlet-title").assertEquals(role.get("listViewDashlet2"),true);

		// Navigate Knowledge record detail view 
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(testName);
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1,1);

		// Add the new dashlet on record View of the KB Record
		searchCtrl.set(role.get("recordViewDashlet1"));
		VoodooUtils.waitForReady();
		dashletCtrl.click();
		saveDashlet.click();
		VoodooUtils.waitForReady(30000);
		sugar().knowledgeBase.dashboard.save();

		// Verify dashlet is shown on the record View of KB record
		dashlet.assertEquals(role.get("recordViewDashlet1"),true);

		// Verify Edit button is unavailable and Inline editing also can't be performed on category & approver fields
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		sugar().knowledgeBase.recordView.getControl("editButton").assertVisible(false);
		sugar().knowledgeBase.recordView.showMore();

		// Verify Category,ApprovedBy & KB Fields cannot be edited
		// TODO:VOOD-1445
		new VoodooControl("span", "css", ".fld_kbsapprover_name").assertAttribute("class", "edit", false);
		new VoodooControl("span", "css", ".fld_category_name").assertAttribute("class", "edit", false);
		new VoodooControl("span", "css", ".headerpane .fld_name").assertAttribute("class", "edit", false);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}