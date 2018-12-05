package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Accounts_22932 extends SugarTest {
	AccountRecord myAccount;
	ArrayList<Record> myContacts;
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		myAccount = (AccountRecord) sugar().accounts.api.create();
		myContacts = sugar().contacts.api.create(ds);
		sugar().login();
		myAccount.navToRecord();    
	}

	/**
	 * Verify that corresponding contact records are displayed  using search function on the select contacts pop-up box.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22932_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).clickLinkExisting();
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl searchInputCtrl = new VoodooControl("input", "css" ,"div.filter-view.search.layout_Contacts input.search-name");
		VoodooControl nameCtrl = new VoodooControl("span", "css" ,"span[data-voodoo-name='full_name']");
		VoodooControl secondNameRecordCtrl = new VoodooControl("tr", "css" ,"div.layout_Contacts div.flex-list-view-content tbody tr:nth-child(2)");

		// check for record with search
		for(Record con : myContacts) {  
			ContactRecord myCon = (ContactRecord) con;
			// TODO VOOD-726
			searchInputCtrl.set(myCon.getRecordIdentifier());
			sugar().alerts.waitForLoadingExpiration();
			nameCtrl.assertContains(myCon.getRecordIdentifier(), true);   
			secondNameRecordCtrl.assertExists(false);
		}
		// TODO VOOD-726
		new VoodooControl("a", "css" ,"span.fld_close a.btn-invisible.btn-link").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}