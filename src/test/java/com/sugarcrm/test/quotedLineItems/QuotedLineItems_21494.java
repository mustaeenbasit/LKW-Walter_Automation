package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class QuotedLineItems_21494 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that selecting a Quote (Line Item) in Contact Detail view works
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_21494_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// TODO: VOOD-1000 QuotedLineItems Sub-panel missing from Contacts record view
		// Scroll to QuotedLineItems sub-panel, toggle drop-down and select 'Link existing'
		VoodooControl quotedLineItemsSubPanel = new VoodooControl("a", "css", ".layout_Products  .flex-list-view");
		quotedLineItemsSubPanel.scrollIntoViewIfNeeded(false);
		new VoodooControl("a", "css", ".layout_Products .btn.dropdown-toggle").click();
		new VoodooControl("a", "css", ".dropdown-menu .panel-top.fld_select_button .rowaction").click();
		VoodooUtils.waitForReady();

		// Select first record in list and click on 'Link'
		sugar().quotedLineItems.searchSelect.selectRecord(1);
		sugar().quotedLineItems.searchSelect.link();

		// Verify that the QuotedLineItems is successfully added in Contacts sub-panel
		quotedLineItemsSubPanel.scrollIntoViewIfNeeded(false);

		// TODO: VOOD-1000
		new VoodooControl("a", "css", ".layout_Products tr.single td span.fld_name div").assertEquals(sugar().quotedLineItems.defaultData.get("name"), true);
		new VoodooControl("a", "css", ".layout_Products tr.single td span.fld_contact_name div").assertEquals(sugar().contacts.defaultData.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}