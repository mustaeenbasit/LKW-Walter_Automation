package com.sugarcrm.test.contracts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Contracts_19843 extends SugarTest {
	ArrayList<Module>modules = new ArrayList<Module>();

	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.quotedLineItems.api.create();
		sugar.login();

		// enable QLI and Contracts module
		modules.add(sugar.quotedLineItems);
		modules.add(sugar.contracts);
		sugar.admin.enableModuleDisplayViaJs(modules);

		// enable QLI from subpanel
		sugar.admin.enableSubpanelDisplayViaJs(sugar.quotedLineItems);

		// Go to contracts module and create a note record to this Contract
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		// Click "Select" button in "Contacts" sub-panel.
		new VoodooControl("a", "id", "contracts_products_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Click 
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Editing Product can be canceled for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19843_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// TODO: VOOD-1713
		// Click QLI link for a QLI record in "QLI" sub-panel.
		VoodooControl qliRecordCrtl = new VoodooControl("a", "css", "#whole_subpanel_products tr:nth-child(3) td:nth-child(1) a");
		qliRecordCrtl.click();

		// Verify that the User should be navigated to the QLI detail page. 
		sugar.quotedLineItems.recordView.assertExists(true);

		// Update some fields on the QLI detail page.
		sugar.quotedLineItems.recordView.edit();
		sugar.quotedLineItems.recordView.getEditField("name").set(testName);
		sugar.quotedLineItems.recordView.getEditField("quantity").set("20");

		// Cancel editing the product/QLI for Contract.
		sugar.quotedLineItems.recordView.cancel();

		// Navigate back to the Contracts detail page and observe the above clicked QLI in QLI subpanel
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Again click QLI in QLI subpanel
		qliRecordCrtl.click();
		VoodooUtils.waitForReady();

		// Verify that the Product/QLI detail page should display without any changes. 
		sugar.quotedLineItems.recordView.getDetailField("name").assertContains(sugar.quotedLineItems.getDefaultData().get("name"), true); // Name should be "TDX-2000" not "Contracts_19843"
		sugar.quotedLineItems.recordView.getDetailField("quantity").assertContains(sugar.quotedLineItems.getDefaultData().get("quantity"), true); // Quantity should be "1" not "20"

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}