package com.sugarcrm.test.quotedLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_21499 extends SugarTest {

	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * New action dropdown list in Quote Line Items subpanel
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_21499_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);

		// TODO: VOOD-1032 - Need lib support for Documents subpanel in Quoted Line Items
		new VoodooControl("a", "css", ".layout_Documents .actions .dropdown-toggle").click();
		new VoodooControl("a", "css", ".fld_select_button .rowaction").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".layout_Documents .search-and-select .toggle-all").click();
		new VoodooControl("a", "css", "a[name='link_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Documents").scrollIntoViewIfNeeded(false);

		// Assert that the linked Document record has been added in the subpanel
		Assert.assertTrue("The record is different!!", (new VoodooControl("td", "css", ".layout_Documents .single:nth-child(1) td:nth-child(2)").getText().trim()).equals(sugar().documents.getDefaultData().get("documentName")));

		// Create a document record from the subpanel
		new VoodooControl("i", "css", "div[data-voodoo-name='Documents'] .fa-plus").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-978 - Need Lib support for Doc Create via UI
		VoodooFileField browseFile = new VoodooFileField("input", "id", "filename_file");

		// File Name is a mandatory field to create document record via UI, Hence took an existing csv file
		browseFile.set("src/main/resources/data/DocumentsModuleFields.csv");
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().documents.editView.save();
		new VoodooControl("a", "css", ".layout_Documents").scrollIntoViewIfNeeded(false);

		// Assert that the document record created from the subpanel has been added in the subpanel
		Assert.assertTrue("The record is different!!", (new VoodooControl("td", "css", ".layout_Documents .single:nth-child(1) td:nth-child(2)").getText().trim()).equals("DocumentsModuleFields.csv"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}