package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Leads_22584 extends SugarTest {

	public void setup() throws Exception {
		sugar().targetlists.api.create();
		sugar().login();
	}

	/**
	 * Verify the Actions dropdown list in leads listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22584_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify action dropdown is disabled when no records on listview
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		VoodooControl actionDropdown = sugar().leads.listView.getControl("actionDropdown");
		Assert.assertTrue("Action dropdown is enabled!", actionDropdown.isDisabled());

		// Create Lead record
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		sugar().leads.listView.toggleSelectAll();
		// Verify action dropdown is enabled
		Assert.assertTrue("Action dropdown is disabled!", !actionDropdown.isDisabled());

		// Verify all menu actions listed in dropdown (i.e Email, Merge, Add to Target List, Mass Update, Delete, Export)
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().leads.listView.getControl("deleteButton").assertVisible(true);
		sugar().leads.listView.getControl("exportButton").assertVisible(true);

		// TODO: VOOD-657
		// Verify merge option
		new VoodooControl("span", "css", ".fld_merge_button").assertVisible(true);

		// Verify mail option and compose mail page after triggering
		// TODO: VOOD-843
		new VoodooControl("span", "css", ".fld_mass_email_button").click();
		new VoodooControl("span", "css", ".btn-toolbar.pull-right span.fld_cancel_button:not(.hide)").click();
		VoodooUtils.waitForReady();

		// Verify mass update action and also verify record after mass update
		FieldSet fs = testData.get(testName).get(0);
		String qaUserName = sugar().users.getQAUser().get("userName");
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.massUpdate();
		sugar().leads.massUpdate.getControl(String.format("massUpdateField%02d", 2)).set(fs.get("assignedTo"));
		sugar().leads.massUpdate.getControl(String.format("massUpdateValue%02d", 2)).set(qaUserName);
		sugar().leads.massUpdate.update();
		sugar().leads.listView.verifyField(1, "relAssignedTo", qaUserName);

		// Verify target list and verify record after add to list
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-528, VOOD-657
		new VoodooControl("span", "css", ".fld_addtolist_button").click();
		String targetListName = sugar().targetlists.getDefaultData().get("targetlistName");
		new VoodooSelect("span", "css", ".fld_prospect_lists_name div a").set(targetListName);
		new VoodooControl("a", "css", ".massaddtolist.fld_update_button a:not(.hide)").click();
		VoodooUtils.waitForReady();
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.verifyField(1, "targetlistName", targetListName);

		// Click on delete menu option and verifying no record appears after deletion of record
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().leads.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}