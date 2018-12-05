package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessEmailTemplatesRecord;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.View;

/**
 * Contains data and tasks associated with the ProcessEmailsModule, such as field
 * data.
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ProcessEmailTemplatesModule extends ProcessModule {
	protected static ProcessEmailTemplatesModule module;
	public View designView;

	public static ProcessEmailTemplatesModule getInstance() throws Exception {
		if(module == null)
			module = new ProcessEmailTemplatesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProcessEmailTemplatesModule() throws Exception {
		moduleNameSingular = "pmse_Emails_Templates";
		moduleNamePlural = "pmse_Emails_Templates";
		recordClassName = ProcessEmailTemplatesRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Account Module Menu items
		menu = new Menu(this);
		menu.addControl("createProcessEmailTemplate", "a", "css", "li[data-module='pmse_Emails_Templates'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PMSE_EMAILS_TEMPLATES']");
		menu.addControl("viewProcessEmailTemplates", "a", "css", "li[data-module='pmse_Emails_Templates'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST']");
		menu.addControl("importProcessEmailTemplates", "a", "css", "li[data-module='pmse_Emails_Templates'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PMSE_EMAILS_TEMPLATES']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init ProcessEmailsModule...");
		super.init();

		designView = new View("div", "css", "#content .layout_pmse_Emails_Templates");
		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("base_module");
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
		createDrawer.addControl("saveAndDesignButton", "a", "css", ".create.fld_save_open_emailstemplates a");

		// TODO: VOOD-1859 : Lib support for Fields selector of Process Email Template's Design View
		// controls for design view
		designView.addControl("targetModule", "div","css", "[data-voodoo-name='base_module'] div");
		designView.addControl("designName", "input", "css", ".edit.fld_name input");
		designView.addControl("designDescription", "textarea","css",".edit.fld_description [name='description']");
		designView.addControl("subject", "input","css",".edit.fld_subject input");
		designView.addControl("contentBody", "body","css", "#tinymce");
		designView.addControl("save", "a", "css", ".edit.fld_save_button [name='save_button']");
		designView.addControl("saveAndExit", "a", "css", ".edit.fld_save_buttonExit [name='save_buttonExit']");
		designView.addControl("cancel", "a", "css", ".edit.fld_cancel_button [name='cancel_button']");
		designView.addControl("moduleTitle", "span", "css", ".headerpane .record-cell .module-title");
		designView.addControl("subjectSelector", "a", "css", ".edit.fld_subject [data-name='subject']");
		designView.addControl("primaryDropdown", "a", "css", ".btn.dropdown-toggle.btn-primary");
		designView.addControl("fieldsSelector", "a", "css", "[data-voodoo-name='subject'] a");
	}

	/**
	 * Click the "Save" button on design view.
	 *
	 * You must already be on the Design view to use this method.
	 * Stays on design view after saving the record.
	 *
	 * @throws Exception
	 */
	public void saveDesign() throws Exception{
		designView.getControl("save").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the "SaveAndExit" button on design view.
	 *
	 * You must already be on the Design view to use this method.
	 * Takes you to the Process Email Templates in listview mode.
	 *
	 * @throws Exception
	 */
	public void saveAndExitDesign() throws Exception{
		designView.getControl("primaryDropdown").click();
		designView.getControl("saveAndExit").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the "Cancel" button on design view.
	 *
	 * You must already be on the Design view to use this method.
	 * Leaves you on the Design view and displays the delete confirmation prompt.
	 *
	 * @throws Exception
	 */
	public void cancelDesign() throws Exception {
		designView.getControl("cancel").click();
	}

} // ProcessEmailsModule