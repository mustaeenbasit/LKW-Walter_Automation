package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28598 extends SugarTest {
	public void setup() throws Exception {
		// 1 Account & 1 campaign is created via API
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().campaigns.api.create(fs);

		sugar().login();
	}

	/**
	 * Verify the preview functionality on global search results page. 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28598_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();

		// TODO: VOOD-1849, CB-252
		// search and hit enter
		globalSearch.set(testName+'\uE007');
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1848
		// Verify Preview button is enabled for Sidecar module
		Assert.assertFalse("Preview button is disabled for Sidecar module records.", new VoodooControl("a", "css", ".layout_default .search-result .search-list a").isDisabled());

		// Verify Preview detail record
		sugar().globalSearch.preview(1);
		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.getPreviewPaneField("name").assertEquals(testName, true);
		sugar().previewPane.getPreviewPaneField("website").assertEquals(sugar().accounts.getDefaultData().get("website"), true);
		sugar().previewPane.getPreviewPaneField("type").assertEquals(sugar().accounts.getDefaultData().get("type"), true);
		sugar().previewPane.getPreviewPaneField("industry").assertEquals(sugar().accounts.getDefaultData().get("industry"), true);
		sugar().previewPane.getPreviewPaneField("workPhone").assertEquals(sugar().accounts.getDefaultData().get("workPhone"), true);

		// Verify Preview button is disabled for BWC module
		VoodooControl previewBtnForBWCRecord = new VoodooControl("a", "css", ".layout_default .search-result:nth-of-type(2) .search-list a");
		Assert.assertTrue("Preview button is enabled for BWC module records.", previewBtnForBWCRecord.isDisabled());

		// "Legacy modules cannot be previewed" tooltip should be displayed when hover over it. 
		previewBtnForBWCRecord.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(testData.get(testName).get(0).get("bwc_preview_text"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}