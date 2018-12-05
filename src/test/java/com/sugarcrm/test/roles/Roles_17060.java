package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Roles_17060 extends SugarTest {
	FieldSet roleRecord;
	ContactRecord myCon;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		DataSource callsDS = testData.get(testName+"_calls");
		sugar().calls.api.create(callsDS);
		sugar().accounts.api.create();
		myCon = (ContactRecord) sugar().contacts.api.create();
		sugar().login();

		// Create role => Calls -> Related to (read/owner write)
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().calls.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "parent_namelink").click();
		new VoodooControl("select", "id", "flc_guidparent_name").set(roleRecord.get("roleName"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Call record assigned to qauser and set parent type and its name and save record
		sugar().calls.navToListView();
		sugar().calls.listView.editRecord(1);

		// account parent type &  record
		sugar().calls.listView.getEditField(1, "relatedToParentType").set(sugar().accounts.moduleNameSingular);
		sugar().calls.listView.getEditField(1, "relatedToParentName").set(sugar().accounts.getDefaultData().get("name"));
		VoodooControl assignedTo = sugar().calls.listView.getEditField(1, "assignedTo");
		assignedTo.scrollIntoViewIfNeeded(false);
		assignedTo.set(sugar().users.getQAUser().get("userName"));
		sugar().calls.listView.saveRecord(1);

		sugar().logout();
	}

	/**
	 * User should be able to view and edit related-to field if he has access to
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_17060_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		VoodooControl parentTypeDetail = sugar().calls.recordView.getDetailField("relatedToParentType");
		VoodooControl parentNameDetail = sugar().calls.recordView.getDetailField("relatedToParentName");

		// Verify parent name and type shown on detail view
		parentTypeDetail.assertEquals(sugar().accounts.moduleNameSingular, true);
		parentNameDetail.assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		// Edit call record view
		sugar().calls.recordView.edit();
		VoodooSelect parentTypeEdit = (VoodooSelect)sugar().calls.recordView.getEditField("relatedToParentType");
		VoodooSelect parentNameEdit = (VoodooSelect)sugar().calls.recordView.getEditField("relatedToParentName");
		parentTypeEdit.set(sugar().contacts.moduleNameSingular);
		parentNameEdit.set(myCon.getRecordIdentifier());
		sugar().calls.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify new parent name and type shown on detail view
		parentTypeDetail.assertEquals(sugar().contacts.moduleNameSingular, true);
		parentNameDetail.assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		// Verify parent type and parent name is not shown up and editable for another user (i.e Administrator)
		sugar().calls.recordView.gotoNextRecord();
		parentTypeDetail.assertEquals("Related to", true);
		parentNameDetail.assertVisible(false);
		sugar().calls.recordView.edit();
		parentTypeEdit.assertVisible(false);
		parentNameEdit.assertVisible(false);
		sugar().calls.recordView.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}