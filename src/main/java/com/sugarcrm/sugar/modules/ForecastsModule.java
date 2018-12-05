package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ForecastSetting;
import com.sugarcrm.sugar.views.WorksheetView;

/**
 * Contains tasks and data associated with the Forecasts module.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ForecastsModule extends Module {
	protected static ForecastsModule module;
	public ForecastSetting setup;
	public WorksheetView worksheet;
	Configuration config;
	String baseUrl;

	public static ForecastsModule getInstance() throws Exception {
		if (module == null)
			module = new ForecastsModule();
		return module;
	}

	private ForecastsModule() throws Exception {
		moduleNameSingular = "Forecast";
		moduleNamePlural = "Forecasts";
		setup = new ForecastSetting();
		worksheet = new WorksheetView(sugar().revLineItems, "div", "ID", "content");
		config = VoodooUtils.getGrimoireConfig();
		baseUrl = new SugarUrl().getBaseUrl();
	}

	/**
	 * Reset the Forecast Module Settings.
	 * <p>
	 * Can be called anywhere in the Application.<br>
	 * When used, you will be left on the Forecast Setting Config Page.
	 * 
	 * @throws Exception
	 */
	public void resetForecastSettings() throws Exception {
		// TODO: Make this go() to a URL which triggers forecast settings to
		// accept new settings.
		VoodooUtils.go(baseUrl + "#bwc/index.php?module=Forecasts&action=ResetSettings");
		sugar().alerts.waitForLoadingExpiration(30000);
		sugar().forecasts.setup.getControl("save").waitForVisible(30000);
	}

	/**
	 * Setup Forecast Module Settings.
	 * <p>
	 * Can be called anywhere in the Application.<br>
	 * When used, Forecast Module will be setup per desired settings.
	 * 
	 * @param settings FieldSet of Forecast Module Settings to use for setup.
	 * @throws Exception
	 */
	public void setupForecasts(FieldSet settings) throws Exception {
		resetForecastSettings();
		sugar().navbar.navToModule(moduleNamePlural);
		
		// Remove all worksheet columns
		setup.toggleWorksheetSettings();
		setup.removeAllWorksheetColumns();
		
		// Remove all scenarios
		setup.toggleScenarioSettings();
		setup.removeAllScenarios();
		
		for (String controlName : settings.keySet()) {
			if (controlName.contains("timePeriod") ) {
				if (!setup.getControl("timePeriodType").queryVisible()) {
					setup.toggleTimePeriodSettings();					
				}
				setup.getControl(controlName).set(settings.get(controlName));
			}
			else if (controlName.contains("range")) {
				if (!setup.getControl("rangeTwo").queryVisible()) {
					setup.toggleRangeSettings();					
				}
				setup.setRanges(settings.get(controlName));
			}
			else if (controlName.contains("worksheet")) {
				if (!setup.getControl("worksheet").queryVisible()) {
					setup.toggleWorksheetSettings();					
				}
				setup.selectWorksheetColumns(settings.get(controlName));				
			}
			else if (controlName.contains("scenario")) {
				if (!setup.getControl("scenario").queryVisible()) {
					setup.toggleScenarioSettings();					
				}
				setup.selectScenarios(settings.get(controlName));
			}
		}
		setup.saveSettings();
	}
} // ForecastsModule