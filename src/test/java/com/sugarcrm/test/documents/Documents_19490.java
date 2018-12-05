package com.sugarcrm.test.documents;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Documents_19490 extends SugarTest {
	DataSource contactsData = new DataSource();
	BWCSubpanel contactsSubpanelCtrl;

	public void setup() throws Exception {
		contactsData = testData.get(testName + "_" + sugar().contacts.moduleNamePlural);
		sugar().documents.api.create();
		sugar().contacts.api.create(contactsData);

		// Login
		sugar().login();

		// Navigate to the Document detail view
		sugar().documents.navToListView();
		sugar().documents.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// More than one pages should exist on Contacts sub panel of Document
		// Link all Contacts records to the Document record
		// TODO: VOOD-826 and VOOD-1713
		contactsSubpanelCtrl = sugar().documents.detailView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		VoodooUtils.focusDefault();
		contactsSubpanelCtrl.expandSubpanelActionsMenu();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "documents_contacts_select_button").click();
		VoodooUtils.focusWindow(1);
		VoodooControl selectAllCtrl = new VoodooControl("input", "id", "massall_top");
		selectAllCtrl.waitForVisible(); // Wait needed
		selectAllCtrl.click();
		// TODO: VOOD-1787
		VoodooUtils.pause(3000);
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame"); 
	}

	/**
	 * Documents - Contacts management_Verify that the "Start" pagination function in the "Contacts" sub-panel works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Documents_19490_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet subpanelData = testData.get(testName).get(0);

		// Need to sort the linked Contacts as order is rendom
		// TODO: VOOD-1534 and VOOD-1828
		new VoodooControl("img", "css", "#list_subpanel_contacts .listViewThLinkS1 img").click();
		VoodooUtils.waitForReady();

		// Define controls for Contacts Subpanel
		// TODO: VOOD-826, VOOD-1713 and VOOD-1980
		VoodooControl nextBtnCtrl = new VoodooControl("button", "css", "#list_subpanel_contacts button[name='listViewNextButton']");
		VoodooControl startButton = new VoodooControl("button", "css", "#list_subpanel_contacts .pagination button[name='listViewStartButton']");
		VoodooControl prevButton = new VoodooControl("button", "css", "#list_subpanel_contacts .pagination button[name='listViewPrevButton']");
		VoodooControl endButton = new VoodooControl("button", "css", "#list_subpanel_contacts .pagination button[name='listViewEndButton']");
		VoodooControl pageNumbersCtrl = new VoodooControl("span", "css", "#list_subpanel_contacts .pagination .pageNumbers");
		VoodooControl subpanelEvenRowRecords =  new VoodooControl("span", "css", "#list_subpanel_contacts .evenListRowS1");
		VoodooControl subpanelOddRowRecords =  new VoodooControl("span", "css", "#list_subpanel_contacts .oddListRowS1");

		// Documents and Contact records exist, and "Start" link is valid".(start link will be valid if we move on next page)
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that Start and Previous are Enabled, Next and End button are disabled when you are on the Last Page
		assertFalse("Start button enabled when you are on the Last Page!!", startButton.isDisabled());
		assertFalse("Previous button enable when you are on the Last Page!!", prevButton.isDisabled());
		assertTrue("Next button disabled when you are on the Last Page!!", nextBtnCtrl.isDisabled());
		assertTrue("End button disabled when you are on the Last Page!!", endButton.isDisabled());
		pageNumbersCtrl.assertContains(subpanelData.get("secondPage"), true);

		// Verify that only Two record should displayed
		Assert.assertTrue("not exactly two records are displayed", subpanelEvenRowRecords.countWithClass() + subpanelOddRowRecords.countWithClass() == 2);
		contactsSubpanelCtrl.assertContains(contactsData.get(0).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(1).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(2).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(3).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(4).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(5).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(6).get("lastName"), false);

		// Click "Start" link in the "Contacts" sub-panel
		startButton.click();
		VoodooUtils.waitForReady();

		// Verify that the list view changes to the start page correctly
		// Verify that Start and Previous are Disabled, Next and End button are Enabled when you are on the Last Page
		assertTrue("Start button disabled when you are on the Last Page!!", startButton.isDisabled());
		assertTrue("Previous button disabled when you are on the Last Page!!", prevButton.isDisabled());
		assertFalse("Next button enabeld when you are on the Last Page!!", nextBtnCtrl.isDisabled());
		assertFalse("End button enabled when you are on the Last Page!!", endButton.isDisabled());
		pageNumbersCtrl.assertContains(subpanelData.get("firstPage"), true);

		// Verify that only Five record should displayed
		Assert.assertTrue("not exactly Five records are displayed", subpanelEvenRowRecords.countWithClass() + subpanelOddRowRecords.countWithClass() == 5);
		contactsSubpanelCtrl.assertContains(contactsData.get(0).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(1).get("lastName"), false);
		contactsSubpanelCtrl.assertContains(contactsData.get(2).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(3).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(4).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(5).get("lastName"), true);
		contactsSubpanelCtrl.assertContains(contactsData.get(6).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}