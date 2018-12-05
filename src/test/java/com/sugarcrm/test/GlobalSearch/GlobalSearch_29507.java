package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_29507 extends SugarTest {
	public void setup() throws Exception {
		// Account record exists
		sugar().accounts.api.create();

		// Create a RLI record
		sugar().revLineItems.api.create();

		// Login as an admin User
		sugar().login();

		// Opportunity record exists linked to the account and it also creates a RLI record related to Opportunity
		// TODO: VOOD-444
		FieldSet rliName = new FieldSet();
		rliName.put("rli_name", testName);
		sugar().opportunities.create(rliName);
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify OOTB Default fields that display for RLI Search Result
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29507_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet rliData = testData.get(testName).get(0);

		// Type a first few characters of RLI record name in Global Search and hit enter
		// TODO: VOOD-1849, CB-252
		sugar().navbar.getControl("globalSearch").set(testName.substring(0, 15) + '\uE007');

		// Verify that Primary: RLI icon, RLI name and Secondary: Expected Close Date are displaying
		// TODO: VOOD-1868
		VoodooControl firstSearchedResultCtrl = new VoodooControl("li", "css", "ul.nav.search-results li.search-result:nth-of-type(1)");
		VoodooControl rliModuleIconCtrl = new VoodooControl("span", "css", firstSearchedResultCtrl.getHookString() + " .primary .label-module");
		rliModuleIconCtrl.assertEquals(rliData.get("rliModuleLabel"), true);
		rliModuleIconCtrl.assertCssAttribute("background-color", rliData.get("rliModuleLabelColor"), true);
		// TODO: VOOD-1951
		// Also verifying that the searched string is bold
		new VoodooControl("strong", "css", firstSearchedResultCtrl.getHookString() + " .primary a strong").assertEquals(testName, true);
		new VoodooControl("span", "css", firstSearchedResultCtrl.getHookString() + " .secondary span").assertEquals(rliData.get("expectedCloseDate"), true);
		new VoodooControl("span", "css", firstSearchedResultCtrl.getHookString() + " .secondary .fld_date_closed").assertEquals(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"), true);

		// Hit Preview for the found record
		sugar().globalSearch.preview(1);

		// Preview is available and displays correct information about the found record
		sugar().previewPane.setModule(sugar().revLineItems);
		sugar().previewPane.getPreviewPaneField("name").assertEquals(testName, true);
		sugar().previewPane.getPreviewPaneField("relOpportunityName").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		sugar().previewPane.getPreviewPaneField("salesStage").assertEquals(sugar().opportunities.getDefaultData().get("rli_stage"), true);
		sugar().previewPane.getPreviewPaneField("date_closed").assertEquals(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}