package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;

import com.sugarcrm.sugar.UnfoundElementException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class VoodooUtilsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void testBrowserTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running testBrowserTitle()...");
		
		String expected = "My Dashboard » Home » SugarCRM";
		String found = VoodooUtils.getTitle();
		assertTrue("testBrowserTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

		VoodooUtils.voodoo.log.info("testBrowserTitle() complete.");
	}
	
	@Test
	public void testBrowserActionMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running testBrowserActionMenuItems()...");
		
		String currentPageURL = VoodooUtils.getUrl();
		sugar().accounts.navToListView();
		String nextPageURL = VoodooUtils.getUrl();
		
		VoodooUtils.back();
		assertTrue("Browser doesnot back from Accounts list view to Home Dashboard View", VoodooUtils.getUrl().equals(currentPageURL));

		VoodooUtils.forward();
		assertTrue("Browser doesot forward from Home Dashboard View to Accounts list view", VoodooUtils.getUrl().equals(nextPageURL));
		
		VoodooUtils.voodoo.log.info("testBrowserActionMenuItems() complete.");
	}
	
	@Test
	public void testWaitForReady() throws Exception {
		VoodooUtils.voodoo.log.info("Running testWaitForReady...");

		VoodooUtils.waitForReady();

		long time = System.currentTimeMillis();
		VoodooUtils.waitForReady(15000);

		// Test that once waitForReady is called, a second waitForReady returns quickly
		assertTrue(System.currentTimeMillis() - time < 1000);

		// Manually navigate to accounts and dynamically wait for the page to load
		sugar().navbar.showAllModules();
		VoodooControl topNavLink = new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='Accounts'] a");
		topNavLink.click();
		VoodooUtils.waitForReady();
		sugar().accounts.listView.getControl("createButton").click();
		VoodooUtils.waitForReady(5000);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("testWaitForReady complete.");
	}

	@Test
	public void testWaitForReadyPortal() throws Exception {
		VoodooUtils.voodoo.log.info("Running testWaitForReadyPortal");

		VoodooUtils.waitForReady();
		sugar().admin.portalSetup.enablePortal();
		portal.loginScreen.navigateToPortal();
		VoodooUtils.waitForReady();
		sugar().loginScreen.navigateToSugar();
		sugar().admin.portalSetup.disablePortal();

		VoodooUtils.voodoo.log.info("testWaitForReadyPortal complete.");
	}

	@Test
	public void testWaitForReadyBWC() throws Exception {
		VoodooUtils.voodoo.log.info("Running testWaitForReadyBWC");

		sugar().campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info("testWaitForReadyBWC complete");
	}

	@Test
	public void testExecuteJS () throws Exception {
		VoodooUtils.voodoo.log.info("Running testExecuteJS");

		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		int rowsCount = (int) ((long) VoodooUtils.executeJS(
				"return jQuery('.flex-list-view-content table.table.table-striped.dataTable." +
						"reorderable-columns tbody tr').length;"));

		assertTrue("Accounts listview row count not =0", rowsCount == 0);

		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().accounts.api.create();

		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		rowsCount = (int) ((long) VoodooUtils.executeJS(
				"return jQuery('.flex-list-view-content table.table.table-striped.dataTable." +
						"reorderable-columns tbody tr').length;"));

		assertTrue("Accounts listview row count not =3", rowsCount == 3);

		sugar().accounts.api.deleteAll();

		VoodooUtils.voodoo.log.info("testExecuteJS complete");
	}

	@Test
	public void testTakeScreenshot() {
		// Throw an UnfoundElementException to create a screenshot,
		// and catch it so the test does not fail as a result.
		try {
			throw new UnfoundElementException("<a id='unfound'>");
		} catch (UnfoundElementException uee) {
			final File folder = new File("./log");

			// Grab a list of screenshots in the log folder containing the name of this test.
			File[] matchingFiles = folder.listFiles( new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return name.matches( ".*testTakeScreenshot.*\\.png" );
				}
			} );

			// The list obtained in the previous line should contain exactly one match.
			assertEquals(matchingFiles.length, 1);
		}

	}

	@Test
	public void testwaitForDialog() throws Exception {
		VoodooUtils.voodoo.log.info("Running testwaitForDialog");

		// Navigate to Teams
		sugar().teams.navToListView();

		// Create 1 team
		sugar().navbar.clickModuleDropdown(sugar().teams);
		sugar().teams.menu.getControl("createTeam").click();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.editView.getEditField("name").set(sugar().teams.getDefaultData().get("name"));
		sugar().teams.editView.getControl("save").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();

		sugar().teams.navToListView();

		sugar().teams.listView.basicSearch(sugar().teams.getDefaultData().get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("selectAllCheckbox").click();
		sugar().teams.listView.getControl("deleteButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		sugar().teams.listView.clearSearchForm();
		sugar().teams.listView.submitSearchForm();

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("testwaitForDialog complete");
	}

	public void cleanup() throws Exception {}
}