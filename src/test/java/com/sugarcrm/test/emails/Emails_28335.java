package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_28335 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		// Initializing test data
		customData = testData.get(testName).get(0);
		
		// Creating test contact record
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();
		
		// Login as admin
		sugar().login();
		
		// Editing contact record (adding email address)
		// TODO: VOOD-444 - Support creating relationships via API
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", customData.get("emailAddress"));
		myContact.edit(fs);
	}

	/**
	 * Verify that select one email address at a time gets highlighted by a regular click
	 * @throws Exception
	 */
	@Test
	public void Emails_28335_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Navigate to Contacts
		sugar().contacts.navToListView();
		
		// Clicking email address value with the Contact Record
		sugar().contacts.listView.getDetailField(1, "emailAddress").scrollIntoViewIfNeeded(sugar().contacts.listView.getControl("horizontalScrollBar"), true);

		// TODO: VOOD-843 - Lib support to handle new email composer UI
		// Verify that Compose Email page is open
		new VoodooControl("span", "css", "div.layout_Emails span.module-title").assertContains(customData.get("emailHeader"), true);

		// Select multiple emails from Address Book
		new VoodooControl("a", "css", "a[data-name='to_addresses']").click();
		VoodooUtils.waitForReady();
		
		// Select qauser, admin2, Administrator
		new VoodooControl("input", "css", "[name='Users_3'] input").click();
		new VoodooControl("input", "css", "[name='Users_2'] input").click();
		new VoodooControl("input", "css", "[name='Users_1'] input").click();
		
		// Click 'Done' button
		new VoodooControl("a", "css", ".active .fld_done_button a").click();
		VoodooUtils.waitForReady();
		
		// Verify that multiple emails appear in TO textbox
		// TODO: VOOD-844 - Lib support for handling inside of Address books in Email
		VoodooControl addressToInputBox = new VoodooControl("ul", "css", ".fld_to_addresses.edit .select2-choices");
		addressToInputBox.assertContains(sugar().users.getQAUser().get("userName"), true);
		addressToInputBox.assertContains(customData.get("administrator"), true);
		addressToInputBox.assertContains(customData.get("admin2"), true);

		// Click qauser		
		// Using xPath in order to select the element (pill) having text as 'qauser' below and henceforth
		new VoodooControl("li", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'qauser')]").click();
		VoodooUtils.waitForReady();

		// Verify its background turns blue - rgba(23, 109, 229, 1)
		new VoodooControl("li", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'qauser')]").assertCssAttribute("background-color", customData.get("bgColor1"));
		
		// Enable cc, bcc
		new VoodooControl("a", "css", "a[data-toggle-field='cc_addresses']").click();
		new VoodooControl("a", "css", "a[data-toggle-field='bcc_addresses']").click();
		
		// Drag qauser to cc
		new VoodooControl("li", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'qauser')]")
			.dragNDrop(new VoodooControl("li", "xpath", "//span[@class='fld_cc_addresses edit']/div/ul"));
		VoodooUtils.waitForReady();
		
		// Verify that qauser is no longer in to field
		addressToInputBox.assertContains(sugar().users.getQAUser().get("userName"), false);
		
		// Verify that qauser now appears in cc field
		new VoodooControl("ul", "css", ".fld_cc_addresses.edit .select2-choices").assertContains(sugar().users.getQAUser().get("userName"), true);
		
		// Verify its background is black
		new VoodooControl("li", "xpath", "//span[@class='fld_cc_addresses edit']/div/ul/li[contains(.,'qauser')]").assertCssAttribute("background-color", customData.get("bgColor2"));
		
		// Click admin2
		VoodooControl admin2Pill = new VoodooControl("li", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'admin2')]");
		admin2Pill.click();
		VoodooUtils.waitForReady();
		
		// Verify its background turns blue
		admin2Pill.assertCssAttribute("background-color", customData.get("bgColor1"));
		
		// Click on blank space in to field
		new VoodooControl("div", "css", "span.fld_to_addresses div").click();
		
		// Verify its background turns black
		admin2Pill.assertCssAttribute("background-color", customData.get("bgColor2"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
