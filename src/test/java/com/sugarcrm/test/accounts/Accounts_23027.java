package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23027 extends SugarTest {
	FieldSet customData = new FieldSet();
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		customData=sugar().contacts.defaultData;

		// Create an Accounts record related to a Contact record
		sugar().accounts.api.create();
		ContactRecord conRecord = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Link the Accounts record with Contacts record in subpanel
		contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(conRecord);
	}
	/**
	 * Account Detail - Contacts sub-panel_Verify that editing contact record related to this account can be canceled.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// On Contacts subpanel click on Edit action button and supply new value to Office Phone and then click on Cancel 
		contactsSubpanel.editRecord(1);
		contactsSubpanel.getEditField(1, "phoneWork").set(customData.get("phoneMobile"));
		contactsSubpanel.cancelAction(1);

		// TODO: VOOD-1424
		// Verify the record persist with no change
		contactsSubpanel.getDetailField(1, "fullName").assertEquals(customData.get("fullName"), true);

		// TODO: VOOD-609, VOOD-1380
		new VoodooControl("span", "css", ".list.fld_primary_address_city").assertEquals(customData.get("primaryAddressCity"), true);
		new VoodooControl("span", "css", ".list.fld_primary_address_state").assertEquals(customData.get("primaryAddressState"), true);
		contactsSubpanel.getDetailField(1, "emailAddress").assertEquals("", true);
		contactsSubpanel.getDetailField(1, "phoneWork").assertEquals(customData.get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}