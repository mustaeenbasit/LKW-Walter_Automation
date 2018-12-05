package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BWCRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.TeamACLView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains data and tasks associated with the Teams module, such as field data.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TeamsModule extends BWCModule {
	protected static TeamsModule module;
	public TeamACLView teamsAcl = new TeamACLView();

	public static TeamsModule getInstance() throws Exception {
		if(module == null)
			module = new TeamsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private TeamsModule() throws Exception {
		moduleNameSingular = "Team";
		moduleNamePlural = "Teams";
		bwcSubpanelName = "Teams";
		recordClassName = TeamRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// TeamsModule Module Menu items
		menu = new Menu(this);
		menu.addControl("createTeam", "a", "css", "[data-navbar-menu-item='LNK_NEW_TEAM']");
		menu.addControl("viewTeams", "a", "css", "[data-navbar-menu-item='LNK_LIST_TEAM']");

		// override 'save' and 'cancel' hook values for edit view and 'edit' button for detailview
		editView.addControl("save", "input", "id", "btn_save");
		editView.addControl("cancel", "input", "id", "btn_cancel");
		detailView.addControl("editButton", "a", "id", "teamEditButton");
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Teams...");
		super.init();

		// Related Subpanels
		relatedModulesMany.put("team_user", sugar().users);

		// Add Subpanels
		detailView.addSubpanels();
	}

	/**
	 * simple helper method to navigate to the teams's listview
	 */
	public void navToListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating list view ...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Creates a single team record via UI using data as Fieldset.
	 * This method overloads the BWCModule method with the same signature because the
	 * Teams module has non-standard create/edit/detail views.
	 * @param teamData a FieldSet containing the data for the new record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet teamData) throws Exception {
		VoodooUtils.voodoo.log.info("Reconciling team data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(teamData);
		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");

		// Navigate to Teams List View
		navToListView();

		// Start the Create process
		sugar().navbar.selectMenuItem(this, "create" + moduleNameSingular);

		// Find the elements in the fieldset and set their values
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
		// Click Save
		editView.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Setting Guid to Record
		String href = VoodooUtils.getUrl();
		int recordEqualsPos = href.lastIndexOf("record=");
		String guid = href.substring(recordEqualsPos + 7);

		BWCRecord toReturn = (BWCRecord)Class.forName(recordClassName).getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Team record created.");
		return toReturn;
	}

	/**
	 * It will perform delete team from the listview via searching team
	 * @param teamName String which you want to delete
	 * @throws Exception
	 */
	public void deleteTeam(String teamName) throws Exception {
		VoodooUtils.voodoo.log.info("Deleting " + teamName + " ...");

		navToListView();
		sugar().teams.listView.basicSearch(teamName);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().teams.listView.getControl("selectAllCheckbox").click();
		sugar().teams.listView.getControl("deleteButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		sugar().teams.listView.clearSearchForm();
		sugar().teams.listView.submitSearchForm();
	}

	/**
	 * Enable Teams Based Permissions.
	 * <p>
	 * When used, Team Based Permissions will be enabled with the default settings.<br>
	 * You will be navigated back to Admin Tools after calling this method.
	 * @throws Exception
     */
	public void enableTeamAcl() throws Exception {
		sugar().admin.navToAdminPanelLink("teamsPermissions");
		teamsAcl.setTeamsAcl(true);
		teamsAcl.save();
	}

	/**
	 * Disable Teams Based Permissions.
	 * <p>
	 * When used, Team Based Permissions will be disable.<br>
	 * You will be navigated back to Admin Tools after calling this method.
	 * @throws Exception
     */
	public void disableTeamAcl() throws Exception {
		sugar().admin.navToAdminPanelLink("teamsPermissions");
		teamsAcl.setTeamsAcl(false);
		teamsAcl.save(true);
	}

	/**
	 * Set up Team Based Permissions.
	 * <p>
	 * When used, Teams Based Permissions will be enabled and set to the proper modules.<br>
	 * This method will navigate back to Admin Tools once used.
	 *
	 * @param modules HashMap of Modules and Boolean of desired state for Team Based ACL.
	 * @throws Exception
     */
	public void setupTeamAcl(HashMap<Module, Boolean> modules) throws Exception {
		sugar().admin.navToAdminPanelLink("teamsPermissions");
		teamsAcl.setTeamsAcl(true);
		teamsAcl.setModulePermissions(modules);
		teamsAcl.save(true);
	}

	/**
	 * Set up Team Based Permissions.
	 * <p>
	 * When used, Teams Based Permissions will be enabled and set for the desired module.<br>
	 * This method will navigate back to Admin Tools once used.
	 *
	 * @param module Module to enable or disable in Team Based ACL.
	 * @param state Boolean of the state desired, true for enabled, false otherwise.
	 * @throws Exception
	 */
	public void setupTeamAcl(Module module, Boolean state) throws Exception {
		HashMap<Module, Boolean> modules = new HashMap<>();
		modules.put(module, state);
		setupTeamAcl(modules);
	}
} // TeamsModule
