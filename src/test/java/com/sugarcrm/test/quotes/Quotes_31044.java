package com.sugarcrm.test.quotes;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Quotes_31044 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();

		sugar().login();
		// Navigate to Admin -> Studio -> Quotes -> Fields.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl studioLinkCtrl = sugar().admin.adminTools.getControl("studio");
		studioLinkCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-517 - Create Studio Module (BWC)
		VoodooControl quotesControl = new VoodooControl("a", "id", "studiolink_Quotes");
		quotesControl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		//  Click on "Quote Type" field and make it required field.
		new VoodooControl("a", "id", "quote_type").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='required']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		quotesControl.click();

		// Navigate to Layout -> Edit View -> Add New Row
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtneditview").click();
		VoodooUtils.waitForReady();

		// Add the Quote Type field on the Layout.
		new VoodooControl("div", "css", "#toolbox .le_panel.special").dragNDrop(new VoodooControl("div", "css", "#panels"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special").dragNDrop(new VoodooControl("div", "css", "[data-name='quote_type']"));

		// Check the "Sync to Detail View" option available on the top on Panel.
		new VoodooControl("input", "id", "syncCheckbox").click();

		// Save & Deploy.
		new VoodooControl("input", "css", "#publishBtn").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that a quote record can be saved after quote type field is added to the edit/detail layout
	 *
	 * @throws Exception
	 */
	@Test
	public void Quotes_31044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		// create a quote record with "Quote" radio button checked for quote type field. Fill the rest of the required fields and click Save.
		sugar().quotes.create();

		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-930 - Library support needed for controls on Quote editview
		// Verify that Quote record is saved successfully.
		sugar().quotes.detailView.getDetailField("name").assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		// Verify that Quote Type field shows "Quote"
		VoodooControl quoteType = new VoodooControl("span", "css", "#quote_type");
		quoteType.assertEquals(sugar().quotes.moduleNameSingular, true);
		VoodooUtils.focusDefault();

		// edit quote record
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// Change quote type field from "Quote" to "Order"
		// TODO: VOOD-930 - Library support needed for controls on Quote editview
		new VoodooControl("input", "css", "[value='Orders']").click();
		VoodooUtils.focusDefault();

		// save the record
		sugar().quotes.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that Quote Record is saved successfully. Quote Type field shows "Order"
		sugar().quotes.detailView.getDetailField("name").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		quoteType.assertEquals(fs.get("quote_type"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}