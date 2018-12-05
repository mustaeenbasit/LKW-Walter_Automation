package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27710 extends SugarTest {
	VoodooControl contactsModuleCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, publishButton, panelOne, panelTwo;;
	ContactRecord myContact;
	FieldSet fs;
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();
		myContact = (ContactRecord)sugar().contacts.api.create();

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-938
		contactsModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");

		// Contacts > Layouts > Record View 
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();

		// Change all of the panels to be displayed as a 'Tab'
		panelOne = new VoodooControl("select", "css", "#panels > div:nth-child(2) > span:nth-child(3) > select");
		panelTwo = new VoodooControl("select", "css", "#panels > div:nth-child(3) > span:nth-child(3) > select");

		panelOne.set(fs.get("tab_select"));
		panelTwo.set(fs.get("tab_select"));
		//  Click 'Save & Deploy'
		publishButton = new VoodooControl("input", "id", "publishBtn");
		publishButton.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the create view is presented with the primary tab in focus instead of last view tab
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27710_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to contact record
		myContact.navToRecord();

		//  Select an alternate tab to display the appropriate information
		new VoodooControl("li", "css", "#recordTab > li.tab.panel_hidden").click();

		// Go to Contacts > Create Contact
		sugar().navbar.selectMenuItem(sugar().contacts, "createContact");
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl tab1 = new VoodooControl("li", "css", "#recordTab > li.tab.panel_body.active > a");
		VoodooControl tab2 = new VoodooControl("li", "css", "#recordTab > li.tab.panel_hidden > a");

		// Assert that, The create view should presented with the primary tab in focus and not with the last viewed tab
		tab1.assertContains(fs.get("first_tab_name"), true);
		tab2.assertContains(fs.get("second_tab_name"), true);

		// Go to Quick Create -> Create Contact.
		sugar().navbar.quickCreateAction(sugar().contacts.moduleNamePlural);

		// Assert that, The create view should presented with the primary tab in focus and not with the last viewed tab
		tab1.assertContains(fs.get("first_tab_name"), true);
		tab2.assertContains(fs.get("second_tab_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}