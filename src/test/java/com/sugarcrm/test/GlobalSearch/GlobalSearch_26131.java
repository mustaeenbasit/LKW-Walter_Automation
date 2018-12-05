package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_26131 extends SugarTest {
	VoodooControl moduleCtrl, fieldCtrl, billingAddressStreet,boostValue,accountDescription,saveBtnCtrl,studioLinkCtrl;
	DataSource searchData = new DataSource();

	public void setup() throws Exception {
		searchData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Check that setting a field's full text searchable to medium boost is working
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_26131_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Studio controls
		// TODO: VOOD-542, VOOD-1504
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("a", "id", "fieldsBtn");
		billingAddressStreet = new VoodooControl("a", "id", "billing_address_street");
		boostValue = new VoodooControl("input","id", "fts_field_boost");
		accountDescription = new VoodooControl("a", "id", "description");
		saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		studioLinkCtrl = sugar().admin.adminTools.getControl("studio");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLinkCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Accounts -> Fields -> Set boost Value to medium of BillingAddressStreet field
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();		
		billingAddressStreet.click();
		VoodooUtils.waitForReady();
		boostValue.set(searchData.get(0).get("boostValue"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create account Records
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		VoodooControl accountName = sugar().accounts.createDrawer.getEditField("name");
		VoodooControl billingAddress = sugar().accounts.createDrawer.getEditField("billingAddressStreet");
		VoodooControl description = sugar().accounts.createDrawer.getEditField("description");
		for(int i = 0; i<=1; i++) {
			accountName.set(searchData.get(i).get("name"));
			sugar().accounts.createDrawer.showMore();
			billingAddress.set(searchData.get(i).get("billingAddressStreet"));
			description.set(searchData.get(i).get("description"));
			sugar().accounts.createDrawer.save();
			if(i==0) 
				sugar().accounts.listView.create();
		}

		// Enter the string in Search Navbar
		sugar().navbar.getControl("globalSearch").set(searchData.get(0).get("billingAddressStreet").substring(0,4)+ "\uE007");
		VoodooUtils.waitForReady();

		// Verify Account having medium boost value field is occurring above in the global search results page.
		sugar().globalSearch.getRow(1).assertContains(searchData.get(0).get("name"),true);
		sugar().globalSearch.getRow(2).assertContains(searchData.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}