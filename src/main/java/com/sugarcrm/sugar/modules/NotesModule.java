package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Notes module, such as field data.
 * 
 * @author Jessica Cho
 */
public class NotesModule extends StandardModule {
	protected static NotesModule module;

	public static NotesModule getInstance() throws Exception {
		if (module == null) module = new NotesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private NotesModule() throws Exception {
		moduleNameSingular = "Note";
		moduleNamePlural = "Notes";
		bwcSubpanelName = "History";
		recordClassName = NoteRecord.class.getName();
		api = new Api();

		//Load Tasks Module element definitions from CSV
		loadFields();

		// Related to fields
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("contactName", "Contacts");
		relatedModulesOne.put("teamName", "Teams");

		// Notes Module Menu items
		menu = new Menu(this);
		menu.addControl("createNote", "a", "css", "li[data-module='Notes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_NOTE']");
		menu.addControl("viewNotes", "a", "css", "li[data-module='Notes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NOTE_LIST']");
		menu.addControl("importNotes", "a", "css", "li[data-module='Notes'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_NOTES']");

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
		VoodooUtils.voodoo.log.info("Init Notes...");
		super.init();

		// Define the columns on the ListView. This name is a reference of the data-fieldname attribute of the element.
		listView.addHeader("name");
		listView.addHeader("contact_name");
		listView.addHeader("parent_name");
		listView.addHeader("filename");
		listView.addHeader("created_by_name");	
		listView.addHeader("date_modified");
		listView.addHeader("date_entered");
		
		// TODO: There is no subpanel in notes module right now.  
		// Other modules have notes subpanel (7.1.5 or above)

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("date_modified");
		standardsubpanel.addHeader("date_entered");
		standardsubpanel.addHeader("assigned_user_name");

		// Add Subpanels
		recordView.addSubpanels();

		// Notes Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // end NotesModule
