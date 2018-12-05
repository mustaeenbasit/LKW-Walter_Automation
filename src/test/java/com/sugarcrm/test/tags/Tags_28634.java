package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28634 extends SugarTest {
	FieldSet tagsRecord = new FieldSet();
	
	public void setup() throws Exception {
		// Create 2 Tags records
		for (int i = 0; i < 2; i++) {
			tagsRecord.put("name", testName+"_"+i);
			sugar.tags.api.create(tagsRecord);
		}
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * User can Favorite tags in List View and Record View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28634_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Tags Module
		sugar.tags.navToListView();
		
		// In List View - click the star to favorite a Tag record
		sugar.tags.listView.getControl("favoriteStar01").click();
		
		// Verify, Tag record is favorited by the user
		sugar.tags.listView.getControl("favoriteStar01").assertAttribute("class", "active", true);
		
		// Click to see Record View of the Tag record favorited
		sugar.tags.listView.clickRecord(1);
		sugar.tags.recordView.getControl("favoriteButton").assertAttribute("class", "active", true);
		
		// Go to a different Tag's Record View
		sugar.tags.navToListView();
		sugar.tags.listView.clickRecord(2);
		
		// Click the star to favorite the record
		sugar.tags.recordView.getControl("favoriteButton").click();
		
		// Verify, Tag record is favorited by the user
		sugar.tags.recordView.getControl("favoriteButton").assertAttribute("class", "active", true);
		
		// Go to Tags List View and verify that Tag record is favorited by the user
		sugar.tags.navToListView();
		sugar.tags.listView.getControl("favoriteStar02").assertAttribute("class", "active", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}