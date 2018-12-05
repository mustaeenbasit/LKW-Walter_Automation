package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contact module object which contains tasks associated with the Contact module
 * like create/deleteAll
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ContactsModule extends StandardModule {
	protected static ContactsModule module;

	public static ContactsModule getInstance() throws Exception {
		if(module == null) module = new ContactsModule();
		return module;
	}

	private ContactsModule() throws Exception {
		moduleNameSingular = "Contact";
		moduleNamePlural = "Contacts";
		bwcSubpanelName = "Contacts";
		recordClassName = ContactRecord.class.getName();

		loadFields();

		// Contacts Module Menu Items
		menu = new Menu(this);
		menu.addControl("createContact", "a", "css", "li[data-module='Contacts'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_CONTACT']");
		menu.addControl("createContactFromVcard", "a", "css", "li[data-module='Contacts'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_VCARD']");
		menu.addControl("viewContacts", "a", "css", "li[data-module='Contacts'] ul[role='menu'] a[data-navbar-menu-item='LNK_CONTACT_LIST']");
		menu.addControl("viewContactReports", "a", "css", "li[data-module='Contacts'] ul[role='menu'] a[data-navbar-menu-item='LNK_CONTACT_REPORTS']");
		menu.addControl("importContacts", "a", "css", "li[data-module='Contacts'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_CONTACTS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Contacts...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("full_name");
		listView.addHeader("title");
		listView.addHeader("phone_work");
		listView.addHeader("date_entered");
		listView.addHeader("account_name");
		listView.addHeader("email");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		
		// Related Subpanels
		relatedModulesMany.put("calls_contacts", sugar().calls);
		relatedModulesMany.put("opportunities_contacts", sugar().opportunities);
		relatedModulesMany.put("meetings_contacts", sugar().meetings);
		relatedModulesMany.put("contact_tasks", sugar().tasks);
		relatedModulesMany.put("contact_notes", sugar().notes);
		relatedModulesMany.put("contact_leads", sugar().leads);
		relatedModulesMany.put("contacts_cases", sugar().cases);
		relatedModulesMany.put("documents_contacts", sugar().documents);
		relatedModulesMany.put("contacts_bugs", sugar().bugs);
		relatedModulesMany.put("contracts_contacts", sugar().contracts);
		relatedModulesMany.put("accounts_contacts", sugar().accounts);
		relatedModulesMany.put("contacts_users", sugar().users);
		relatedModulesMany.put("contact_direct_reports", sugar().contacts);
		relatedModulesMany.put("contact_emails", sugar().emails);
		relatedModulesMany.put("quotes_contacts_billto", sugar().quotes);
		relatedModulesMany.put("quotes_contacts_shipto", sugar().quotes);

		// Add Subpanels
		recordView.addSubpanels();
		// Adding custom subpanels
		recordView.addCustomSubpanel(sugar().quotes, "Quotes (Bill To)", "quotesBillToContacts");
		recordView.addCustomSubpanel(sugar().quotes, "Quotes (Ship To)", "quotesShipToContacts");

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("full_name");
		standardsubpanel.addHeader("primary_address_city");
		standardsubpanel.addHeader("primary_address_state");
		standardsubpanel.addHeader("email");
		standardsubpanel.addHeader("phone_work");
		standardsubpanel.addHeader("account_name");

		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
}//ContactsModule