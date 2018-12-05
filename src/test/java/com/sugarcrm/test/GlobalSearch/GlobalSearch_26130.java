package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_26130 extends SugarTest {
	VoodooControl moduleCtrl, fieldCtrl, accountName,boostValue,accountDescription,saveBtnCtrl,studioLinkCtrl;
	DataSource searchData = new DataSource();

	public void setup() throws Exception {
		searchData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Check that setting a field's full text searchable to high boost is working
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_26130_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Studio controls
		// TODO: VOOD-542, VOOD-1504
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("a", "id", "fieldsBtn");
		accountName = new VoodooControl("a", "id", "name");
		boostValue = new VoodooControl("input","id", "fts_field_boost");
		accountDescription = new VoodooControl("a", "id", "description");
		saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		studioLinkCtrl = sugar().admin.adminTools.getControl("studio");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLinkCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Accounts -> Fields -> Set High boost Value of Description field
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		accountDescription.click();
		VoodooUtils.waitForReady();
		boostValue.set(searchData.get(0).get("boostValue"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Accounts -> Fields -> Set boost Value to medium of Account Name field
		accountName.click();
		VoodooUtils.waitForReady();
		boostValue.set(searchData.get(1).get("boostValue"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create account Record.
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		VoodooControl accountName = sugar().accounts.createDrawer.getEditField("name");
		VoodooControl description = sugar().accounts.createDrawer.getEditField("description");
		for(int i = 0; i<=1; i++) {
			accountName.set(searchData.get(i).get("name"));
			sugar().accounts.createDrawer.showMore();
			description.set(searchData.get(i).get("description"));
			sugar().accounts.createDrawer.save();
			if(i==0) 
				sugar().accounts.listView.create();
		}

		// Enter the string in Search Navbar
		sugar().navbar.getControl("globalSearch").set(searchData.get(0).get("name").substring(0,4)+ "\uE007");
		VoodooUtils.waitForReady();

		// Verify Account having high boost value field is occurring above in the global search results page.
		sugar().globalSearch.getRow(1).assertContains(searchData.get(1).get("name"),true);
		sugar().globalSearch.getRow(2).assertContains(searchData.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}