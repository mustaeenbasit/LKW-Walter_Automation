package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_22959 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Enable Contracts subpanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);

		// TODO: VOOD-444 - Create dependency of an account with quote record, once resolved it should create via API
		sugar().quotes.create();
	}

	/**
	 * Quotes Detail - Contracts sub-panel - Create_Verify that a document is created in "Full Form" format from "CONTRACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_22959_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		BWCSubpanel contractSubpanel = sugar().quotes.detailView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractSubpanel.expandSubpanelActionsMenu();
		contractSubpanel.subpanelAction("#contracts_quotes_create_button");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		new VoodooControl("input", "id", "Contracts_subpanel_full_form_button").click();

		FieldSet contractFS = new FieldSet();
		contractFS.put("name", testName);	
		sugar().contracts.editView.getEditField("name").set(contractFS.get("name"));
		VoodooUtils.focusDefault();
		sugar().contracts.editView.save();

		// Verify contract is displayed on "CONTRACTS" sub-panel
		// TODO: VOOD-1708
		contractSubpanel.verify(1, contractFS, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}