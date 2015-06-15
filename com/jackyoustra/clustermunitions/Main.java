package com.jackyoustra.clustermunitions;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new ArrowListener(), this);
		super.onEnable();
	}
}
