package com.sugarcrm.test.subpanels;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_29746 extends SugarTest {
	public void setup() throws Exception {
		sugar().targetlists.api.create();
		sugar().login();
	}

	/**
	 * Verify that Chevron icon is not showing in Sidecar modules->Subpanels
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29746_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to TargetList view
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// array list of modules
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().targets);
		modules.add(sugar().contacts);
		modules.add(sugar().leads);
		modules.add(sugar().users);
		modules.add(sugar().accounts);

		// TODO: VOOD-1344 and VOOD-1499 - Once resolved below commented line should work for Campaign Log subpanel
		//modules.add(sugar().campaigns);

		// Verify no chevron icon on subpanels
		for (int j = 0; j < modules.size(); j++) {
			String moduleNamePlural = modules.get(j).moduleNamePlural;
			StandardSubpanel subPanel = sugar().targetlists.recordView.subpanels.get(moduleNamePlural);
			verifyChevronIcon(subPanel);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	private void verifyChevronIcon(StandardSubpanel subpanel) throws Exception {
		FieldSet customData = testData.get(testName).get(0);

		// Verify no chevron icon on subpanels
		// TODO: VOOD-1843 Once resolved below code should use getChildElement
		VoodooControl panelTopForSubpanel = new VoodooControl("span", "css", subpanel.getHookString()+" .subpanel-controls .panel-top");
		new VoodooControl("a", "css", panelTopForSubpanel.getHookString()+" a").assertAttribute("data-original-title", customData.get("data_original_title"), false);
		new VoodooControl("a", "css", panelTopForSubpanel.getHookString()+" a:nth-child(2)").assertAttribute("data-original-title", customData.get("data_original_title"), false);
		new VoodooControl("a", "css", panelTopForSubpanel.getHookString()+" a:nth-child(3)").assertVisible(false);
	}

	public void cleanup() throws Exception {}
}