package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_28103 extends SugarTest {
	VoodooControl moduleCtrl,studioLinkCtrl,shippingCtrl;

	public void setup() throws Exception {
		sugar().quotes.api.create();
		sugar().login();	

		// Go to Admin > Shipping Provider
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		shippingCtrl = sugar().admin.adminTools.getControl("shippingProvider");
		shippingCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1195
		// Create two Shipping Provider
		for(int i = 1; i <= 2; i++) {
			new VoodooControl("input", "css", "input[name='New']").click();
			VoodooUtils.waitForReady();
			new VoodooControl("input", "css", "input[name='name']").set(testName+"_"+i);
			new VoodooControl("input", "css", "input[title='Save']").click();
			VoodooUtils.waitForReady();
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Shipping Provider selection window should not blank when trying to add the option in quote edit view
	 * @throws Exception
	 */
	@Test
	public void Quotes_28103_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin > Studio > Quotes > Layout > Edit View
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLinkCtrl = sugar().admin.adminTools.getControl("studio");
		studioLinkCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Quotes");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtneditview").click();
		VoodooUtils.waitForReady();

		// Add the "Shipping Provider" to the layout in Quotes.
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl); 
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name=shipper_name]").dragNDrop(moveToNewFilter);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to the Quotes Module. Choose a quote to edit or create a new quote.
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-865
		// Populate required fields. Select the arrow to choose the Shipping Provider.
		new VoodooControl("button", "id", "btn_shipper_name").click();
		VoodooUtils.focusWindow(1);

		// Verify that the blank screen should not be displayed and should be able to choose the option of shipping provider.
		VoodooControl row1 = new VoodooControl("a", "css", "tr.oddListRowS1 td a");
		row1.assertVisible(true);
		new VoodooControl("a", "css", "tr.evenListRowS1 td a").assertVisible(true);
		row1.click();
		VoodooUtils.focusWindow(0);

		// Cancel the quote record
		sugar().quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}