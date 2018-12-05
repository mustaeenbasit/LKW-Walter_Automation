package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.sugar.views.*;

/**
 * Contains data and tasks associated with the Knowledge Base module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class KBModule extends StandardModule {
	protected static KBModule module;

	public static KBModule getInstance() throws Exception {
		if(module == null)
			module = new KBModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private KBModule() throws Exception {
		moduleNameSingular = "KBContent";
		moduleNamePlural = "KBContents";
		recordClassName = KBRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		relatedModulesOne.put("case", "Cases");
		relatedModulesOne.put("teamName", "Teams");

		// KB Module Menu items
		menu = new Menu(this);
		menu.addControl("createArticle", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_ARTICLE']");
		menu.addControl("createTemplate", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_KBCONTENT_TEMPLATE']");
		menu.addControl("viewArticles", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_ARTICLES']");
		menu.addControl("viewTemplates", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_KBCONTENT_TEMPLATES']");
		menu.addControl("viewCategories", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST_KBCATEGORIES']");
		menu.addControl("configure", "a", "css", "li[data-module='KBContents'] ul[role='menu'] a[data-navbar-menu-item='LNK_KNOWLEDGE_BASE_ADMIN_MENU']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));

		// Special KBCreateDrawer
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init KBModule...");
		super.init();

		// Special KBCreateDrawer for custom handling
		createDrawer = new KBCreateDrawer(this);

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("language"); // with no sort icon
		listView.addHeader("revision");
		listView.addHeader("status");
		listView.addHeader("category_name");
		listView.addHeader("viewcount");
		listView.addHeader("date_entered");
		listView.addHeader("kbsapprover_name");
		listView.addHeader("assigned_user_name");
		
		// Related Subpanels
		// TODO: VOOD-1760
		// There exists two more "subpanels" Revisions and Localizations. These are not modules, but are shown as subpanels on the recordview.		
		relatedModulesMany.put("kbcontent_notes", sugar().notes);

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("language");
		standardsubpanel.addHeader("revision");
		standardsubpanel.addHeader("active_rev");
		standardsubpanel.addHeader("date_entered");
		standardsubpanel.addHeader("date_modified");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("assigned_user_name");

		// KBModule Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // KBModule
