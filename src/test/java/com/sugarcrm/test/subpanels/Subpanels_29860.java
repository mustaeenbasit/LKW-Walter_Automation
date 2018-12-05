package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_29860 extends SugarTest {
	ContactRecord contactRec;
	String defaultAccName = "";

	public void setup() throws Exception {
		sugar().bugs.api.create();
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		contactRec = (ContactRecord) sugar().contacts.api.create();
		sugar().login();

		// Enabling Bugs Module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Assigning a Account to the contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		defaultAccName = sugar().accounts.getDefaultData().get("name");
		sugar().contacts.recordView.getEditField("relAccountName").set(defaultAccName);
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that Account name should be updated while inline edit in contact subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29860_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Linking the contact record to contact subpanel of bugs record
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().bugs.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(contactRec);

		// Updating account name on contact record in bugs subpanel
		VoodooControl accName = contactSubpanel.getDetailField(1, "relAccountName");
		accName.assertEquals(defaultAccName, true);
		contactSubpanel.editRecord(1);
		contactSubpanel.getEditField(1, "relAccountName").set(testName);
		sugar().alerts.getWarning().confirmAlert();
		contactSubpanel.saveAction(1);

		// Asserting the updated account name on contacts subpanel.
		accName.assertEquals(testName, true);

		// Asserting the that account name is also updated on contacts record view 
		contactSubpanel.clickRecord(1);
		sugar().contacts.recordView.getDetailField("relAccountName").assertEquals(testName, true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}