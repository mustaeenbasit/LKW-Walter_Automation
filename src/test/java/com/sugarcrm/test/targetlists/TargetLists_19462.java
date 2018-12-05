package com.sugarcrm.test.targetlists;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19462 extends SugarTest {
	public void setup() throws Exception {
		sugar.campaigns.api.create();

		// Create 20 TargetList records via API
		DataSource createTargets = testData.get(testName+"_createTargets");
		sugar.targetlists.api.create(createTargets);

		// Login
		sugar.login();
	}

	/**
	 * Verify that the pagination functions in the "Target Lists" listview works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19462_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource pages = testData.get(testName);

		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Link Existing Record to select the target list.
		// TODO: VOOD-1028 - Need library support to Link Existing Record in Campaign detail view
		new VoodooControl("div", "id", "list_subpanel_prospectlists").scrollIntoView();
		new VoodooControl("span", "css", "#list_subpanel_prospectlists .ab").click();
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.waitForReady();
		// Moving the focus to the pop-up window to select the target lists i.e to be linked with the campaign
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();

		// Bringing the focus back to the campaign detail view
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "list_subpanel_prospectlists").scrollIntoView();

		// TODO: VOOD-1072 - Library support needed for controls in Campaign Detail view
		VoodooControl startButton = new VoodooControl("button", "css", "#list_subpanel_prospectlists .pagination button[name='listViewStartButton']");
		VoodooControl nextButton = new VoodooControl("button", "css", "#list_subpanel_prospectlists .pagination button[name='listViewNextButton']");
		VoodooControl prevButton = new VoodooControl("button", "css", "#list_subpanel_prospectlists .pagination button[name='listViewPrevButton']");
		VoodooControl endButton = new VoodooControl("button", "css", "#list_subpanel_prospectlists .pagination button[name='listViewEndButton']");
		VoodooControl pageNumbers = new VoodooControl("span", "css", "#list_subpanel_prospectlists .pagination .pageNumbers");
		String pageNumbersText = pageNumbers.getText().trim();

		// Assert that the first page is displayed
		assertTrue("Start button enabled when you are on the First Page!!", startButton.isDisabled());
		assertTrue("Previous button enabled when you are on the First Page!!", prevButton.isDisabled());
		assertTrue("You are not on the first page!!", pageNumbersText.equals(pages.get(0).get("pageNumber")));

		// Click on the next button and assert that the 2nd page is displayed
		assertFalse("Only one page exists!!", nextButton.isDisabled());
		nextButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		assertTrue("Second page is not displayed!!", pageNumbersText.equals(pages.get(1).get("pageNumber")));

		// Click on the next button and assert that the 3rd page is displayed
		assertFalse("Only two pages exist!!", nextButton.isDisabled());
		nextButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		assertTrue("Third page is not displayed!!", pageNumbersText.equals(pages.get(2).get("pageNumber")));

		// Click previous button and assert that the 2nd page is displayed
		assertFalse("Previous button is disabled!!", prevButton.isDisabled());
		prevButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		Assert.assertTrue("2nd page is not displayed on clicking previous!!", pageNumbersText.equals(pages.get(1).get("pageNumber")));

		// Click previous button and assert that 1st page is displayed
		assertFalse("Previous button is disabled!!", prevButton.isDisabled());
		prevButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		assertTrue("1st page is not displayed!!", pageNumbersText.equals(pages.get(0).get("pageNumber")));
		assertTrue("Start button enabled when you are on the First Page!!", startButton.isDisabled());
		assertTrue("Previous button enabled when you are on the First Page!!", prevButton.isDisabled());

		// Click on the end button and assert that the last page is displayed
		assertFalse("Only one page exists!!", endButton.isDisabled());
		endButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		assertTrue("Last page is not displayed!!", pageNumbersText.equals(pages.get(3).get("pageNumber")));

		// Click the start button and assert that the first page is displayed
		assertFalse("Start button is disabled!!", startButton.isDisabled());
		startButton.click();
		VoodooUtils.waitForReady();
		pageNumbersText = pageNumbers.getText().trim();
		assertTrue("You are not on the first page!!", pageNumbersText.equals(pages.get(0).get("pageNumber")));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}