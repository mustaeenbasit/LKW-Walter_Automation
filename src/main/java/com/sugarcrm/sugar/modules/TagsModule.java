package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TagRecord;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Tags module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class TagsModule extends StandardModule {
	protected static TagsModule module;

	public static TagsModule getInstance() throws Exception {
		if(module == null)
			module = new TagsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private TagsModule() throws Exception {
		moduleNameSingular = "Tag";
		moduleNamePlural = "Tags";
		recordClassName = TagRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");

		// Account Module Menu items
		menu = new Menu(this);
		menu.addControl("createTag", "a", "css", "li[data-module='Tags'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_TAG']");
		menu.addControl("viewTags", "a", "css", "li[data-module='Tags'] ul[role='menu'] a[data-navbar-menu-item='LNK_TAG_LIST']");
		menu.addControl("importTags", "a", "css", "li[data-module='Tags'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_TAGS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Tags...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("created_by_name");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		listView.addHeader("date_entered");

		// TODO: Move the common controls for each view to its View subclass.
		recordView.addControl("relAssignedUserName", "a", "css", "span.fld_assigned_user_name a");

		// Related Subpanels
		relatedModulesMany.put("calls_tags", sugar().calls);
		relatedModulesMany.put("contacts_tags", sugar().contacts);
		relatedModulesMany.put("revenuelineitems_tags", sugar().revLineItems);
		relatedModulesMany.put("opportunities_tags", sugar().opportunities);
		relatedModulesMany.put("meetings_tags", sugar().meetings);
		relatedModulesMany.put("tasks_tags", sugar().tasks);
		relatedModulesMany.put("notes_tags", sugar().notes);
		relatedModulesMany.put("accounts_tags", sugar().accounts);
		relatedModulesMany.put("leads_tags", sugar().leads);
		relatedModulesMany.put("cases_tags", sugar().cases);
		relatedModulesMany.put("bugs_tags", sugar().bugs);
		relatedModulesMany.put("prospectlists_tags", sugar().targetlists);
		relatedModulesMany.put("prospects_tags", sugar().targets);
		relatedModulesMany.put("products_tags", sugar().productCatalog);
		relatedModulesMany.put("kbdocuments_tags", sugar().knowledgeBase);
		
		// Add Subpanels
		recordView.addSubpanels();

		// Add Mass Update panel
		massUpdate = new MassUpdate(this);
	}
} // TagsModule
