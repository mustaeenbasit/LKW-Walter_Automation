package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;

import java.util.HashMap;

/**
 * Team Based Permissions View or Team Based ACL View.
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class TeamACLView extends View {
	public TeamACLView() throws Exception {
		super("form", "css", "[name='TBAConfiguration']");
		addControl("saveButton", "input", "css", getHookString() + " .actionsContainer input[name='save']");
		addControl("cancelButton", "input", "css", getHookString() + " .actionsContainer input[name='cancel']");
		addControl("enableDisableButton", "input", "id", "tba_set_enabled");
	}

	/**
	 * Enable or Disable Teams Based ACL.
	 * <p>
	 * When used, the state of Teams Based ACL will be enabled or disabled, based on desired state<br>
	 * @param state Boolean of the desired state of Teams Based ACL, true for enabled, false otherwise.
	 * @throws Exception
     */
	public void setTeamsAcl(Boolean state) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("enableDisableButton").set(state.toString());
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Set module permission for Team Base ACL.
	 * @param module Module to be set.
	 * @param state Boolean of desired state to set checkbox, true to enable, false otherwise.
	 * @throws Exception
     */
	public void setModuleCheckbox(Module module, Boolean state) throws Exception {
		setModuleCheckbox(module.moduleNamePlural, state);
	}

	/**
	 * Set module permission for Team Base ACL.
	 * @param module String of the module to be set.
	 * @param state Boolean of desired state to set checkbox, true to enable, false otherwise.
	 * @throws Exception
	 */
	public void setModuleCheckbox(String module, Boolean state) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getModuleCheckbox(module).set(state.toString());
		VoodooUtils.focusDefault();
	}

	/**
	 * Save the settings on this Team ACL View.
	 * <p>
	 * When used, you will be left on the Admin Tools page with the setting for Team ACL saved.
	 * @throws Exception
	 */
	public void save() throws Exception {
		save(true);
	}

	/**
	 * Save the Team based ACL settings.
	 * <p>
	 * When used, your desired action of confirm or cancel for the alert will be performed and you will be brought <br>
	 * back to the Admin Tools page or left on the Team ACL setting screen.
	 * @param confirmAlert Boolean true to confirm alert, false to cancel alert.
	 * @throws Exception
	 */
	public void save(Boolean confirmAlert) throws Exception {
		clickSave();
		if(confirmAlert) {
			// if you want to confirm the changes we first need to check for the visibility of the Warning Alert to handle.
			// Sometimes changes don't trigger an alert; so, if there isn't one, skip the confirmAlert.
			// If there is no alert, an exception will be thrown when trying to process the cancelAlert.
			if(sugar().alerts.getWarning().queryVisible()) {
				sugar().alerts.getWarning().confirmAlert();
			}
		} else {
			sugar().alerts.getWarning().cancelAlert();
		}
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click on the save button.
	 * <p>
	 * When used, the save action will be performed.<br>
	 * If this triggers an alert, the user will need to handle it.
	 * @throws Exception
	 */
	public void clickSave() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("saveButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Click the cancel button.
	 * <p>
	 * When used, if you have made changes to the ACL table of modules, you will be prompted to handle a
	 * warning alert to confirm or cancel the action. If you have not made any changes, no warning message will appear.
	 * @throws Exception
	 */
	public void clickCancel() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("cancelButton").click();
	}

	/**
	 * Set Modules to enabled or disabled.
	 * <p>
	 *
	 * @param modules HashMap<Module, Boolean> of modules to be enabled or disabled based on the Boolean.
	 * @throws Exception
     */
	public void setModulePermissions(HashMap<Module, Boolean> modules) throws Exception {
		for(Module module : modules.keySet()) {
			setModuleCheckbox(module, modules.get(module));
		}
	}

	/**
	 * Get a VoodooControl of the checkbox of the desired Module.
	 * @param module Module of the desired modules checkbox in this Teams ACL View.
	 * @return VoodooControl representing the checkbox of the desired module
	 * @throws Exception
     */
	public VoodooControl getModuleCheckbox(Module module) throws Exception {
		return getModuleCheckbox(module.moduleNamePlural);
	}

	/**
	 * Get a VoodooControl of the checkbox of the desired Module.
	 * @param module String of the module desired in this Teams ACL View.
	 * @return VoodooControl representing the checkbox of the desired module
	 * @throws Exception
     */
	public VoodooControl getModuleCheckbox(String module) throws Exception {
		return new VoodooControl("input", "css", "[type='checkbox'][data-module-name='" + module + "']");
	}
}
