package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_18246 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that leads "Search and Select" drawer doesn't have any click-able link  
	 * @throws Exception
	 */
	@Test
	public void Accounts_18246_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Updating the email address and account name for leads record
		DataSource searchValues = testData.get(testName);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.getEditField("emailAddress").set(searchValues.get(4).get("textValue"));
		sugar().leads.recordView.getEditField("accountName").set(sugar().accounts.defaultData.get("name"));
		sugar().leads.recordView.save();

		// Navigate to accounts record and click Link existing record for leads subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel leadsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.clickLinkExisting();

		// Verify that value in the search and select drawer for leads is not click-able link (values on list view available on 'div' tag rather than 'a' tag) 
		// TODO: VOOD-1487 : Need lib support for verification of sugar-fields on SSV
		int count = new VoodooControl("td", "css", ".search-and-select .single td").count();
		for (int i = 2; i < count -2; i++) {
			new VoodooControl("div", "css", ".search-and-select .single td:nth-of-type(" + i + ") div").assertEquals(searchValues.get(i-2).get("textValue"), true);			
		}

		// Click Preview icon on search and select window
		sugar().leads.searchSelect.preview(1);
		VoodooUtils.waitForReady();

		// Asserting that email address is link on preview pane of leads
		sugar().previewPane.getPreviewPaneField("emailAddress").click();
		sugar().accounts.recordView.composeEmail.getControl("cancelButton").assertVisible(true);
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.composeEmail.cancel();
		sugar().leads.searchSelect.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}