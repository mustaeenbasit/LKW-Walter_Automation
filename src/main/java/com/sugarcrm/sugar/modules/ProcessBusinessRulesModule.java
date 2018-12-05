package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessBusinessRulesRecord;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Process Business Rules module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ProcessBusinessRulesModule extends ProcessModule {
	protected static ProcessBusinessRulesModule module;

	public static ProcessBusinessRulesModule getInstance() throws Exception {
		if(module == null)
			module = new ProcessBusinessRulesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProcessBusinessRulesModule() throws Exception {
		moduleNameSingular = "pmse_Business_Rules";
		moduleNamePlural = "pmse_Business_Rules";
		recordClassName = ProcessBusinessRulesRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// ProcessBusinessModule Menu items
		menu = new Menu(this);
		menu.addControl("createBusinessRule", "a", "css", "li[data-module='pmse_Business_Rules'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PMSE_BUSINESS_RULES']");
		menu.addControl("viewBusinessRules", "a", "css", "li[data-module='pmse_Business_Rules'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST']");
		menu.addControl("importBusinessRules", "a", "css", "li[data-module='pmse_Business_Rules'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PMSE_BUSINESS_RULES']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init ProcessBusinessModule...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("rst_type");
		listView.addHeader("rst_module");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		listView.addHeader("date_entered");

		for (int i = 1; i <= 99; i++) {
			// Build internal Voodoo names for each control in a row.
			String design = String.format("design%02d", i);
			String export = String.format("export%02d", i);

			// Build a string prefix that represents the current row in each control.
			String currentRow = "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(" + i + ")";

			// Add Voodoo controls for all controls in the row.
			listView.addControl(design, "a", "css", currentRow + " span[data-voodoo-name='designer_button'] a");
			listView.addControl(export, "a", "css", currentRow + " span[data-voodoo-name='export_button'] a");
		}

		// Specific control for save and design
		createDrawer.addControl("saveAndDesignButton", "a", "css", ".create.fld_save_open_businessrules a");
	}
} // ProcessBusinessModule