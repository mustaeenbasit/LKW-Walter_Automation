package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28620 extends SugarTest {
	VoodooControl studioLinkAccounts, layoutsButton, listviewButton, popUpListViewButton, saveAndDeployBtn;
	
	public void setup() throws Exception {
		sugar.login();
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// Move to Accounts > Layouts > PopupView > ListView > Popup ListView
		// TODO VOOD-1508: Support Studio Module PopUp Layouts View
		studioLinkAccounts = new VoodooControl("a", "id", "studiolink_Accounts");
		layoutsButton = new VoodooControl("a", "css", "td#layoutsBtn a");
		listviewButton = new VoodooControl("a", "css", "#Buttons td:nth-child(3) a");
		popUpListViewButton = new VoodooControl("a", "css", "#PopupListViewBtn a");
		VoodooControl tagsOption = new VoodooControl("li", "css", "[data-name='tag']");
		VoodooControl BillingCityOption = new VoodooControl("li", "css", "[data-name='billing_address_city']");
		saveAndDeployBtn = new VoodooControl("input", "id", "savebtn");       
		
		studioLinkAccounts.click();
		VoodooUtils.waitForReady();
		layoutsButton.click();
		VoodooUtils.waitForReady();
		listviewButton.click();
		VoodooUtils.waitForReady();
		popUpListViewButton.click();
		VoodooUtils.waitForReady();
		
		// Dragging and dropping "Tags" field from Hidden column to Default column
		tagsOption.scrollIntoViewIfNeeded(false);
		tagsOption.dragNDrop(BillingCityOption);
		saveAndDeployBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify user is able to view tags associated to a record on search & select list views
	 * @throws Exception
	 */
	@Test
	public void Tags_28620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.selectMenuItem(sugar.accounts, "createAccount");
		sugar.accounts.createDrawer.getEditField("name").set(sugar.accounts.getDefaultData().get("name"));
		sugar.accounts.createDrawer.getEditField("tags").set(sugar.tags.getDefaultData().get("name"));
		sugar.accounts.createDrawer.save();
		
		sugar.navbar.selectMenuItem(sugar.contacts, "createContact");
		sugar.contacts.createDrawer.getEditField("relAccountName").click();
		
		// Selecting the 'Search and Select' option
		// TODO: VOOD-1418
		new VoodooControl("li", "css", ".select2-result").click();
		VoodooUtils.waitForReady();
		
		// Verifying that Tags column is displayed on the SSV 
		// TODO: VOOD-1487 : Need lib support for verification of sugar-fields on SSV
		VoodooControl tagColumn = new VoodooControl("th", "css", ".search-and-select [data-fieldname='tag']");
		tagColumn.assertVisible(true);
		tagColumn.assertEquals("Tags", true);
		
		// Verifying that the Account column displays the account saved above
		new VoodooControl("div", "css", ".single [data-type='name'] div")
			.assertEquals(sugar.accounts.defaultData.get("name"), true);
		
		// Verifying that the Tag column displays the tag saved above
		new VoodooControl("div", "css", ".single [data-type='tag'] div")
			.assertEquals(sugar.tags.defaultData.get("name"), true);
		
		sugar.accounts.searchSelect.cancel();
		sugar.contacts.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}