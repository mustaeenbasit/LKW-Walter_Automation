package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Documents module, such as field
 * data.
 * 
 * @author Ian Fleming <ifleming@sugarcrm.com> 
 */
public class DocumentsModule extends BWCModule {
	protected static DocumentsModule module;
	
	public static DocumentsModule getInstance() throws Exception {
		if(module == null) 
			module = new DocumentsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private DocumentsModule() throws Exception {
		moduleNameSingular = "Document";
		moduleNamePlural = "Documents";
		bwcSubpanelName = "Documents";
		recordClassName = DocumentRecord.class.getName();
		
		// Load field defs from CSV
		loadFields();

		// Define the columns on the ListView.
		listView.addHeader("document");
		listView.addHeader("status");
		listView.addHeader("type");
		listView.addHeader("date_end");
		listView.addHeader("user");
		listView.addHeader("date_created");

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("teamName", "Teams");
	
		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		
		// Documents Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createDocument", "a", "css", "[data-navbar-menu-item='LNK_NEW_DOCUMENT']");
		menu.addControl("viewDocuments", "a", "css", "[data-navbar-menu-item='LNK_DOCUMENT_LIST']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Documents...");
		super.init();
		
		// Related Subpanels
		relatedModulesMany.put("documents_accounts", sugar().accounts);
		relatedModulesMany.put("documents_contacts", sugar().contacts);
		relatedModulesMany.put("documents_opportunities", sugar().opportunities);
		relatedModulesMany.put("documents_cases", sugar().cases);
		relatedModulesMany.put("documents_bugs", sugar().bugs);
		relatedModulesMany.put("documents_quotes", sugar().quotes);
		relatedModulesMany.put("documents_revenuelineitems", sugar().revLineItems);
		
		// Add Subpanels
		detailView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("document_name");
		standardsubpanel.addHeader("filename");
		standardsubpanel.addHeader("category_id");
		standardsubpanel.addHeader("doc_type");
		standardsubpanel.addHeader("status_id");
		standardsubpanel.addHeader("active_date");
		
		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);

		// Override Basic Search panel
		listView.addControl("nameBasic", "input", "id", "document_name_basic");
	}
} // DocumentsModule
