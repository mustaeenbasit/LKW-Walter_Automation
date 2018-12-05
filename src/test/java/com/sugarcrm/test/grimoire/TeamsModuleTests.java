package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TeamsModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		// Navigate to Teams
		sugar().teams.navToListView();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		VoodooUtils.focusFrame("bwc-frame");

		// module title and basic clear search with paginations
		sugar().teams.listView.getControl("moduleTitle").assertExists(true);
		sugar().teams.listView.getControl("nameBasic").assertExists(true);
		sugar().teams.listView.getControl("searchButton").assertExists(true);
		sugar().teams.listView.getControl("clearButton").assertExists(true);
		sugar().teams.listView.getControl("startButton").assertExists(true);
		sugar().teams.listView.getControl("endButton").assertExists(true);
		sugar().teams.listView.getControl("nextButton").assertExists(true);
		sugar().teams.listView.getControl("prevButton").assertExists(true);

		// action dropdowns
		sugar().teams.listView.getControl("selectAllCheckbox").assertExists(true);
		sugar().teams.listView.getControl("selectDropdown").assertExists(true);
		sugar().teams.listView.getControl("actionDropdown").assertExists(true);
		sugar().teams.listView.getControl("massUpdateButton").assertExists(true);
		sugar().teams.listView.getControl("exportButton").assertExists(true);
		sugar().teams.listView.getControl("deleteButton").assertExists(true);
		sugar().teams.listView.getControl("selectThisPage").assertExists(true);
		sugar().teams.listView.getControl("selectAll").assertExists(true);
		sugar().teams.listView.getControl("deselectAll").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElements() test complete");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		// Verify 'Teams' module title
		String expected = sugar().teams.moduleNamePlural;
		String found = sugar().teams.listView.getModuleTitle();
		assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyModuleTitle() test complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems() ...");

		// Verify that all menus are present
		sugar().navbar.clickModuleDropdown(sugar().teams);
		VoodooControl createTeam = sugar().teams.menu.getControl("createTeam");
		VoodooControl viewTeam = sugar().teams.menu.getControl("viewTeams");
		createTeam.assertVisible(true);
		viewTeam.assertVisible(true);

		// Verify that "Create Team" menu is functional
		createTeam.click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl cancel = sugar().teams.editView.getControl("cancel");
		cancel.assertVisible(true);
		cancel.click();
		VoodooUtils.focusDefault();

		// Verify that "View Teams" menu is functional
		sugar().navbar.clickModuleDropdown(sugar().teams);
		viewTeam.click();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("moduleTitle").assertEquals("Search Teams", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifySelectionMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySelectionMenu()...");

		VoodooUtils.focusFrame("bwc-frame");

		// Verify selection menus and action dropdowns
		sugar().teams.listView.getControl("selectAllCheckbox").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().teams.listView.openSelectDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("selectThisPage").assertVisible(true);
		sugar().teams.listView.getControl("selectAll").assertVisible(true);
		sugar().teams.listView.getControl("deselectAll").assertVisible(true);

		// Reset dropdown
		sugar().teams.listView.getControl("selectDropdown").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifySelectionMenu() test complete.");
	}

	@Test
	public void verifyRecordActionMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyRecordActionMenu()...");

		// Verify action menus for record
		sugar().teams.listView.checkRecord(1);
		sugar().teams.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("deleteButton").assertVisible(true);
		sugar().teams.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().teams.listView.getControl("exportButton").assertVisible(true);

		// Reset dropdown and uncheck record
		sugar().teams.listView.getControl("actionDropdown").click();
		VoodooUtils.focusDefault();
		sugar().teams.listView.uncheckRecord(1);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyRecordActionMenu() test complete.");
	}

	@Test
	public void verifyToggle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyToggle()...");

		// Verify toggle favorite, check-uncheck, select-deselect record
		VoodooControl favIcon = sugar().teams.listView.getControl("favoriteStar01");
		VoodooControl firstCheckBox = sugar().teams.listView.getControl("checkbox01");
		VoodooControl secondCheckBox = sugar().teams.listView.getControl("checkbox02");
		VoodooControl thirdCheckBox = sugar().teams.listView.getControl("checkbox03");

		// toggle favorite
		sugar().teams.listView.toggleFavorite(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		favIcon.assertAttribute("class", "on", true);
		VoodooUtils.focusDefault();
		sugar().teams.listView.toggleFavorite(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		favIcon.assertAttribute("class", "off", true);
		VoodooUtils.focusDefault();

		// toggle record checkbox (check/uncheck)
		sugar().teams.listView.toggleRecordCheckbox(2);
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("Second checkbox is unchecked", secondCheckBox.isChecked());
		VoodooUtils.focusDefault();
		sugar().teams.listView.toggleRecordCheckbox(2);
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("Second checkbox is checked", !secondCheckBox.isChecked());
		VoodooUtils.focusDefault();

		// toggle all record selection (select/deselect)
		sugar().teams.listView.toggleSelectAll();
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("First checkbox is unchecked", firstCheckBox.isChecked());
		Assert.assertTrue("Second checkbox is unchecked", secondCheckBox.isChecked());
		Assert.assertTrue("Third checkbox is unchecked", thirdCheckBox.isChecked());
		VoodooUtils.focusDefault();
		sugar().teams.listView.toggleSelectAll();
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("First checkbox is checked", !firstCheckBox.isChecked());
		Assert.assertTrue("Second checkbox is checked", !secondCheckBox.isChecked());
		Assert.assertTrue("Third checkbox is checked", !thirdCheckBox.isChecked());
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyToggle() test complete");
	}

	@Test
	public void verifyBasicSearch() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyBasicSearch()...");

		String searchStr = "Administrator";

		// Verify search record
		sugar().teams.listView.basicSearch("Blank");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("link01").assertExists(false);
		VoodooUtils.focusDefault();
		sugar().teams.listView.basicSearch(searchStr);
		sugar().teams.listView.verifyField(1, "name", searchStr);
		sugar().teams.listView.clearSearchForm();
		sugar().teams.listView.submitSearchForm();

		VoodooUtils.voodoo.log.info("verifyBasicSearch() test complete.");
	}

	@Test
	public void verifySearchDeleteTeam() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySearchDeleteTeam()...");

		sugar().teams.api.create();
		sugar().teams.deleteTeam(sugar().teams.getDefaultData().get("name"));

		// Verify team is deleted
		sugar().teams.listView.verifyField(1, "name", "West");

		VoodooUtils.voodoo.log.info("verifySearchDeleteTeam() test complete.");
	}

	@Test
	public void verifyDetailRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyDetailRecord()...");

		// Verify detail record
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.detailView.getDetailField("name").assertContains("West", true);
		sugar().teams.detailView.openPrimaryButtonDropdown();
		sugar().teams.detailView.getControl("deleteButton").assertVisible(true);
		sugar().teams.detailView.getControl("copyButton").assertVisible(true);
		sugar().teams.detailView.getControl("primaryButtonDropdown").click(); // To reset dropdown
		sugar().teams.detailView.subpanels.get(sugar().users.moduleNamePlural).assertElementContains("No data", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyDetailRecord() test complete.");
	}

	@Test
	public void verifyEditRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditRecord()...");

		// Verify edit record
		sugar().teams.listView.editRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.editView.getEditField("name").assertVisible(true);
		sugar().teams.editView.getControl("cancel").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyEditRecord() test complete.");
	}

	@Test
	public void verifyAssignUserToTeam() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyAssignUserToTeam()...");

		sugar().teams.api.create();
		VoodooUtils.refresh();
		sugar().teams.listView.clickRecord(1);

		// Verify no team assign
		VoodooUtils.focusFrame("bwc-frame");
		BWCSubpanel userBwc = sugar().teams.detailView.subpanels.get(sugar().users.moduleNamePlural);
		userBwc.assertElementContains("No data", true);

		// TODO: VOOD-776, VOOD-805, VOOD-1066, VOOD-1307
		// Verify admin2 assign to newly created team and unlink
		userBwc.getControl("teamMembership").click();
		// TODO: CB-255 "Polling fails in WebDriverInterface.focusWindow() if the window doesn't exist yet.", pause required to execute test
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(3) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		userBwc.assertElementContains("admin2", true);
		userBwc.getControl("unlinkRecordRow01").click();
		VoodooUtils.acceptDialog();
		userBwc.assertElementContains("No data", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyAssignUserToTeam() test complete.");
	}

	public void cleanup() throws Exception {}
}