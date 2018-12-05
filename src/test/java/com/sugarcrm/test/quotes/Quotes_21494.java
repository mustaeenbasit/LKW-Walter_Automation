package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Quotes_21494 extends SugarTest {
	public void setup() throws Exception {
		sugar.contacts.api.create();
		sugar.quotes.api.create();
		sugar.login();		
	}

	/**
	 * Verify that selecting a Quote in Contact Detail view works
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_21494_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);

		// TODO: VOOD-1000 Quotes Sub-panel missing from Contacts record view
		VoodooControl quotesSubPanel = new VoodooControl("a", "css", ".layout_Quotes  .flex-list-view");
		VoodooControl expandToggleCtrl = new VoodooControl("a", "css", ".layout_Quotes .btn.dropdown-toggle");		
		VoodooControl linkExistingCtrl = new VoodooControl("a", "css", ".dropdown-menu .panel-top.fld_select_button .rowaction");
		VoodooControl selectionListCtrl = new VoodooControl("span", "css", ".fld_name.list");
		VoodooControl listItemSelectCtrl = new VoodooControl("input", "css", ".single .list input");
		VoodooControl linkButtonCtrl = new VoodooControl("a", "name", "link_button");
		VoodooControl listItemNameCtrl = new VoodooControl("a", "css", ".layout_Quotes tr.single td span.fld_name div");
		
		// Scroll to Quotes sub-panel, toggle drop-down and select 'Link existing'
		quotesSubPanel.scrollIntoViewIfNeeded(false);
		expandToggleCtrl.click();		
		linkExistingCtrl.click();
		
		// Select first record in list and click on 'Link'
		selectionListCtrl.waitForVisible();
		listItemSelectCtrl.click();
		linkButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that the quote is successfully added in Contacts sub-panel
		quotesSubPanel.scrollIntoViewIfNeeded(false);
		listItemNameCtrl.assertEquals(sugar.quotes.defaultData.get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}