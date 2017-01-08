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
		Überprüfung_Ende task_Überprüfung_Ende = new Überprüfung_Ende();
		task_Überprüfung_Ende.runTaskTimer(this, 600, 600);
        reloadConfig();
}
	
    public static pvp getInstance(){
        return instance;
    }
    
	@Override
	public void onDisable() {
	}

}
