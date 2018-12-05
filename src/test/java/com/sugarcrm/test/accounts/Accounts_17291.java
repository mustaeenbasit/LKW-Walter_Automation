package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_17291 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify relationships dropdown
	 *  
	 * @throws Exception
	 */
	@Test
	public void Accounts_17291_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// array list of modules
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().calls);
		modules.add(sugar().meetings);
		modules.add(sugar().tasks);
		modules.add(sugar().notes);
		modules.add(sugar().emails);
		modules.add(sugar().contacts);
		modules.add(sugar().opportunities);
		modules.add(sugar().leads);
		modules.add(sugar().cases);
		modules.add(sugar().revLineItems);
		modules.add(sugar().documents);
		modules.add(sugar().quotes);
		// TODO: VOOD-1344 - Once resolved campaign log module should be added in modules arraylist

		// array list of filter-names
		ArrayList<String> filterName = new ArrayList<String>();
		filterName.add("All");
		filterName.add(sugar().contacts.moduleNamePlural);
		filterName.add(sugar().opportunities.moduleNamePlural);
		filterName.add(sugar().leads.moduleNamePlural);
		ArrayList<StandardSubpanel> subPanelList = new ArrayList<StandardSubpanel>();

		// array list of subpanels
		for (int j = 0; j < modules.size(); j++) {
			String moduleNamePlural = modules.get(j).moduleNamePlural;
			StandardSubpanel subPanel = sugar().accounts.recordView.subpanels.get(moduleNamePlural);
			subPanelList.add(subPanel);
		}

		// Verifying subpanels visibility according to filterName
		for (int i = 0; i < filterName.size(); i++) {
			sugar().accounts.recordView.setRelatedSubpanelFilter(filterName.get(i));
			switch (filterName.get(i)) {
			case "All":
				for(StandardSubpanel sp:subPanelList){
					sp.scrollIntoViewIfNeeded(false);
					sp.assertVisible(true);
				}
				break;
			case "Contacts":
				for(StandardSubpanel sp:subPanelList){
					String subpanelName = sp.getControl("subpanelName").getText();
					if(subpanelName.equals(sugar().contacts.moduleNamePlural))
						sp.assertVisible(true);
					else
						sp.assertVisible(false);
				}
				break;	
			case "Opportunities":
				for(StandardSubpanel sp:subPanelList){
					String subpanelName = sp.getControl("subpanelName").getText();
					if(subpanelName.equals(sugar().opportunities.moduleNamePlural))
						sp.assertVisible(true);
					else
						sp.assertVisible(false);
				}
				break;
			case "Leads":
				for(StandardSubpanel sp:subPanelList){
					String subpanelName = sp.getControl("subpanelName").getText();
					if(subpanelName.equals(sugar().leads.moduleNamePlural))
						sp.assertVisible(true);
					else
						sp.assertVisible(false);
				}
				break;
			case "Quotes":
				for(StandardSubpanel sp:subPanelList){
					String subpanelName = sp.getControl("subpanelName").getText();
					if(subpanelName.equals(sugar().quotes.moduleNamePlural))
						sp.assertVisible(true);
					else
						sp.assertVisible(false);
				}
				break;
			}
		}

		// Revert back the changes of Subpanel Related All selector
		sugar().accounts.recordView.setRelatedSubpanelFilter("All");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}