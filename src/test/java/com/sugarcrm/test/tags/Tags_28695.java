package com.sugarcrm.test.tags;

import org.junit.Assert;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tags_28695 extends SugarTest {
	AccountRecord myAccountRecord;

	public void setup() throws Exception {
		myAccountRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Tags displays properly in all List Views
	 * @throws Exception
	 */
	@Test
	public void Tags_28695_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource tagsData = testData.get(testName);

		// Adding three tag in created account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		for (int i = 0; i < tagsData.size(); i++) {
			sugar().accounts.recordView.getEditField("tags").set(tagsData.get(i).get("tags"));
		}
		sugar().accounts.recordView.save();

		// Navigate to Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// Move to Accounts > Layouts > PopupView > ListView > Popup ListView
		// TODO VOOD-1508: Support Studio Module PopUp Layouts View
		VoodooControl tagsOption = new VoodooControl("li", "css", "[data-name='tag']");
		VoodooControl BillingCityOption = new VoodooControl("li", "css", "[data-name='billing_address_city']");
		VoodooControl studioAccountCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl studioLayoutCtrl = new VoodooControl("a", "css", "td#layoutsBtn a");
		VoodooControl studioSaveBtnCtrl = new VoodooControl("input", "id", "savebtn");

		studioAccountCtrl.click();
		VoodooUtils.waitForReady();
		studioLayoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons td:nth-child(3) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#PopupListViewBtn a").click();
		VoodooUtils.waitForReady();

		// Dragging and dropping 'tags' field from Hidden column to Default column
		tagsOption.scrollIntoViewIfNeeded(false);
		tagsOption.dragNDrop(BillingCityOption);
		studioSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Move to Accounts > Layouts > ListView 
		// TODO VOOD-1507: Support Studio Module ListView Layouts View
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		studioAccountCtrl.click();
		VoodooUtils.waitForReady();
		studioLayoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#viewBtnlistview").click();
		VoodooUtils.waitForReady();
		tagsOption.scrollIntoViewIfNeeded(false);
		tagsOption.dragNDrop(BillingCityOption);
		studioSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Move to studio -> Quotes -> Relationships
		// TODO VOOD-1505: Support Studio Module Relationship View
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#studiolink_Quotes").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[value='Add Relationship']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#relationship_type_field").set(tagsData.get(0).get("relationshipType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[value='Save & Deploy']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Quotes module
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");

		// Clicking on account field in quotes
		// TODO: VOOD-1851
		new VoodooControl("button", "css", "#btn_quotes_accounts_1_name").click();
		VoodooUtils.focusWindow(1);

		// Verifying Only one account is displaying in bwc pop window
		// TODO: VOOD-776
		new VoodooControl("tr", "css", "tr.evenListRowS1").assertVisible(false);

		// Verifying Correct account name is appearing 
		new VoodooControl("tr", "css", "tr.oddListRowS1 a").assertEquals(myAccountRecord.getRecordIdentifier(), true);

		// Verifying tag field is not available in bwc pop window of account
		new VoodooControl("th", "css", ".list.view tr:nth-child(2) th").assertContains(sugar().tags.moduleNamePlural, false);
		VoodooUtils.closeWindow();
		VoodooUtils.focusWindow(0);
		sugar().quotes.editView.cancel();

		// Navigate to account listview
		sugar().accounts.navToListView();

		// Verifying only one account is displaying
		Assert.assertEquals(1, sugar().accounts.listView.countRows());

		// Verifying correct account name is appearing
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(myAccountRecord.getRecordIdentifier(), true);

		// Verifying all tags are appearing properly in associated account
		sugar().accounts.listView.getDetailField(1, "tags").assertEquals(tagsData.get(0).get("tags") + ", " + tagsData.get(1).get("tags") + ", " + tagsData.get(2).get("tags"), true);

		// Navigate to contact create drawer
		sugar().navbar.selectMenuItem(sugar().contacts, "createContact");
		VoodooSelect searchForMore = (VoodooSelect) sugar().contacts.createDrawer.getEditField("relAccountName");
		searchForMore.clickSearchForMore();

		// Verifying only one account is appearing in search and select drawer
		Assert.assertTrue(sugar().accounts.searchSelect.countRows() == 1);

		// Verifying correct account name is appearing in SSV
		// TODO: VOOD-1487
		new VoodooControl("div", "css", ".layout_Accounts .list.fld_name div").assertEquals(myAccountRecord.getRecordIdentifier(), true);

		// Verifying all tags are appearing properly which associated to account in SSV
		new VoodooControl("div", "css", ".list.fld_tag").assertEquals(tagsData.get(0).get("tags") + ", " + tagsData.get(1).get("tags") + ", " + tagsData.get(2).get("tags"), true);

		// Closing the SSV
		sugar().accounts.searchSelect.cancel();
		
		// Closing the Contact create drawer
		sugar().contacts.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}