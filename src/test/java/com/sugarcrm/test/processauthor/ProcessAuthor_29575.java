package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29575 extends SugarTest {
	UserRecord chrisUser;
	FieldSet processInfo = new FieldSet();

	public void setup() throws Exception {
		processInfo = testData.get(testName).get(0);

		// Log-In as admin
		sugar().login();

		// Create a custom admin user
		chrisUser = (UserRecord)sugar().users.create();

		// Update user type to admin to access process definition dashlet
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563 - Need lib support for user profile edit page
		new VoodooControl("select", "id", "UserType").set(processInfo.get("adminUser"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Log-Out from Admin
		sugar().logout();
	}

	/**
	 * Verify desinger canvas is open after click confirm from design icon in Process definition dashlet
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29575_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log-In as Chris
		sugar().login(chrisUser);

		// Import the process definition and enable it
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create an account record to trigger the process
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();

		// Create a Dashboard and add Process Definition Dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", ".inline-drawer-header input").set(processInfo.get("dashletName"));
		new VoodooControl("a", "css", ".fld_title a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Save the dashboard
		sugar().home.dashboard.save();

		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("button", "css", ".btn-group.dashlet-group .btn:nth-child(2)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".actions.pull-right .btn-mini").click();
		sugar().alerts.getWarning().assertContains(processInfo.get("warningMessage"), true);
		sugar().alerts.getWarning().confirmAlert();

		// TODO: VOOD-1947 - Support PMSE Design Canvas/ toolbar buttons/ title
		new VoodooControl("div", "class", "pmse-toolbar-buttons").assertVisible(true);
		new VoodooControl("div", "id", "jcore_designer").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}