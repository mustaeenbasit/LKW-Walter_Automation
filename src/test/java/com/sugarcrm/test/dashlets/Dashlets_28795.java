package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28795 extends SugarTest {
	FieldSet customFS;
	UserRecord chrisAdmin;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Enable the KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a custom Admin user
		chrisAdmin = (UserRecord)sugar().users.create();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-987
		new VoodooControl("select", "id", "UserType").set(customFS.get("adminUser"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Logout from default admin user
		sugar().logout();
	}

	/**
	 * Verify the 'My Activity Stream' dashlet contains correct data when added on KB templates listview dashboard
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28795_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Perform the actions via custom admin user
		try{
			sugar().login(chrisAdmin);

			// Navigate to KB template and create a KB template  
			sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewTemplates");
			sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
			sugar().knowledgeBase.menu.getControl("createTemplate").click();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
			VoodooUtils.focusFrame(0);
			sugar().knowledgeBase.createDrawer.getEditField("body").set("testing content");
			VoodooUtils.focusDefault();
			sugar().knowledgeBase.createDrawer.save();

			// Define Controls for Dashlets
			// TODO: VOOD-960
			VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
			VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
			VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".drawer.active .fld_save_button a");

			// Create new Dash-board
			sugar().knowledgeBase.dashboard.clickCreate();
			sugar().knowledgeBase.dashboard.getControl("title").set(customFS.get("dashboardName"));

			// Add new Dashlet
			sugar().knowledgeBase.dashboard.addRow();
			sugar().knowledgeBase.dashboard.addDashlet(1, 1);
			VoodooUtils.waitForReady();
			dashletSearchCtrl.set(customFS.get("selectActivityInDashlet"));
			VoodooUtils.waitForReady();
			dashletSelectCtrl.click();
			VoodooUtils.waitForReady();
			saveBtnCtrl.click(); // Save dashlet
			VoodooUtils.waitForReady();

			// Save Dash-board
			sugar().knowledgeBase.dashboard.save();
			VoodooUtils.waitForReady();

			// TODO: VOOD-1756 - Need Lib support for Templates in KnowledgeBase Module
			// Make some changes of the new created KB template
			new VoodooControl("a", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1) a").click();
			new VoodooControl("a", "css", ".fld_edit_button a").click();
			VoodooUtils.waitForReady();
			sugar().knowledgeBase.recordView.getEditField("name").set(testName+"_1");
			sugar().knowledgeBase.recordView.save();

			// Navigate to the KB Templates list view
			sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
			sugar().knowledgeBase.menu.getControl("viewTemplates").click();

			// Verify that 'My Activity Stream' dashlet contains all changes which have been made for template
			// TODO: VOOD-960
			new VoodooControl("li", "css", "ul.dashlet-cell div.dashlet-content div > ul > li:nth-child(1)").assertContains(customFS.get("updatedData"), true);
			new VoodooControl("li", "css", "ul.dashlet-cell div.dashlet-content div > ul > li:nth-child(2)").assertContains(customFS.get("createdData"), true);
		}
		finally{
			sugar().logout();
			sugar().login();
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}