package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models the Forecast Module Settings View.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ForecastSetting extends View {

	public ForecastSetting() throws Exception {

		String rangeThreeTag = ".show_bucketsFields [id=show_buckets_ranges] [data-voodoo-name=upside] .ticks:nth-of-type";
		// Common Setup controls
		addControl("cancel", "a", "css", "a[name='cancel_button'");
		addControl("save", "a", "css", "a[name='save_button'");

		// Forecast Config Tabs
		// Time Period
		addControl("timePeriodSettings", "a", "css", "div[data-voodoo-name='config-timeperiods'] a");
		addSelect("timePeriodType", "a", "css", "span[data-voodoo-name='timeperiod_interval'] a");
		addSelect("timePeriodFuture", "a", "css", "span[data-voodoo-name='timeperiod_shown_forward'] a");
		addSelect("timePeriodPast", "a", "css", "span[data-voodoo-name='timeperiod_shown_backward'] a");

		// Range
		addControl("rangeSettings", "a", "css", "div[data-voodoo-name='config-ranges'] a");
		addControl("rangeTwo", "input", "css", "div[data-voodoo-name='config-ranges'] .show_binaryFields input");
		addControl("rangeThree", "input", "css", "div[data-voodoo-name='config-ranges'] .show_bucketsFields input");
		addControl("rangeCustom", "input", "css", "div[data-voodoo-name='config-ranges'] .show_custom_bucketsFields input");
		
		// Range Slider Bars
		addControl("lowerRangeTwo", "div", "css" ,".show_binaryFields .fld_include.edit .noUi-handle.noUi-lowerHandle div");
		addControl("lowerRangeThree", "div", "css" ,".fld_upside .noUi-handle.noUi-lowerHandle div");
		addControl("upperRangeThree", "div", "css" ,".fld_upside .noUi-handle.noUi-upperHandle div");
		
		// Two Range tick marks, every 10%
		addControl("rangeTwo00", "div", "css", ".show_binaryFields .ticks:nth-of-type(4)");
		addControl("rangeTwo10", "div", "css", ".show_binaryFields .ticks:nth-of-type(5)");
		addControl("rangeTwo20", "div", "css", ".show_binaryFields .ticks:nth-of-type(6)");
		addControl("rangeTwo30", "div", "css", ".show_binaryFields .ticks:nth-of-type(7)");
		addControl("rangeTwo40", "div", "css", ".show_binaryFields .ticks:nth-of-type(8)");
		addControl("rangeTwo50", "div", "css", ".show_binaryFields .ticks:nth-of-type(9)");
		addControl("rangeTwo60", "div", "css", ".show_binaryFields .ticks:nth-of-type(10)");
		addControl("rangeTwo70", "div", "css", ".show_binaryFields .ticks:nth-of-type(11)");
		addControl("rangeTwo80", "div", "css", ".show_binaryFields .ticks:nth-of-type(12)");
		addControl("rangeTwo90", "div", "css", ".show_binaryFields .ticks:nth-of-type(13)");
		
		// Three Range tick marks, every 10%
		addControl("rangeThree00", "div", "css", rangeThreeTag + "(4)");
		addControl("rangeThree10", "div", "css", rangeThreeTag + "(5)");
		addControl("rangeThree20", "div", "css", rangeThreeTag + "(6)");
		addControl("rangeThree30", "div", "css", rangeThreeTag + "(7)");
		addControl("rangeThree40", "div", "css", rangeThreeTag + "(8)");
		addControl("rangeThree50", "div", "css", rangeThreeTag + "(9)");
		addControl("rangeThree60", "div", "css", rangeThreeTag + "(10)");
		addControl("rangeThree70", "div", "css", rangeThreeTag + "(11)");
		addControl("rangeThree80", "div", "css", rangeThreeTag + "(12)");
		addControl("rangeThree90", "div", "css", rangeThreeTag + "(13)");

		// Scenarios
		addControl("scenarioSettings", "a", "css", "div[data-voodoo-name='config-scenarios'] a");
		addSelect("scenario", "li", "css", "div[data-voodoo-name='config-scenarios'] .select2-container .select2-search-field:not([style='display: none;'])");
		addControl("scenarioX", "a", "css", "div[data-voodoo-name='config-scenarios'] .select2-choices .select2-search-choice:not([style='display: none;']) a");

		// Worksheet Columns
		addControl("worksheetSettings", "a", "css", "div[data-voodoo-name='config-worksheet-columns'] a");
		addSelect("worksheet", "li", "css", "div[data-voodoo-name='config-worksheet-columns'] .select2-container .select2-search-field");
		addControl("workSheetX", "a", "css", "div[data-voodoo-name='config-worksheet-columns'] .select2-choices .select2-search-choice:not([style='display: none;']) a");
	}

	/**
	 * Toggle Time Period Panel in Forecast Module Setup.
	 * <p>
	 * Must be in Forecast Module Setup process to use.<br>
	 * Leaves you in Forecast Module Setup with the Time Period Panel toggled.
	 * 
	 * @throws Exception
	 */
	public void toggleTimePeriodSettings() throws Exception {
		getControl("timePeriodSettings").click();
		VoodooUtils.pause(1000);
	}

	/**
	 * Toggle Forecast Range Panel in Forecast Module Setup.
	 * <p>
	 * Must be in Forecast Module Setup process to use.<br>
	 * Leaves you in Forecast Module Setup with the Forecast Range Panel
	 * toggled.
	 * 
	 * @throws Exception
	 */
	public void toggleRangeSettings() throws Exception {
		getControl("rangeSettings").click();
		VoodooUtils.pause(1000);
	}

	/**
	 * Toggle Scenario Panel in Forecast Module Setup.
	 * <p>
	 * Must be in Forecast Module Setup process to use.<br>
	 * Leaves you in Forecast Module Setup with the Scenario Panel toggled.
	 * 
	 * @throws Exception
	 */
	public void toggleScenarioSettings() throws Exception {
		getControl("scenarioSettings").click();
		VoodooUtils.pause(1000);
	}

	/**
	 * Toggle Worksheet Column Panel in Forecast Module Setup.
	 * <p>
	 * Must be in Forecast Module Setup process to use.<br>
	 * Leaves you in Forecast Module Setup with the Worksheet Column Panel
	 * toggled.
	 * 
	 * @throws Exception
	 */
	public void toggleWorksheetSettings() throws Exception {
		getControl("worksheetSettings").click();
		VoodooUtils.pause(1000);
	}

	/**
	 * Click the save button in Forecast Module Setup.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @throws Exception
	 */
	public void saveSettings() throws Exception {
		getControl("save").click();
		VoodooUtils.pause(1000);
		new VoodooControl("div", "css", "#alerts").waitForVisible(); // Takes a couple of seconds before the save actually executes in Sugar
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Click the cancel button in Forecast Module Setup.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @throws Exception
	 */
	public void cancelSettings() throws Exception {
		getControl("cancel").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Removes all the current selected scenarios on the Scenarios Panel.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @throws Exception
	 */
	public void removeAllScenarios() throws Exception {
		while (getControl("scenarioX").queryVisible()) {
			VoodooUtils.pause(500);
			getControl("scenarioX").click();
			VoodooUtils.pause(500);
		}
		VoodooUtils.pause(500);
	}

	/**
	 * Removes all the current selected worksheet columns on the Scenarios
	 * Panel.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @throws Exception
	 */
	public void removeAllWorksheetColumns() throws Exception {
		while (getControl("workSheetX").queryVisible()) {
			VoodooUtils.pause(500);
			getControl("workSheetX").click();
			VoodooUtils.pause(500);
		}
		VoodooUtils.pause(500);
	}

	/**
	 * Select the desired scenarios for Forecast Module Setup.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @param	scenario	a String of scenario to select.
	 * @throws Exception
	 */
	public void selectScenarios(String scenario) throws Exception {
		getControl("scenario").set(scenario);
		VoodooUtils.pause(500);

	}

	/**
	 * Select the desired worksheet columns for Forecast Module Setup.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @param worksheet
	 *            FieldSet of worksheet columns to select.
	 * @throws Exception
	 */
	public void selectWorksheetColumns(String worksheet) throws Exception {
		getControl("worksheet").set(worksheet);
		VoodooUtils.pause(500);
	}
	
	/**
	 * Set Forecast Ranges.
	 * <p>
	 * Must be in the Forecast Module Setup to use.
	 * 
	 * @param rangeInfo String prepresentation of range info,<br>
	 * e.g. two,20 -- which means Two Range, lower end 20%.<br>
	 * or three,50,70 -- which means Three Range, lower end 50% upper end 70%
	 * 
	 * @throws Exception
	 */
	public void setRanges(String rangeInfo) throws Exception {
		String[] subStrings = rangeInfo.split(",");

		String numRange = subStrings[0];
		int lowEndRange = Integer.parseInt(subStrings[1]);
		
		int highEndRange = 0;
		if(subStrings.length == 3){
			highEndRange = Integer.parseInt(subStrings[2]);
		}
		
		switch(numRange) {
			case "two": {
				getControl("rangeTwo").click();
				VoodooUtils.pause(1000);
				getControl("lowerRangeTwo").dragNDrop(getControl("rangeTwo" + lowEndRange));
				break;
			}
			case "three": {
				getControl("rangeThree").click();
				VoodooUtils.pause(1000);
				getControl("lowerRangeThree").dragNDrop(getControl("rangeThree" + lowEndRange));
				VoodooUtils.pause(500);
				getControl("upperRangeThree").dragNDrop(getControl("rangeThree" + highEndRange));
				break;
			}
			case "custom": {
				getControl("rangeCustom").click();
				// Determine how to set custom ranges later.
				break;
			}
			default:
				break;
		}
		
	}

} // ForecastSetting