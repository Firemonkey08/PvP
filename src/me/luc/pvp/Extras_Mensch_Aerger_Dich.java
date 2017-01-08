package me.luc.pvp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class Extras_Mensch_Aerger_Dich implements Listener {

	
	public static HashMap<String, Long> ZeitTod = new HashMap<String, Long>();
	public static double Faenger;
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static Location LocationBlau;
	public static Location LocationRot;
	public static boolean freeze = false;
	public static long Zeit_Anfang;
	public static int beste_Zeit;
	public static Player bester_Spieler;
	public static long longZeit_Eintrag;
	public static int Zeit_Eintrag;
	public static int test;
	public static int timer_neue_Runde;
	public static int timer_freeze;
	public static int timer_Aktualisierung_Zeit;
	public static int DauerRunde = 4000; 
	public static int DauerFreeze = 600;
	public static int Glowing_Anzahl;
	
	public static void Extra_Start(Player player){
		//Team Blau erstellen
	    Team blau = Methoden.board.getTeam("blau");
	    if (blau == null) {
	    	blau = Methoden.board.registerNewTeam("blau");
	    	blau.setDisplayName("Blau");
	    	blau.setAllowFriendlyFire(false);
	    	blau.setPrefix(ChatColor.BLUE.toString());
	    }
		
    	Team rot = Methoden.board.getTeam("rot");
    	if (rot == null) {
    		player.sendMessage("Team Rot nicht initialisiert");
    		return;
    	}
    	
    	//Koordinaten für aktive Arena

		Location LocationBlau = Methoden.Location_ausConfig(Methoden.aktive_arena + ".Blau");
		Location LocationRot = Methoden.Location_ausConfig(Methoden.aktive_arena + ".Rot");
		if (LocationBlau == null || LocationRot == null){
			player.sendMessage("Daten für die Arena " + Methoden.aktive_arena + " nicht gefunden");
			return;
		}
		
		//Scoreboard
		Methoden.objMAD.setDisplaySlot(DisplaySlot.SIDEBAR);
		Methoden.objMAD = Methoden.board.getObjective("time");
	    for(Player p : Bukkit.getOnlinePlayers()){
	    	if (rot.hasPlayer(p)){
	        players.add(p);
	        Methoden.objMAD.getScore(p).setScore(0);
	    	}
		} 
	    
	    Faenger = -1;
	    Zeit_Anfang = new Date().getTime();
	    
        //Scoreboard Zeit runterzählen
        Extras_Mensch_Aerger_Dich instance = new Extras_Mensch_Aerger_Dich();
        instance.Aktualisierung_Zeit();
        
        instance.Timer_neue_Runde();
	}

	//neue Runde nach 2 Minuten
	public void Timer_neue_Runde(){
		timer_neue_Runde = 0;
		Plugin plugin = pvp.getInstance();
	    timer_neue_Runde = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	        public void run() {
	        	neue_Runde();
	        }
	    }, 0L, DauerRunde + 0L);
	}
	
	//Scoreboard übrige Zeit
	public void Aktualisierung_Zeit(){
		Plugin plugin = pvp.getInstance();
		timer_Aktualisierung_Zeit = 1;
	    timer_Aktualisierung_Zeit = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	        public void run() {
	        	
	        	if (freeze) Methoden.objMAD.setDisplayName("Zeit: " + String.valueOf((DauerFreeze/20) + (Zeit_Anfang - new Date().getTime())/1000));
	        	else Methoden.objMAD.setDisplayName("Zeit: " + String.valueOf((DauerRunde/20) + (Zeit_Anfang - new Date().getTime())/1000));
	        	
	        }
	    }, 0L, 20L);
	}
	
	public static void neue_Runde(){
		
		Glowing_Anzahl = 4;
		Zeit_Anfang = new Date().getTime();

		Team rot = Methoden.board.getTeam("rot");
		Team blau = Methoden.board.getTeam("blau");
		
		Faenger = Faenger + 1;
	    
		//Score füre Scoreboard
		if (Faenger != 0){
			for(Player p : players){
		    	if (rot.hasPlayer(p)){
		        	if (!ZeitTod.containsKey(p.getName())){
		        		ZeitTod.put(p.getName(), (long) (DauerRunde/20));
		        	}
		    		
		        	longZeit_Eintrag = (long) Methoden.objMAD.getScore(p).getScore() + ZeitTod.get(p.getName());
		        	Zeit_Eintrag = (int) longZeit_Eintrag;
		        	Methoden.objMAD.getScore(p).setScore(Zeit_Eintrag);
		    	}
			} 
		}
		ZeitTod.clear();
		
		//letzer war Fänger
		if (Faenger >= players.size()){
			Extra_Ende();
			return;
		}
		
	    for(Player p : players) p.sendMessage("Runde " + Double.toString(Faenger + 1) + " beginnt.");
	    
    	// Spieler Inventar geben und teleportieren
        for(Player p : players) rot.addPlayer(p);
        if (Faenger >= 0 && Faenger < players.size()){
        	blau.addPlayer(players.get((int) Faenger));
        	
    		for(Player p : players) p.sendMessage(ChatColor.BLUE + players.get((int) Faenger).getName() + ChatColor.WHITE + " ist Fänger");
        }
	
		Location LocationBlau = Methoden.Location_ausConfig(Methoden.aktive_arena + ".Blau");
		Location LocationRot = Methoden.Location_ausConfig(Methoden.aktive_arena + ".Rot");
		
        for(Player p : players){
            if (rot.hasPlayer(p)){
            	p.setGameMode(GameMode.getByValue(Methoden.GamemodeRot));
            	p.setScoreboard(Methoden.board);
            	Methoden.Klassenart.put(p.getName(), "Lauefer");
            	Methoden.InventarGeben(p);
            	p.setHealth(Methoden.LebenRot);
            	p.setFoodLevel(15);
            	p.teleport(LocationRot);
            	Methoden.imKampf.add(p.getName());
            }
            if (blau.hasPlayer(p)){
            	p.setGameMode(GameMode.getByValue(Methoden.GamemodeRot));
            	p.setScoreboard(Methoden.board);
            	Methoden.Klassenart.put(p.getName(), "Faenger");
            	Methoden.InventarGeben(p);
            	p.setHealth(Methoden.LebenBlau);
            	p.setFoodLevel(15);
            	p.teleport(LocationBlau);
            	Methoden.imKampf.add(p.getName());
        		p.addPotionEffect(new PotionEffect(PotionEffectType.getById(2),DauerFreeze,8));
            }
        }
        Methoden.Kampf = true;
        Methoden.KampfEnde = false;
        freeze = true;
   
        Plugin plugin = pvp.getInstance();
        timer_freeze = 2;
        timer_freeze = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    
        	//Fänger ist frei und sieht alles
	        public void run() {
	        	freeze = false;
	    	    for(Player p : players) {
	    	    	p.sendMessage(ChatColor.RED + "Fänger ist frei!");
	    	    }
	        }
	    }, DauerFreeze + 0L);
	}
	
	public static void Extra_Ende(){
		//besten Spieler ermitteln
		for(Player p : players){
			p.setGameMode(GameMode.getByValue(Methoden.GamemodeBack));
			Methoden.Playerleft = false;
			if (Methoden.objMAD.getScore(p).getScore() >= beste_Zeit) {
				beste_Zeit = Methoden.objMAD.getScore(p).getScore();
				bester_Spieler = p;
			}
			 p.setFoodLevel(20);
		 }
		for(Player p : players){
			for(Player p2 : players){
				p.sendMessage(p2.getName() + ": " +  Integer.toString(Methoden.objMAD.getScore(p2).getScore()));
			}
			p.sendMessage("Spieler " + ChatColor.GREEN + bester_Spieler.getName() + ChatColor.WHITE + " hat gewonnen!");
		}
		
		//beenden
		ZeitTod.clear();
		Methoden.KampfBeenden();
		Bukkit.getScheduler().cancelTask(timer_neue_Runde);
		Bukkit.getScheduler().cancelTask(timer_Aktualisierung_Zeit);
		 
	}
	
	public static void Glowing(){
		Glowing_Anzahl = Glowing_Anzahl - 1;
		if (Glowing_Anzahl >= 0){
		    for(Player p : players) {
				Team rot = Methoden.board.getTeam("rot");
				p.sendMessage(ChatColor.RED + "Fänger sieht alle! Noch " + ChatColor.GREEN + Glowing_Anzahl + ChatColor.RED +" übrig.");
				if (rot.hasPlayer(p)){
			    	PotionEffectType Effekt = PotionEffectType.getById(24);
		    		Integer Dauer = 80;
		    		Integer Staerke = 1;
		    		p.addPotionEffect(new PotionEffect(Effekt,Dauer,Staerke));
				}
		    }
		}
		else
			players.get((int) Faenger).sendMessage("Alle aufgebraucht");
	}
}
