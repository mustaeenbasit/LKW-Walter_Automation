package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessDefinitionRecord;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Process Defintion module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ProcessDefinitionsModule extends ProcessModule {
	protected static ProcessDefinitionsModule module;

	public static ProcessDefinitionsModule getInstance() throws Exception {
		if(module == null)
			module = new ProcessDefinitionsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProcessDefinitionsModule() throws Exception {
		moduleNameSingular = "pmse_Project";
		moduleNamePlural = "pmse_Project";
		recordClassName = ProcessDefinitionRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Process Definition Module Menu items
		menu = new Menu(this);
		menu.addControl("createProcessDefinition", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PMSE_PROJECT']");
		menu.addControl("viewProcessDefinition", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST']");
		menu.addControl("importProcessDefinition", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PMSE_PROJECT']");
	}

	/**
	 * Perform setup which depends on other modules or views already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Process Definitions...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("prj_module");
		listView.addHeader("prj_status");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		listView.addHeader("date_entered");

		for (int i = 1; i <= 99; i++) {
			// Build internal Voodoo names for each control in a row.
			String design = String.format("design%02d", i);
			String enableAndDisable = String.format("enableAndDisable%02d", i);
			String export = String.format("export%02d", i);

			// Build a string prefix that represents the current row in each control.
			String currentRow = "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(" + i + ")";

			// Add Voodoo controls for all controls in the row.
			listView.addControl(design, "a", "css", currentRow + " span[data-voodoo-name='designer_button'] a");
			listView.addControl(enableAndDisable, "a", "css", currentRow + " span[data-voodoo-name='enabled-disabled-button'] a");
			listView.addControl(export, "a", "css", currentRow + " span[data-voodoo-name='export_button'] a");
		}

		// Specific control for save and design
		createDrawer.addControl("saveAndDesignButton", "a", "css", ".create.fld_save_open_design a");
	}

	/**
	 * Import a pre-defined Process Definition and Design.
	 * <p>
	 * When this import process finishes you are left on the Process Design view with the imported process displayed and saved.
	 *
	 * @param pathToImport String path to the .bpm file to import
	 * @throws Exception
	 */
	public void importProcessDefinition(String pathToImport) throws Exception {
		// TODO: VOOD-2000 Get the Rowcount from listview
		int rowCount = sugar().processDefinitions.listView.countRows();

		sugar().navbar.selectMenuItem(sugar().processDefinitions, "importProcessDefinition");
		new VoodooFileField("input", "css", ".fld_project_import.edit input").set(pathToImport);

		// TODO: VOOD-1365
		new VoodooControl("a", "css", "span[data-voodoo-name='project_finish_button'] a").click();
		//Check if it is first import
		if (rowCount == 0) {
			sugar().alerts.getWarning().confirmAlert();
			sugar().alerts.waitForLoadingExpiration();
		}
	}
} // ProcessDefinitionModule
