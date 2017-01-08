package me.luc.pvp;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;


public class Kill implements Listener {

	private pvp plugin;
	public Kill(pvp p) {
	    plugin = p;
	    }
	
	String Sieger_Team;
	public static int killCount;
	public static boolean Mensch_Aerger_Dich_rotes_Team = true;
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityRespawn(PlayerRespawnEvent playerRespawnEvent) {
		
//STERBENDE Person
		Player dyingP = (Player) playerRespawnEvent.getPlayer(); 
		plugin.reloadConfig();
       	FileConfiguration config = plugin.getConfig();
       	
		Location LocationBack = Methoden.Location_ausConfig(Methoden.aktive_arena + ".Back");
		
		//wenn ein Spieler beim Kampfende stirbt
		if (Methoden.KampfEnde == true){
    		if (Methoden.imKampf.contains(dyingP.getName())){
    			Methoden.SpielerBeenden(dyingP);
	    		if (LocationBack != null) dyingP.teleport(LocationBack);
	    		dyingP.sendMessage("Kampf beendet");
	    		Methoden.KampfEnde = true;
    		}
		}
		
		
		if (Methoden.Kampf == false) return;
       	
	    Team rot = Methoden.board.getTeam("rot");
	    Team blau = Methoden.board.getTeam("blau");
	    
	  //Teleporten und Inventar
	    if (blau.hasPlayer(dyingP)){
	    	Location LocationBlau = Methoden.Location_ausConfig((String) config.get(Methoden.aktive_arena + ".Blau.Dead"));
	    	if (LocationBlau == null) return;
	    	playerRespawnEvent.setRespawnLocation(LocationBlau);
	    	dyingP.setHealth(Methoden.LebenTotBlau);
	    	dyingP.setGameMode(GameMode.getByValue(Methoden.GamemodeTotBlau));
	    	Methoden.InventarGeben(dyingP);
	    }
    	
	    if (rot.hasPlayer(dyingP)){
		    Location LocationRot = Methoden.Location_ausConfig((String) config.get(Methoden.aktive_arena + ".Rot.Dead"));
		    if (LocationRot == null) return;
	    	playerRespawnEvent.setRespawnLocation(LocationRot);
	    	dyingP.setHealth(Methoden.LebenTotRot);
	    	dyingP.setGameMode(GameMode.getByValue(Methoden.GamemodeTotRot));
	    	Methoden.InventarGeben(dyingP);
	    }
	}
	 
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(PlayerDeathEvent entityDeathEvent) {
		Player deadP = entityDeathEvent.getEntity();
	    Team rot = Methoden.board.getTeam("rot");
	    Team blau = Methoden.board.getTeam("blau");
	    Player killer = entityDeathEvent.getEntity().getKiller();
	    
		if (Methoden.Kampf == false) return;
		
	    //Mensch_Aerger_Dich
	    if (Methoden.aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich")) {
	    	Mensch_Aerger_Dich_rotes_Team = false;
	    	if (rot.hasPlayer(deadP)) {
	    		Extras_Mensch_Aerger_Dich.ZeitTod.put(deadP.getName(), (long) ((new Date().getTime() - Extras_Mensch_Aerger_Dich.Zeit_Anfang))/1000);
	    		//bei Tötung Punkte für Fänger
	    		if (killer != null){
	    			if (blau.hasPlayer(killer)) Methoden.objMAD.getScore(killer).setScore(Methoden.objMAD.getScore(killer).getScore() + 10);
	    		}
	    		//lebt noch jmd aus dem roten T
	    		for(OfflinePlayer p : rot.getPlayers()) {
		    		if (p.isOnline()){
		    			if (!Extras_Mensch_Aerger_Dich.ZeitTod.containsKey(p.getName())){
		    				Mensch_Aerger_Dich_rotes_Team = true;
		    			}
		    		}
	    		}
	    		if (Mensch_Aerger_Dich_rotes_Team == false){
		    		Bukkit.getScheduler().cancelTask(Extras_Mensch_Aerger_Dich.timer_neue_Runde);
		            Extras_Mensch_Aerger_Dich instance = new Extras_Mensch_Aerger_Dich();
		            instance.Timer_neue_Runde();
	    		}
	    		
	    	}
	    	if (blau.hasPlayer(deadP)) {
	    		Bukkit.getScheduler().cancelTask(Extras_Mensch_Aerger_Dich.timer_neue_Runde);
	            Extras_Mensch_Aerger_Dich instance = new Extras_Mensch_Aerger_Dich();
	            instance.Timer_neue_Runde();
	    	}
	    		
	    }
	    
		
		//Items löschen	    
	    entityDeathEvent.getDrops().clear();
	    
		if (Methoden.aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich")) return;
	    
		Objective obj = Methoden.board.getObjective("kills");

//TÖTENDE Person		
	    //Score erhöhen fürs Team
	    if (blau.hasPlayer(deadP)){
	    	for(OfflinePlayer p : rot.getPlayers()){
	    		killCount = obj.getScore(p).getScore();
	    		killCount += 1;
	    		obj.getScore(p).setScore(killCount);
	    		//Check Sieg//Check Sieg
	    		if (killCount >= Methoden.kills_Sieg){
	    			for(Player p2 : plugin.getServer().getOnlinePlayers()) p2.sendMessage(ChatColor.RED + "Team Rot hat gewonnen!");
	    			Methoden.KampfBeenden();
	    			return;
	    		}
	    	}
	    }
	    if (rot.hasPlayer(deadP)){
	    	for(OfflinePlayer p : blau.getPlayers()){
	    		killCount = obj.getScore(p).getScore();
	    		killCount += 1;
	    		obj.getScore(p).setScore(killCount);
	    		//Check Sieg
	    		if (killCount >= Methoden.kills_Sieg){
	    			for (Player p2 : plugin.getServer().getOnlinePlayers()) p2.sendMessage(ChatColor.BLUE + "Team Blau hat gewonnen!");
	    			Methoden.KampfBeenden();
	    			return;
	    		}
	    	}
	   
	    }
	}
}
