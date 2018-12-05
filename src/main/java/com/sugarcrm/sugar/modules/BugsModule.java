package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * Contains data and tasks associated with the Bugs module, such as field data.
 * 
 * @author Ian Fleming <ifleming@sugarcrm.com>
 */
public class BugsModule extends StandardModule {
	protected static BugsModule module;

	public static BugsModule getInstance() throws Exception {
		if (module == null)
			module = new BugsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * 
	 * @throws Exception
	 */
	private BugsModule() throws Exception {
		moduleNameSingular = "Bug";
		moduleNamePlural = "Bugs";
		bwcSubpanelName = "Bugs";
		recordClassName = BugRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		// TODO - Need to add releases relationships when the Releases module is
		// Automated
		relatedModulesOne.put("assignedUserName", "User");
		relatedModulesOne.put("teamName", "Teams");

		// Bugs Module Menu items
		// TODO: When JIRA story VOOD-451 is un-blocked by SFA-1287, please
		// update element def's
		menu = new Menu(this);
		menu.addControl("createBug", "a", "css", "li[data-module='Bugs'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_BUG']");
		menu.addControl("viewBugs", "a", "css", "li[data-module='Bugs'] ul[role='menu'] a[data-navbar-menu-item='LNK_BUG_LIST']");
		menu.addControl("viewBugReports", "a", "css", "li[data-module='Bugs'] ul[role='menu'] a[data-navbar-menu-item='LNK_BUG_REPORTS']");
		menu.addControl("importBugs", "a", "css", "li[data-module='Bugs'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_BUGS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Bugs...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("bug_number");
		listView.addHeader("name");
		listView.addHeader("status");
		listView.addHeader("type");
		listView.addHeader("priority");
		listView.addHeader("fixed_in_release_name");
		listView.addHeader("assigned_user_name");

		// Related Subpanels
		relatedModulesMany.put("contacts_bugs", sugar().contacts);
		relatedModulesMany.put("accounts_bugs", sugar().accounts);
		relatedModulesMany.put("cases_bugs", sugar().cases);
		relatedModulesMany.put("documents_bugs", sugar().documents);
		relatedModulesMany.put("bug_calls", sugar().calls);
		relatedModulesMany.put("bug_meetings", sugar().meetings);
		relatedModulesMany.put("bug_notes", sugar().notes);
		relatedModulesMany.put("bug_tasks", sugar().tasks);
		relatedModulesMany.put("bug_emails", sugar().emails);

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("bug_number");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("type");
		standardsubpanel.addHeader("priority");
		standardsubpanel.addHeader("assigned_user_name");

		// Bugs Mass Update Panel
		massUpdate = new MassUpdate(this);
	}

	public static void createReleases(DataSource releases) throws Exception {
		// TODO - This will be removed when a common admin function exists for
		// Creating Releases

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("a", "id", "bug_tracker").click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		for (FieldSet release : releases) {
			new VoodooControl("input", "xpath", "//input[@value='  Create  ']").click();
			VoodooUtils.pause(2000);
			new VoodooControl("input", "name", "name").set(release.get("name"));

			new VoodooControl("input", "xpath", "//input[@value='  Save  ']").click();
			VoodooUtils.pause(2000);
		}
		VoodooUtils.focusDefault();
	}

	public static void deleteReleases(DataSource releases) throws Exception {
		// TODO - This will be removed when a common admin function exists for
		// Deleting Releases

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("a", "id", "bug_tracker").click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusFrame("bwc-frame");
		for (FieldSet release : releases) {
			// Iterate thru the initial data file used for creation to click the
			// delete button the correct number of times
			new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr[contains(.,'"+release.get("name")+"')]/td[4]/ul/li/form/a").click();
			VoodooUtils.acceptDialog();
		}
		VoodooUtils.focusDefault();
	}
} // BugsModule
