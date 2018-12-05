package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_25059 extends SugarTest {
	FieldSet roleRecordData, assignedToFieldFS; 

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For opportunity module, set likely field as owner read/owner write.
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().opportunities.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "amountlink").click();
		new VoodooControl("select", "id", "flc_guidamount").set(roleRecordData.get("access"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Create an opportunity, Assign this opportunity to other users admin2.Make sure likely has value
		assignedToFieldFS = new FieldSet();
		assignedToFieldFS.put("name", roleRecordData.get("oppName"));
		assignedToFieldFS.put("relAssignedTo", roleRecordData.get("user2"));
		sugar().opportunities.create(assignedToFieldFS);
		assignedToFieldFS.clear();

		// Create another opportunity, Assign this opportunity to QAUser.Make sure likely has value.
		assignedToFieldFS.put("relAssignedTo", roleRecordData.get("userName"));
		sugar().opportunities.create(assignedToFieldFS);
		assignedToFieldFS.clear();

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 *  In Opportunity, Verify No access label is displayed for the field if owner permission is set and this opportunity does not belong to the user
	 * @throws Exception
	 */
	@Test
	public void Roles_25059_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Go to Opportunities module
		sugar().opportunities.navToListView();

		// TODO: VOOD-1349, VOOD-1445
		VoodooControl listViewDetailLikelyFieldCtrl = new VoodooControl("span", "css", ".fld_amount.noaccess span");
		VoodooControl listViewEditLikelyFieldCtrl = sugar().opportunities.listView.getEditField(1, "likelyCase");//new VoodooControl("span", "css", ".fld_amount.edit input");
		VoodooControl detailViewLikelyFieldCtrl = new VoodooControl("span", "css", ".noaccess.fld_amount span");

		// Verify In list view, first Opportunity(which is assigned to users admin2) will display No access label for likely field and other will display likely value.
		sugar().opportunities.listView.getDetailField(1, "likelyCase").assertEquals(roleRecordData.get("likelyValue"), true);
		listViewDetailLikelyFieldCtrl.assertEquals(roleRecordData.get("noAccess"), true);

		// In list view, click edit for both Opportunities
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.editRecord(2);

		// Verify In list edit view, Opportunity(which is assigned to users admin2) will display No access label for likely field and other will display likely field as Edit-able
		listViewEditLikelyFieldCtrl.assertExists(true); // edit attribute contains by the class shows the field is edit-able
		listViewEditLikelyFieldCtrl.assertAttribute("value", roleRecordData.get("likelyValue"), true);
		listViewDetailLikelyFieldCtrl.assertEquals(roleRecordData.get("noAccess"), true);

		// Click Cancel
		sugar().opportunities.listView.cancelRecord(1);
		sugar().opportunities.listView.cancelRecord(2);

		// Click preview
		sugar().opportunities.listView.previewRecord(1);
		VoodooControl previewLikelyFieldCtrl = sugar().previewPane.getPreviewPaneField("likelyCase");//new VoodooControl("div", "css", ".preview-data .fld_amount div");

		// Verify In preview pane, Opportunity(which is assigned to users admin2) will display Nothing for likely field and other will display likely value
		previewLikelyFieldCtrl.assertEquals(roleRecordData.get("likelyValue"), true);
		sugar().previewPane.gotoNextRecord(); // Move to next record
		VoodooUtils.waitForReady();
		previewLikelyFieldCtrl.assertExists(false); // The div is absent in which displaying likely field value actually

		// Select Opportunity(which is assigned to users admin2) to record view.
		sugar().opportunities.listView.clickRecord(2);

		// Verify that in record view, Opportunity record is displaying No access label for likely field
		detailViewLikelyFieldCtrl.assertEquals(roleRecordData.get("noAccess"), true);

		// Open other Opportunity record((which is assigned to users QAUser) to record view.
		sugar().opportunities.recordView.gotoPreviousRecord();

		// Verify that in record view, Opportunity record is displaying likely value
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals(roleRecordData.get("likelyValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}