package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19483 extends SugarTest{
	TargetListRecord myTargetListRecord;

	public void setup() throws Exception {
		myTargetListRecord = (TargetListRecord)sugar().targetlists.api.create();
		sugar.login();
	}

	/**
	 * Target List - Contacts management_Verify that "Create" and "Cancel" function in contacts sub-panel windows works correctly.
	 *
	 */
	@Test
	public void TargetLists_19483_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to target list record
		myTargetListRecord.navToRecord();

		StandardSubpanel contactSubPanel = (StandardSubpanel)sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubPanel.scrollIntoViewIfNeeded(false);

		// create the record via subpanel 
		contactSubPanel.create(sugar().contacts.getDefaultData());
		FieldSet fs = new FieldSet();
		fs.put("name", sugar().contacts.getDefaultData().get("fullName"));
		// The created contact shows in the "Contacts" sub-panel.
		// TODO: VOOD-1424
		contactSubPanel.verify(1, fs, true);

		//  Click "Create" button in the "Contacts" sub-panel.
		contactSubPanel.addRecord();
		VoodooUtils.waitForReady();

		// Click the "Cancel" button.
		sugar().contacts.createDrawer.cancel();
		contactSubPanel.countRows();
		Assert.assertTrue("More then 1 record exist", contactSubPanel.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
