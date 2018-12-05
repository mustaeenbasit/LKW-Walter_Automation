package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.InboundEmailRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.Menu;

/**
 * Inbound Email module object which contains tasks associated with the Inbound Email module like
 * create
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class InboundEmailModule extends BWCModule {
	protected static InboundEmailModule module;

	public static InboundEmailModule getInstance() throws Exception {
		if (module == null)
			module = new InboundEmailModule();
		return module;
	}

	private InboundEmailModule() throws Exception {
		moduleNameSingular = "InboundEmail";
		moduleNamePlural = "InboundEmail";
		recordClassName = InboundEmailModule.class.getName();

		// Load fields from InboundEmailModule.java
		loadFields();
		
		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("type");
		listView.addHeader("mail_account_usage");
		listView.addHeader("mail_server");
		listView.addHeader("status");
		
		// Override link Column and regenerate affected part of listView
		listView.setLinkColumn(5);

		// InboundEmail Module Menu Items
		menu = new Menu(this);
		menu.addControl("newGroupMailAccount", "a", "css", "li[data-module='" + moduleNameSingular + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_CREATE_NEW_GROUP']");
		menu.addControl("newBounceMailAccount", "a", "css", "li[data-module='" + moduleNameSingular + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_CREATE_NEW_BOUNCE']");
		menu.addControl("allMailAccounts", "a", "css", "li[data-module='" + moduleNameSingular + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_MAILBOXES']");
		menu.addControl("schedulers", "a", "css", "li[data-module='" + moduleNameSingular + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_SCHEDULER']");

		editView.addControl("save", "input", "id", "button");
		editView.addControl("cancel", "input", "id", "emailCancel");
		editView.addControl("testSettings", "input", "id", "emailTestSettings");
		editView.addControl("prefillGmail", "a", "id", "prefill_gmail_defaults_link");
		
		listView.addControl("saveMacro", "input", "css", "input[name='Edit']");
		listView.addControl("macroInput", "input", "css", "input[name='inbound_email_case_macro']");
		// TODO: Update defs once TR-2566 is fixed
		listView.addControl("selectAllCheckbox", "input", "id", "massall");
		listView.addControl("deleteButton", "input", "id", "delete_button");
		
		// TODO: Update defs once TR-2567 is fixed
		detailView.addControl("editButton", "input", "id", "emailEdit");
		detailView.addControl("copyButton", "input", "id", "emailDuplicate");
		detailView.addControl("deleteButton", "input", "id", "Delete");
	}
	
	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Inbound Email module...");
	}
	
	/**
	 * Creates a single record record via the UI from the data in a FieldSet.
	 * 
	 * @param testData	A FieldSet of data passed from the test for the new record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	@Override
	public Record create(FieldSet testData) throws Exception {
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(testData);

		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");
		navToListView();
		sugar().navbar.selectMenuItem(this, "newGroupMailAccount");
		VoodooUtils.pause(2000);
		VoodooUtils.focusFrame("bwc-frame");

		// Iterate over the field data and set field values.
		for(String controlName : recordData.keySet()) {
			if(recordData.get(controlName) != null) {
				String toSet = recordData.get(controlName);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " to "
						+ toSet);
				editView.getEditField(controlName).set(toSet);
				VoodooUtils.pause(300);
			} else {
				throw new Exception("Tried to set field " + controlName + " to a" +
						" null value!");
			}
		}

		editView.getControl("save").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
		VoodooUtils.pause(2000);
		
		VoodooUtils.voodoo.log.fine("Record created.");

		return new InboundEmailRecord(recordData);
	}
	
	/**
	 * Navigate to InboundEmail Module listview.
	 * <p>
	 * Leaves you on Inbound Email module listview with focus on the sidecar content.<br>
	 */
	@Override
	public void navToListView() throws Exception {
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("inboundEmail").waitForVisible(30000);
		sugar().admin.adminTools.getControl("inboundEmail").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		listView.getControl("saveMacro").waitForVisible(30000);
		VoodooUtils.focusDefault();
	}
}// end InboundEmail