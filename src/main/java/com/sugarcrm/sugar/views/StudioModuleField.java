package com.sugarcrm.sugar.views;

public class StudioModuleField extends View {
	protected static StudioModuleField view;
	
	private StudioModuleField() throws Exception {}
	
	public static StudioModuleField getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleField();
		return view;
	}
} 