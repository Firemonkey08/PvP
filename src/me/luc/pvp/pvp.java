package me.luc.pvp;

import org.bukkit.plugin.java.JavaPlugin;

public class pvp extends JavaPlugin{
	
	public Methoden M;
	public ClickSign CS;
	public static pvp instance;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getCommand("pvp").setExecutor(new Command());
		getServer().getPluginManager().registerEvents(new ClickSign(), this);
		getServer().getPluginManager().registerEvents(new Kill(this), this);
		getServer().getPluginManager().registerEvents(new Ausloggen(), this);
		getServer().getPluginManager().registerEvents(new Methoden(this), this);
		getServer().getPluginManager().registerEvents(new Extras_Mensch_Aerger_Dich(), this);
		�berpr�fung_Ende task_�berpr�fung_Ende = new �berpr�fung_Ende();
		task_�berpr�fung_Ende.runTaskTimer(this, 600, 600);
        reloadConfig();
}
	
    public static pvp getInstance(){
        return instance;
    }
    
	@Override
	public void onDisable() {
	}

}
