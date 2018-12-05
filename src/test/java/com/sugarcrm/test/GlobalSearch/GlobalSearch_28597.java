package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28597 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Knowledge Base module is displayed once in Global Search Quicksearch module
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28597_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		
		// Click on Global Search textbox
		sugar().navbar.getControl("globalSearch").click();
		
		// Verify that Global Search textbox is expanded
		// TODO: VOOD-1849
		new VoodooControl("div", "css", ".search.expanded").assertVisible(true);
		
		// Click on 'ALL' in Global Search textbox
		// TODO: VOOD-1849
		VoodooControl allModule = new VoodooControl("span", "css", ".non-module-label");
		VoodooControl moduleDropdown = new VoodooControl("ul", "css", ".module-dropdown-list");
		allModule.click();
		moduleDropdown.assertContains(customFS.get("knowledgeBase"), true);
		
		// Verify Knowledge Base module is displayed once
		Assert.assertTrue("Knowledge Base module displayed more than once", new VoodooControl("span", "xpath", "//*[@id='header']/div/div/div/div[2]/div[1]/div[1]/ul/li/*[text()[contains(.,'" + customFS.get("knowledgeBase") + "')]]").count() == 1);;
		
		// Verify modules like KBArticles, KBContents, and KBDocuments are not displayed
		// TODO: VOOD-1849
		moduleDropdown.assertContains(customFS.get("KBArticles"), false);
		moduleDropdown.assertContains(customFS.get("KBContents"), false);
		moduleDropdown.assertContains(customFS.get("KBDocuments"), false);
		
		// Close dropdown
		allModule.click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}