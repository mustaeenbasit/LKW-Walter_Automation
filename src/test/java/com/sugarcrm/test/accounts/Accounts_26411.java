package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_26411 extends SugarTest {
	FieldSet massUpdateRecord;
	FieldSet accountRecord;
	// Common VoodooControl and VoodooSelect references
	VoodooControl studioLink;
	VoodooControl studioLinkAccounts;
	VoodooControl fieldsButton;
	VoodooControl accountLabel;
	VoodooControl publishButton;
	VoodooControl dragToRow;
	VoodooControl dragToFiller;

	public void setup() throws Exception {
		// TODO VOOD-517 Create Studio Module (BWC)
		studioLink = new VoodooControl("a", "id", "studio");
		studioLinkAccounts = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldsButton = new VoodooControl("a", "css", "td#fieldsBtn a");
		accountLabel = new VoodooControl("input", "id",
				"input_LBL_ACCOUNT_NAME");
		publishButton = new VoodooControl("button", "id", "publishBtn");
		dragToRow = new VoodooControl("div", "css",
				".le_panel:nth-of-type(1) .le_row:nth-of-type(2)");
		dragToFiller = new VoodooControl("span", "css",
				".le_panel:nth-of-type(1) .le_row:nth-of-type(3) span");
		accountRecord = testData.get("Accounts_26411").get(0);
		sugar().login();
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.api.create();

		// Create a Custom filed and move it to the accounts record layout
		// TODO VOOD-517 Create Studio Module (BWC)

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		studioLinkAccounts.click();
		fieldsButton.click();
		new VoodooControl("input", "css", "input[name='addfieldbtn']").click();
		new VoodooControl("select", "id", "type").set("DropDown");
		new VoodooControl("select", "id", "options").waitForVisible();
		new VoodooControl("select", "id", "options").set("checkbox_dom");
		new VoodooControl("input", "id", "field_name_id").set(accountRecord
				.get("customField"));
		VoodooUtils.pause(2000);
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		// Takes about 40 seconds to reload page
		VoodooUtils.pause(50000);

		VoodooUtils.focusDefault();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLink.click();
		studioLinkAccounts.click();
		new VoodooControl("a", "css", "td#layoutsBtn a").click();
		new VoodooControl("a", "css", "td#viewBtnrecordview a").click();
		new VoodooControl("div", "css", ".le_row.special").dragNDrop(dragToRow);
		new VoodooControl("div", "css", "div[data-name='mydropdown_c']")
				.dragNDrop(dragToFiller);
		new VoodooControl("input", "id", "publishBtn").click();

	}

	/**
	 * Verify the Mass update of a Custom field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_26411_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusDefault();
		sugar().accounts.navToListView();

		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("myDropDown", "No");

		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.massUpdate.performMassUpdate(massUpdateData);
		VoodooUtils.pause(1000);

		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		new VoodooControl("div", "css", ".fld_mydropdown_c.detail div")
				.assertEquals("No", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
