package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_31258 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that no error displayed while creating duplicate contact records from "Select Contact" popup window under Quotes modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_31258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Navigate to quote and click on create quote
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");

		// Shipping Contact Name field -> Click on Arrow button
		sugar().quotes.editView.getEditField("shippingContactName").click();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-805, VOOD-1260
		VoodooControl createContact = new VoodooControl("input", "css", "#addformlink input");

		// Create contact and save
		createContact.click();
		new VoodooControl("input", "id", "first_name").set(customFS.get("firstName"));
		new VoodooControl("input", "id", "last_name").set(customFS.get("lastName"));
		VoodooControl saveBtn = new VoodooControl("input", "id", "Contacts_popupcreate_save_button");
		saveBtn.click();

		// Verify the record after save
		VoodooControl listRecords = new VoodooControl("table", "css", "table.list.view");
		listRecords.assertContains(customFS.get("fullName"), true);

		// Again create contact with same form values and save
		createContact.click();
		saveBtn.click();

		// Verify duplicate records are shown with Save and Cancel button thereafter click cancel
		VoodooControl moduleTitle = new VoodooControl("h2", "css", ".moduleTitle");
		moduleTitle.assertEquals(customFS.get("saveModuleTitle"), true);
		new VoodooControl("td", "css", "table tr:nth-of-type(2) table tr td").assertContains(customFS.get("duplicateText"), true);
		new VoodooControl("a", "css", ".oddListRowS1 td a").assertEquals(customFS.get("firstName"), true);
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(2) a").assertEquals(customFS.get("lastName"), true);
		new VoodooControl("input", "css", "input[title=Save]").assertVisible(true);
		new VoodooControl("input", "css", "input[title=Cancel]").click(); // click method already takes care if its visibility

		// Verify Contact list view form displayed and delete the record
		moduleTitle.assertContains(customFS.get("searchContactModuleTitle"), true);
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(4) a").assertEquals(customFS.get("fullName"), true);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "actionLinkTop").click();
		VoodooUtils.acceptDialog();

		// TODO: We need to verify record is deleted. Right now there is bug in <=v7.8 [See SFA-3370], whose fixVersion=7.9 (Showing blank page with error in console after delete). We need to fix this scenario, once we are against 7_9 branch		
		VoodooUtils.closeWindow();
		sugar().quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}