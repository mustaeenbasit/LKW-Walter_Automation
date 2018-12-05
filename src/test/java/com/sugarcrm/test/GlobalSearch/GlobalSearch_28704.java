package com.sugarcrm.test.GlobalSearch;

import org.junit.*;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28704 extends SugarTest {
	DataSource customData = new DataSource();
	String Account1 = "";
	String Account2 = "";

	public void setup() throws Exception {
		customData = testData.get(testName);
		String Account1 = customData.get(0).get("name");
		String Account2 = customData.get(1).get("name");
		
		// Creating one account record from api
		FieldSet fs = new FieldSet();
		fs.put("name", Account1);
		sugar().accounts.api.create(fs);
		sugar().login();
		
		// Creating another account record from UI and assigned it to qauser so that Created By Me facet should get enabled
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(Account2);
		sugar().accounts.createDrawer.getEditField("relAssignedTo").set(customData.get(0).get("assignedUser"));
		sugar().accounts.createDrawer.save();

		// Marking this record as favorite
		sugar().accounts.listView.toggleFavorite(1);
	}

	/**
	 * Verify filter conditions on facets
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28704_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter any value in the Search bar and hit enter
		// TODO: CB-252, VOOD-1437
		VoodooControl searchBar = sugar().navbar.getControl("globalSearch");
		searchBar.set(customData.get(0).get("searchString") + '\uE007');
		VoodooUtils.waitForReady();

		// TODO: VOOD-1848
		// Controls for facets
		VoodooControl assignedToMeFacet = new VoodooControl("div", "css", ".dashlet-cell li [data-voodoo-name='search-facet']");
		VoodooControl myFavoriteFacet = new VoodooControl("div", "css", ".dashlet-cell li:nth-child(2) [data-voodoo-name='search-facet']");
		VoodooControl createdByMeFacet = new VoodooControl("div", "css", ".dashlet-cell li:nth-child(3) [data-voodoo-name='search-facet']");
		VoodooControl modifiedbyMeFacet = new VoodooControl("div", "css", ".dashlet-cell li:nth-child(4) [data-voodoo-name='search-facet']");
		VoodooControl globalSearchResultRows = new VoodooControl("li", "css", ".nav.search-results .search-result");

		// Verifying facets
		assignedToMeFacet.assertContains(customData.get(0).get("facetName"), true);
		myFavoriteFacet.assertContains(customData.get(1).get("facetName"), true);
		createdByMeFacet.assertContains(customData.get(2).get("facetName"), true);
		modifiedbyMeFacet.assertContains(customData.get(3).get("facetName"), true);

		// Verifying two records are appearing for accounts in global search result when facets are not selected
		Assert.assertTrue(globalSearchResultRows.countAll() == 2);
		sugar().globalSearch.getRow(1).assertContains(Account1, true);
		sugar().globalSearch.getRow(2).assertContains(Account2, true);
		sugar().globalSearch.getRow(3).assertVisible(false);

		// Verifying search result gives only one record i.e Account1 when selecting 'Assigned to Me' facet
		assignedToMeFacet.click();
		VoodooUtils.waitForReady();
		Assert.assertTrue(globalSearchResultRows.countAll() == 1);
		sugar().globalSearch.getRow(1).assertContains(Account1, true);
		sugar().globalSearch.getRow(2).assertVisible(false);
		// Unselect Assigned to me facet
		assignedToMeFacet.click();
		VoodooUtils.waitForReady();

		// Verifying search result gives only one record i.e Account2 when selecting 'My Favorite' facet
		myFavoriteFacet.click();
		VoodooUtils.waitForReady();
		Assert.assertTrue(globalSearchResultRows.countAll() == 1);
		sugar().globalSearch.getRow(1).assertContains(Account2, true);
		sugar().globalSearch.getRow(2).assertVisible(false);
		// Unselect 'My Favorite' me facet
		myFavoriteFacet.click();
		VoodooUtils.waitForReady();
		
		// Verifying search result gives only one record i.e Account2 when selecting 'Created by Me' facet
		createdByMeFacet.click();
		VoodooUtils.waitForReady();
		Assert.assertTrue(globalSearchResultRows.countAll() == 1);
		sugar().globalSearch.getRow(1).assertContains(Account2, true);
		sugar().globalSearch.getRow(2).assertVisible(false);
		// Unselect 'My Favorite' me facet
		createdByMeFacet.click();
		VoodooUtils.waitForReady();

		// Verifying search result gives only one record i.e Account2 when selecting 'Modified by Me' facet
		modifiedbyMeFacet.click();
		VoodooUtils.waitForReady();
		Assert.assertTrue(globalSearchResultRows.countAll() == 1);
		sugar().globalSearch.getRow(1).assertContains(Account2, true);
		sugar().globalSearch.getRow(2).assertVisible(false);
		// Unselect 'My Favorite' me facet
		modifiedbyMeFacet.click();
		VoodooUtils.waitForReady();

		// Enter any value in the Search bar which is not exist and hit enter 
		// TODO: CB-252, VOOD-1437
		searchBar.set(testName + '\uE007');
		VoodooUtils.waitForReady();
		
		// Verifying No result is displayed
		sugar().globalSearch.getRow(1).assertVisible(false);
		
		// Verifying Facet are visible
		assignedToMeFacet.assertVisible(true);
		myFavoriteFacet.assertVisible(true);
		createdByMeFacet.assertVisible(true);
		modifiedbyMeFacet.assertVisible(true);
		
		// Verifying Facets are Disabled 
		// TODO: VOOD-1848
		for (int i = 0; i < customData.size(); i++) {
			new VoodooControl("div", "css", ".dashlet-cell li:nth-child(" + (i+1) + ") [data-voodoo-name='search-facet'].disabled").assertVisible(true);
		}
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}