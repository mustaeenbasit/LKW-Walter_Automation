package com.sugarcrm.sugar.views;

public class StudioModuleLabel extends View {
	protected static StudioModuleLabel view;
	
	private StudioModuleLabel() throws Exception {}
	
	public static StudioModuleLabel getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleLabel();
		return view;
	}
}