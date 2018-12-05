package com.sugarcrm.test.opportunities;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Assert;
import org.junit.Test;

public class Opportunities_24285 extends SugarTest {
	AccountRecord acc1;
	OpportunityRecord opp1;
	ContactRecord cnt1;
	FieldSet fs = new FieldSet();
	StandardSubpanel cntSub;

	public void setup() throws Exception {
		sugar().login();
		acc1 = (AccountRecord) sugar().accounts.api.create();
		fs.put("relAccountName", acc1.getRecordIdentifier());
		opp1 = (OpportunityRecord) sugar().opportunities.api.create(fs);
		cnt1 = new ContactRecord(sugar().contacts.getDefaultData());
		cnt1.remove("leadSource");
		// TODO: VOOD-718
		opp1.edit(fs);
	}

	/**
	 * Test Case 24285: Full Form Create Contact_Verify that contact related to
	 * an opportunity can be created in full form mode using "Create" function
	 * in "Contacts" sub-panel.
	 * 
	 * Test Case 24286: Full Form Create Contact_Verify that contact for
	 * opportunity is not created in full form mode in "Contacts" sub-panel when
	 * using "Cancel" function.
	 */
	@Test
	public void Opportunities_24285_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		cntSub = (StandardSubpanel) sugar().opportunities.recordView.subpanels.get("Contacts");
		opp1.navToRecord();
		cntSub.addRecord();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.setFields(cnt1);
		sugar().contacts.createDrawer.cancel();
		Assert.assertTrue("The contact has been created in spite of cancellation.", cntSub.isEmpty());

		cntSub.addRecord();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.setFields(cnt1);
		sugar().contacts.createDrawer.save();
		sugar().alerts.getAlert().closeAlert();
		cnt1.verify();
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}