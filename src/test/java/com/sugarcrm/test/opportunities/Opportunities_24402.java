package com.sugarcrm.test.opportunities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_24402 extends SugarTest {
	DataSource customData = new DataSource();
	FieldSet systemSettings = new FieldSet();
	StandardSubpanel quoteSubPanel, documentSubPanel;
	HashMap<String, StandardSubpanel> subpanels = new HashMap<String, StandardSubpanel>();

	public void setup() throws Exception {
		customData = testData.get(testName);

		// Make a list of modules
		ArrayList<StandardModule> allModules = new ArrayList<>();
		allModules.add(sugar().calls);
		allModules.add(sugar().meetings);
		allModules.add(sugar().tasks);
		allModules.add(sugar().notes);
		allModules.add(sugar().contacts);
		allModules.add(sugar().leads);

		// create records
		HashMap<String, ArrayList<Record>> allRecords = createRecords(allModules, customData);

		// preparing dataSource for document record
		FieldSet firstSet = new FieldSet();
		FieldSet secondSet = new FieldSet();
		DataSource dataSource = new DataSource();
		firstSet.put("documentName", customData.get(0).get("lastName"));
		secondSet.put("documentName", customData.get(1).get("lastName"));
		dataSource.add(firstSet);
		dataSource.add(secondSet);

		// create documents records
		ArrayList<Record> documentRecords = sugar().documents.api.create(dataSource);

		// clear the field set
		firstSet.clear();
		secondSet.clear();
		dataSource.clear();

		// preparing dataSource for quotes record
		firstSet.put("name", customData.get(0).get("lastName"));
		secondSet.put("name", customData.get(1).get("lastName"));
		dataSource.add(firstSet);
		dataSource.add(secondSet);

		// create quotes record
		ArrayList<Record> quoteRecords = sugar().quotes.api.create(dataSource);

		// create opportunities record
		sugar().opportunities.api.create();

		// Login as Admin
		sugar().login();

		// Navigate to Admin > System Settings and changing the Sub-panel limit to 1.
		systemSettings.put("maxEntriesPerSubPanel", customData.get(0).get("maxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(systemSettings);

		// Go to opportunity recordView to link records to subpanels
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// bwc subpanels
		quoteSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		documentSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().documents.moduleNamePlural);

		// method call to link records to subpanels
		subpanels = linkRecordsTosubPanel(allModules, allRecords);
		// link documentRecords to subpanel
		documentSubPanel.linkExistingRecords(documentRecords);
		// link quoteRecords to subpanel
		quoteSubPanel.linkExistingRecords(quoteRecords);

		// Add bwc subpanle in the list
		subpanels.put(sugar().documents.moduleNamePlural, documentSubPanel);
		subpanels.put(sugar().quotes.moduleNamePlural, quoteSubPanel);
	}

	/**
	 * Detail View Opportunity_Verify that the list in the sub-panel MoreProjects...; of "Opportunity" detail view can be paginated.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24402_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the record
		verifySubPanelRecord(subpanels);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {
	}

	/**
	 * method to create records for different modules through api. i.e api.create
	 *
	 * @param modules    list of the modules as ArrayList of modules
	 * @param customData list of fieldset record as DataSource
	 * @return result as Map<String, ArrayList<Record>
	 **/
	private HashMap<String, ArrayList<Record>> createRecords(ArrayList<StandardModule> modules, DataSource customData) throws Exception {
		DataSource ds = new DataSource();
		FieldSet firstFieldSet = new FieldSet();
		FieldSet secondFieldSet = new FieldSet();
		HashMap<String, ArrayList<Record>> allRecords = new HashMap<String, ArrayList<Record>>();
		for (StandardModule module : modules) {
			ds.clear();
			firstFieldSet.clear();
			secondFieldSet.clear();
			switch (module.moduleNamePlural) {
				case "Calls":
				case "Meetings":
					firstFieldSet.put("name", customData.get(0).get("lastName"));
					secondFieldSet.put("name", customData.get(1).get("lastName"));
					break;
				case "Tasks":
				case "Notes":
					firstFieldSet.put("subject", customData.get(0).get("lastName"));
					secondFieldSet.put("subject", customData.get(1).get("lastName"));
					break;
				case "Contacts":
				case "Leads":
					firstFieldSet.put("firstName", customData.get(0).get("firstName"));
					firstFieldSet.put("lastName", customData.get(0).get("lastName"));
					secondFieldSet.put("firstName", customData.get(1).get("firstName"));
					secondFieldSet.put("lastName", customData.get(1).get("lastName"));
					break;
			}
			ds.add(firstFieldSet);
			ds.add(secondFieldSet);
			allRecords.put(module.moduleNamePlural, module.api.create(ds));
		}
		return allRecords;
	}

	/**
	 * method to link different records to different SubPanels
	 *
	 * @param modules    list of the modules as ArrayList of modules
	 * @param allRecords map of records as HashMap< String, ArrayList<Record>>
	 * @return result as HashMap<String, StandardSubpanel>
	 **/
	private HashMap<String, StandardSubpanel> linkRecordsTosubPanel(ArrayList<StandardModule> modules, HashMap<String, ArrayList<Record>> allRecords) throws Exception {
		HashMap<String, StandardSubpanel> allSubPanel = new HashMap<String, StandardSubpanel>();
		for (StandardModule module : modules) {
			StandardSubpanel recordSubpanel = sugar().opportunities.recordView.subpanels.get(module.moduleNamePlural);
			recordSubpanel.linkExistingRecords(allRecords.get(module.moduleNamePlural));
			allSubPanel.put(module.moduleNamePlural, recordSubpanel);
		}
		return allSubPanel;
	}

	/**
	 * method to verify records added to different SubPanels
	 *
	 * @param allSubPanel map of subpanels as HashMap<String, StandardSubpanel>
	 **/
	private void verifySubPanelRecord(HashMap<String, StandardSubpanel> allSubPanel) throws Exception {
		// Go to opportunity recordView to verify record inside subpanels
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		FieldSet firstRecord = new FieldSet();
		FieldSet secondRecord = new FieldSet();
		Iterator<Entry<String, StandardSubpanel>> it = allSubPanel.entrySet().iterator();
		while (it.hasNext()) {
			firstRecord.clear();
			secondRecord.clear();
			Entry<String, StandardSubpanel> pair = (Entry<String, StandardSubpanel>) it.next();
			VoodooControl nameLabelCtrl;

			// condition to set subpanel header css hook
			// TODO: VOOD-2105
			if (pair.getKey().equals(sugar().contacts.moduleNamePlural) || pair.getKey().equals(sugar().leads.moduleNamePlural)) {
				nameLabelCtrl = new VoodooControl("span", "css", "[data-voodoo-name='" + pair.getKey() + "'] .sorting.orderByfull_name");
			} else if (pair.getKey().equals(sugar().documents.moduleNamePlural)) {
				nameLabelCtrl = new VoodooControl("span", "css", "[data-voodoo-name='" + pair.getKey() + "'] .sorting.orderBydocument_name");
			} else {
				nameLabelCtrl = new VoodooControl("span", "css", "[data-voodoo-name='" + pair.getKey() + "'] .sorting.orderByname");
			}
			switch (pair.getKey()) {
				case "Calls":
				case "Meetings":
				case "Quotes":
					firstRecord.put("name", customData.get(0).get("lastName"));
					secondRecord.put("name", customData.get(1).get("lastName"));
					break;
				case "Tasks":
				case "Notes":
					firstRecord.put("subject", customData.get(0).get("lastName"));
					secondRecord.put("subject", customData.get(1).get("lastName"));
					break;
				case "Contacts":
				case "Leads":
					firstRecord.put("fullName", "Mr. " + customData.get(0).get("firstName") + " " + customData.get(0).get("lastName"));
					secondRecord.put("fullName", "Mr. " + customData.get(1).get("firstName") + " " + customData.get(1).get("lastName"));
					break;
				case "Documents":
					firstRecord.put("documentName", customData.get(0).get("lastName"));
					secondRecord.put("documentName", customData.get(1).get("lastName"));
					break;
			}
			// click on name header label
			nameLabelCtrl.click();
			VoodooUtils.waitForReady();
			// click show more
			pair.getValue().showMore();
			VoodooUtils.waitForReady();
			// verify records
			// TODO: VOOD-1424
			pair.getValue().verify(1, secondRecord, true);
			pair.getValue().verify(2, firstRecord, true);
			it.remove(); // avoids a ConcurrentModificationException
		}
	}
}