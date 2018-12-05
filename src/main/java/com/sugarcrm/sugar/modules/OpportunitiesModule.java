package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Opportunities module object which contains data and element associated with the Opportunity module
 * @author mlouis
 */
public class OpportunitiesModule extends StandardModule {
	protected static OpportunitiesModule module;

	public static OpportunitiesModule getInstance() throws Exception {
		if(module == null) 
			module = new OpportunitiesModule();
		return module;
	}

	private OpportunitiesModule() throws Exception {
		moduleNameSingular = "Opportunity";
		moduleNamePlural = "Opportunities";
		bwcSubpanelName = "Opportunities";
		recordClassName = OpportunityRecord.class.getName();

		// Load fields
		loadFields();

		// Add this line back into the OpportunitiesModuleField.csv after SC-1120 is merged in Sugar7 Core (select2 lib update)
		// relAssignedUserName,a,css,.fld_assigned_user_name.detail a,a,css,.fld_assigned_user_name.edit a,assigned_user_name,Administrator

		relatedModulesOne.put("assignedUserName", "User");
		relatedModulesOne.put("accountName", "Account");
		relatedModulesOne.put("campaignName", "Campaign");
		relatedModulesOne.put("teamName", "Team");

		// Opportunities Module Menu Items
		menu = new Menu(this);
		menu.addControl("createOpportunity", "a", "css", "li[data-module='Opportunities'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_OPPORTUNITY']");
		menu.addControl("viewOpportunities", "a", "css", "li[data-module='Opportunities'] ul[role='menu'] a[data-navbar-menu-item='LNK_OPPORTUNITY_LIST']");
		menu.addControl("viewOpportunityReports", "a", "css", "li[data-module='Opportunities'] ul[role='menu'] a[data-navbar-menu-item='LNK_OPPORTUNITY_REPORTS']");
		menu.addControl("importOpportunities", "a", "css", "li[data-module='Opportunities'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_OPPORTUNITIES']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 * @throws Exception 
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Opportunities...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("sales_status");
		listView.addHeader("sales_stage");
		listView.addHeader("amount");
		listView.addHeader("opportunity_type");
		listView.addHeader("next_step");
		listView.addHeader("lead_source");
		listView.addHeader("next_step");
		listView.addHeader("date_closed");
		listView.addHeader("date_entered");
		listView.addHeader("commit_stage");
		listView.addHeader("account_name");
		listView.addHeader("created_by_name");
		listView.addHeader("assigned_user_name");
		listView.addHeader("modified_by_name");
		listView.addHeader("probability");

		// Related Subpanels
		relatedModulesMany.put("opportunity_calls", sugar().calls);
		relatedModulesMany.put("opportunity_meetings", sugar().meetings);
		relatedModulesMany.put("opportunity_tasks", sugar().tasks);
		relatedModulesMany.put("opportunity_notes", sugar().notes);
		relatedModulesMany.put("opportunities_revenuelineitems", sugar().revLineItems);
		relatedModulesMany.put("quotes_opportunities", sugar().quotes);
		relatedModulesMany.put("opportunities_products", sugar().quotedLineItems);
		relatedModulesMany.put("opportunities_contacts", sugar().contacts);
		relatedModulesMany.put("opportunity_leads", sugar().leads);
		relatedModulesMany.put("documents_opportunities", sugar().documents);
		relatedModulesMany.put("contracts_opportunities", sugar().contracts);
		relatedModulesMany.put("opportunity_emails", sugar().emails);

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("account_name");
		standardsubpanel.addHeader("contact_role");
		standardsubpanel.addHeader("sales_status");
		standardsubpanel.addHeader("date_closed");
		standardsubpanel.addHeader("amount");
		standardsubpanel.addHeader("assigned_user_name");

		// Add Subpanels
		recordView.addSubpanels();
	}

	/**
	 * Creates a single record record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	@Override
	public Record create(FieldSet testData) throws Exception {
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(testData);

		// Determine which view Sugars UI is in, Opportunity with RLI inline or Opportunity and no RLI
		boolean oppView = sugar().admin.api.isOpportunitiesView();
		// If Sugars UI is Opportunity only view, remove "rli" named fields from fieldSet
		if(oppView) {
			for (Iterator<Entry<String, String>> it = recordData.entrySet().iterator(); it.hasNext(); ) {
				Entry<String, String> entry = it.next();
				if (entry.getKey().startsWith("rli")) {
					it.remove();
					VoodooUtils.voodoo.log.info("Entry: <" + entry.getKey() + ", " + entry.getValue() + "> was removed from the fieldSet of data");
				}
			}
		} else { // If Sugars UI is RLI view, remove "date_closed" and "Case" as in "likelyCase" named fields from fieldSet
			for (Iterator<Entry<String, String>> it = recordData.entrySet().iterator(); it.hasNext(); ) {
				Entry<String, String> entry = it.next();
				if (entry.getKey().equals("date_closed") || entry.getKey().endsWith("Case")) {
					it.remove();
					VoodooUtils.voodoo.log.info("Entry: <" + entry.getKey() + ", " + entry.getValue() + "> was removed from the fieldSet of data");
				}
			}
		}

		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");
		sugar().navbar.navToModule(moduleNamePlural);

		// Move to the CreateDrawer and show hidden fields.
		listView.create();
		createDrawer.showMore();

		// Iterate over the field data and set field values.
		createDrawer.setFields(recordData);

		createDrawer.getControl("saveButton").click(); // save() would clear the alert we need.
		createDrawer.getControl("saveButton").waitForInvisible();

		// Handle alerts and scrape GUID from success message.
		VoodooControl successAlert = sugar().alerts.getSuccess();
		successAlert.waitForVisible();
		String href = new VoodooControl("a", "css", successAlert.getHookString() + " a").getAttribute("href");
		int lastSlashPos = href.lastIndexOf('/');
		String guid = href.substring(lastSlashPos + 1);
		sugar().alerts.waitForLoadingExpiration();

		// Create the record and set its GUID.
		StandardRecord toReturn = (StandardRecord)Class.forName(this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}

} // OpportunitiesModule