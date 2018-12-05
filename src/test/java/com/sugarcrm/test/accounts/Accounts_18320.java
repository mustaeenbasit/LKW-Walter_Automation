package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.PreviewPane;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class Accounts_18320 extends SugarTest {
	protected AccountRecord myAccount;
	protected ArrayList<Record> myContacts;
	StandardSubpanel contactSubpanel;
	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		DataSource contactsList = new DataSource();
		contactsList = testData.get(testName);
		myContacts = (ArrayList<Record>)sugar().contacts.api.create(contactsList);
		sugar().login();

		// related contacts to account
		for(Record createdRecord : myContacts) {	
			createdRecord.navToRecord();
			sugar().contacts.recordView.edit();
			sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
			sugar().alerts.getWarning().confirmAlert();
			sugar().contacts.recordView.save();
			sugar().alerts.waitForLoadingExpiration();
		}
	}

	/**
	 * Subpanel preview pane pagination
	 * @throws Exception
	 */
	@Test
	public void Accounts_18320_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.clickPreview(1);
		sugar().alerts.waitForLoadingExpiration();
		PreviewPane rhsPane = PreviewPane.getInstance();	

		// TODO: VOOD-803
		VoodooControl nextIconEnable = new VoodooControl ("i", "css", ".fa-chevron-right");
		VoodooControl prevIconEnable = new VoodooControl ("i", "css", ".fa-chevron-left");
		VoodooControl nextIconDisable = new VoodooControl ("i", "css", ".disabled .fa-chevron-right");
		VoodooControl prevIconDisable =	new VoodooControl ("i", "css", ".disabled .fa-chevron-left");	
		prevIconDisable.assertExists(true);		

		// navigation in preview pane
		int j = 1;
		for(int i = myContacts.size()-1;i>0;i--){
			rhsPane.assertElementContains(myContacts.get(i).get("firstName"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("lastName"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("department"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("phoneMobile"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("salutation"), true);	
			VoodooControl currRow = new VoodooControl ("tr", "xpath", "//div[@class='filtered tabbable tabs-left layout_Contacts']//tbody[1]//tr[position()=" + j + "]");
			currRow.assertAttribute("class", "single current highlighted");	
			nextIconEnable.click();
			j++;
		}
		nextIconDisable.assertExists(true);
		for(int i = 1;i<=myContacts.size()-1;i++){
			prevIconEnable.click();
			rhsPane.assertElementContains(myContacts.get(i).get("firstName"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("lastName"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("department"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("phoneMobile"), true);
			rhsPane.assertElementContains(myContacts.get(i).get("salutation"), true);				
		}
		prevIconDisable.assertExists(true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}