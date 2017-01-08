package me.luc.pvp;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


@SuppressWarnings("deprecation")
public class Methoden implements Listener{

	private static pvp plugin;
	public Methoden(pvp p) {
	    plugin = p;
	    }
	
	public static ArrayList<String> imKampf = new ArrayList<String>();
	public static HashMap<String, String> Klassenart = new HashMap<String, String>();
	public static String KlassePlayer;
	
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getNewScoreboard();
	public static Scoreboard leeresboard = manager.getNewScoreboard();
	public static Objective obj = board.getObjective("kills");
	public static Objective objMAD = board.getObjective("time");
	
	public static double LebenBack;
	public static double LebenTotBlau;
	public static double LebenTotRot;
	public static int GamemodeRot;
	public static int GamemodeBlau;
	public static int GamemodeBack;
	public static int GamemodeTotBlau;
	public static int GamemodeTotRot;
	public static double LebenRot;
	public static double LebenBlau;
	public static String aktive_welt = "";
	public static String aktive_arena = "";
	public static int kills_Sieg;
	public static boolean Vorbereitung = false;
	public static boolean roteSpieler = false;
	public static boolean blaueSpieler = false;
	public static boolean Kampf = false;
	public static boolean KampfEnde = false;
	public static boolean Playerleft = false;
	public static ArrayList<String> Terrains = new ArrayList<String>();
	
	public static void pvpVorbereiten(Player EventP){
		
		if (Kampf == true){
			EventP.sendMessage("Erst den laufenden Kampf beenden");
			return;
		}
		
		if (board.getTeam("rot") != null) {
			board.getTeam("rot").unregister();
		}
		if (board.getTeam("blau") != null) {
			board.getTeam("blau").unregister();
		}
		if (obj != null) {
		obj.unregister();
		}
		if (objMAD != null) {
		objMAD.unregister();
		}
    	obj = board.registerNewObjective("kills", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("Kills");
		obj = board.getObjective("kills");
		
    	objMAD = board.registerNewObjective("time", "dummy");
		objMAD.setDisplayName("Zeit als Flüchtling");
		
		
        for(Player p : EventP.getLocation().getWorld().getPlayers()){
            p.sendMessage("Vorbereitungen getroffen für PvP");
        }
        Playerleft = false;
        Vorbereitung = true;
        KampfEnde = false;
	}
	public static void ArenaAuswaehlen(String arena, Player eventp){

		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		aktive_arena = arena;
		
		if (Vorbereitung == true){
	        if (plugin.getConfig().isSet(aktive_arena)){
	        	String getkills = aktive_arena + ".killsSieg";
	        	String getworld = aktive_arena + ".Back.world";
	        	String getlebenrot = aktive_arena + ".Rot.LebenStart";
	        	String getlebenblau = aktive_arena + ".Blau.LebenStart";
	        	String getlebenBack = aktive_arena + ".Back.LebenStart";
	        	String getlebenTotBlau = config.getString(aktive_arena + ".Blau.Dead") + ".LebenStart";
	        	String getlebenTotRot = config.getString(aktive_arena + ".Rot.Dead") + ".LebenStart";
	        	String getGamemodeRot = aktive_arena + ".Rot.gamemode";
	        	String getGamemodeBlau = aktive_arena + ".Blau.gamemode";
	        	String getGamemodeBack = aktive_arena + ".Back.gamemode";
	        	String getGamemodeTotBlau = config.getString(aktive_arena + ".Blau.Dead") + ".gamemode";
	        	String getGamemodeTotRot = config.getString(aktive_arena + ".Rot.Dead") + ".gamemode";
	        	String getTerrainmanager = aktive_arena + ".Terrainmanager";
	        	
	        	if (config.isSet(getTerrainmanager)) {
	        		for (String key : config.getConfigurationSection(getTerrainmanager).getKeys(false)) Terrains.add(config.getString(getTerrainmanager + key));
	        	}
	        	
	            LebenRot = (double) config.getDouble(getlebenrot);
	    	    LebenBlau = (double) config.getDouble(getlebenblau);
	    	    LebenBack = (double) config.getDouble(getlebenBack);
	    	    LebenTotBlau = (double) config.getDouble(getlebenTotBlau);
	    	    LebenTotRot = (double) config.getDouble(getlebenTotRot);
	    	    GamemodeRot = (int) config.getInt(getGamemodeRot);
	    	    GamemodeBlau = (int) config.getInt(getGamemodeBlau);
	    	    GamemodeBack = (int) config.getInt(getGamemodeBack);
	    	    GamemodeTotBlau = (int) config.getInt(getGamemodeTotBlau);
	    	    GamemodeTotRot = (int) config.getInt(getGamemodeTotRot);
	    	    
		    	kills_Sieg = (int) config.getInt(getkills);
		        aktive_welt = (String) config.getString(getworld);
		        
		        obj.setDisplayName("Kills (" + Integer.toString(kills_Sieg) + ")");

		        for(Player p : Bukkit.getWorld(aktive_welt).getPlayers()){
		            p.sendMessage("Arena " + aktive_arena + " ausgewählt");
		        }
	        }
        
	       else {
	    	   eventp.sendMessage("Daten für die Arena " + aktive_arena + " nicht gefunden");
	    	   aktive_arena = "";
	       }
       }
	   else {
	    eventp.sendMessage("Bereite ein PvP Spiel vor");
	   }
	}
	public static void TeamzuweisungRot (Player playerrot){	
		playerrot.setScoreboard(board);
		if (Vorbereitung == false) {
			playerrot.sendMessage("Bereite ein PvP Spiel vor");
			return;
		}
	    Team rot = board.getTeam("rot");
	    if (rot == null) {
	    	rot = board.registerNewTeam("rot");
	    	rot.setDisplayName("Rot");
	    	rot.setAllowFriendlyFire(false);
	    	rot.setPrefix(ChatColor.RED.toString());
	    } 
	    if (rot.hasPlayer(playerrot)){		                    
	    	playerrot.sendMessage("Du bist schon im Team rot");
	    }
	    else {
	    	rot.addPlayer(playerrot);
	        for(Player p : playerrot.getLocation().getWorld().getPlayers()){
	            p.sendMessage(ChatColor.AQUA + playerrot.getName() + ChatColor.WHITE+	" ist jetzt in Team " + ChatColor.RED + "rot!");
	        }
	    }
	}
	public static void TeamzuweisungBlau (Player playerblau){
	    playerblau.setScoreboard(board);
		if (Vorbereitung == false) {
			playerblau.sendMessage("Bereite ein PvP Spiel vor");
			return;
		}
	    Team blau = board.getTeam("blau");
	    if (blau == null) {
	    	blau = board.registerNewTeam("blau");
	    	blau.setDisplayName("Blau");
	    	blau.setAllowFriendlyFire(false);
	    	blau.setPrefix(ChatColor.BLUE.toString());
	    }
	    if (blau.hasPlayer(playerblau)){		                    
	    	playerblau.sendMessage("Du bist schon im Team blau");
	    }
	    else {
	    	blau.addPlayer(playerblau);
	    	playerblau.setScoreboard(board);
	        for(Player p : playerblau.getLocation().getWorld().getPlayers()){
	        	p.sendMessage(ChatColor.AQUA + playerblau.getName() + ChatColor.WHITE+	" ist jetzt in Team " + ChatColor.BLUE + "blau!");
	        }
	    }
    }

	public static void KampfStarten(Player player){

		plugin.reloadConfig();
		
		if (Vorbereitung == false) {
			player.sendMessage("Bereite ein PvP Spiel vor");
			return;
		}
		if (aktive_arena == "") {
			player.sendMessage("Wähle eine Arena aus");
			return;
		}
		
    	//Mensch_Aerger_Dich
		if (aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich")) {
			Extras_Mensch_Aerger_Dich.Extra_Start(player); 
			return;
		}
		
		
		roteSpieler = false;
		blaueSpieler = false;
		Team blau = board.getTeam("blau");
    	Team rot = board.getTeam("rot");
    	
    	if (blau == null) {
    		player.sendMessage("Team Blau nicht initialisiert");
    		return;
    	}
    	if (rot == null) {
    		player.sendMessage("Team Rot nicht initialisiert");
    		return;
    	}
		
    	if (plugin.getServer().getOnlinePlayers() != null && blau != null && rot != null ){
	    	for(Player p : plugin.getServer().getOnlinePlayers()){
	            if (rot.hasPlayer(p)){
	            	roteSpieler = true;
	            }
	            if (blau.hasPlayer(p)){
	            	blaueSpieler = true;
	            } 
	    	}
    	}
    	if (roteSpieler == false) {
			player.sendMessage("Kein aktiver Spieler im Team Rot");
			return;
		}
		if (blaueSpieler == false) {
			player.sendMessage("Kein aktiver Spieler im Team Blau");
			return;
		}

		
    	//Koordinaten für aktive Arena

		Location LocationBlau = Location_ausConfig(aktive_arena + ".Blau");
		Location LocationRot = Location_ausConfig(aktive_arena + ".Rot");

		if (LocationBlau == null || LocationRot == null){
			player.sendMessage("Daten für die Arena " + aktive_arena + " nicht gefunden");
			return;
		}
		
    	// Spieler Inventar geben und teleportieren
        for(Player p : plugin.getServer().getOnlinePlayers()){
            if (blau.hasPlayer(p)){
            	p.setGameMode(GameMode.getByValue(GamemodeBlau));
            	obj.getScore(p).setScore(0);
            	p.setScoreboard(board);
            	InventarGeben(p);
            	p.setHealth(LebenBlau);
            	p.teleport(LocationBlau);
            	imKampf.add(p.getName());
            }
            if (rot.hasPlayer(p)){
            	p.setGameMode(GameMode.getByValue(GamemodeRot));
            	obj.getScore(p).setScore(0);
            	p.setScoreboard(board);
            	InventarGeben(p);
            	p.setHealth(LebenRot);
            	p.teleport(LocationRot);
            	imKampf.add(p.getName());
            }
        }
        Kampf = true;
        KampfEnde = false;
	}
	public static void KampfBeenden(){

		if (aktive_arena != "" && Kampf == true){
			Kampf = false;
			KampfEnde = true;
	    	Location LocationBack = Location_ausConfig(aktive_arena + ".Back");
	    	for(Player p : plugin.getServer().getOnlinePlayers()){
	    		if (imKampf.contains(p.getName())){
	    			SpielerBeenden(p);
		    		if (LocationBack != null) p.teleport(LocationBack);
		    		p.sendMessage("Kampf beendet");
	    		}
	    	}

		}
		

		
    	aktive_arena = "";
    	aktive_welt = "";
    	Klassenart.clear();
    	Vorbereitung = false;
    	Kampf = false;
	}
	
	public static boolean InventarGeben(Player player){
		
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		Team blau = board.getTeam("blau");
    	Team rot = board.getTeam("rot");
    	
		//Abfragen
 
    	if (blau == null || rot == null) {
    		player.sendMessage("Teams nicht initialisiert");
    		return false;
    	}
		
    	if (!rot.hasPlayer(player) && !blau.hasPlayer(player)){
    		player.sendMessage("Zuerst Team auswählen");
    		return false;
    	}
    	
	    if (!config.isSet(aktive_arena) || aktive_arena == "" ) {
		    player.sendMessage("Arena nicht gefunden");
		    return false;
		}
	    
	    //Inventar und Effekte entfernen
		player.getInventory().clear();
	    for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());

	    //Klasse bestimmen
	    if (Klassenart.get(player.getName()) != null) KlassePlayer = Klassenart.get(player.getName());
	    if (Klassenart.get(player.getName()) == "" || Klassenart.get(player.getName()) == null) {
	    	KlassePlayer = config.getString(aktive_arena + ".Klassen.Standard");
	    	player.sendMessage("Klasse " + ChatColor.AQUA + KlassePlayer + ChatColor.WHITE + " zugewiesen");
	    	Klassenart.put(player.getName(), KlassePlayer);
	    } 
//Erste und Zweite Hand
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".1Hand")) {
	    	player.getInventory().setHeldItemSlot(0);
	    	player.getInventory().setItemInMainHand(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".1Hand")), 1));
	    }
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".2Hand")) player.getInventory().setItemInOffHand(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".2Hand")), 1));

//Tränke
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Traenke")) {
	    	for (String key : config.getConfigurationSection(aktive_arena + ".Klassen." + KlassePlayer + ".Traenke").getKeys(false)) {
	    		ItemStack Trank = new ItemStack(Material.POTION, 1);
	    		PotionMeta meta = (PotionMeta) Trank.getItemMeta();
	    		PotionEffectType Effekt = PotionEffectType.getById(config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Traenke." + key + ".Typ"));
	    		Integer Dauer = config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Traenke." + key + ".Dauer");
	    		Integer Staerke = config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Traenke." + key + ".Staerke");
	    	    meta.addCustomEffect(new PotionEffect(Effekt,Dauer,Staerke), true);
	    	    Trank.setItemMeta(meta);
	            player.getInventory().addItem(Trank);
	    	}
	    }
	    
//Rüstung
	    
	    Color color = Color.GREEN;
	    if (rot.hasPlayer(player)) color = Color.RED;
	    if (blau.hasPlayer(player)) color = Color.BLUE;
	    
	    
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Kopf")) {
	    	if (config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Kopf").equalsIgnoreCase("LEATHER")){
            	ItemStack itemstack = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemstack.getItemMeta();
                itemMeta.setColor(color);
                itemstack.setItemMeta(itemMeta);
                player.getInventory().setHelmet(itemstack);
	    	}
	    	else player.getInventory().setHelmet(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Kopf") + "_HELMET")));
	    }
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Brust")) {
	    	if (config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Brust").equalsIgnoreCase("LEATHER")){
	    		ItemStack itemstack = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemstack.getItemMeta();
                itemMeta.setColor(color);
                itemstack.setItemMeta(itemMeta);
                player.getInventory().setChestplate(itemstack);
	    	}
	    	else player.getInventory().setChestplate(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Brust") + "_CHESTPLATE")));
	    }
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Beine")) {
	    	if (config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Beine").equalsIgnoreCase("LEATHER")){
            	ItemStack itemstack = new ItemStack(Material.LEATHER_LEGGINGS);
                LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemstack.getItemMeta();
                itemMeta.setColor(color);
                itemstack.setItemMeta(itemMeta);
                player.getInventory().setLeggings(itemstack);
	    	}
	    	else player.getInventory().setLeggings(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Beine") + "_LEGGINGS")));
	    }
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Fuesse")) {
	    	if (config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Fuesse").equalsIgnoreCase("LEATHER")){
            	ItemStack itemstack = new ItemStack(Material.LEATHER_BOOTS);
                LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemstack.getItemMeta();
                itemMeta.setColor(color);
                itemstack.setItemMeta(itemMeta);
                player.getInventory().setBoots(itemstack);
                }
	    	else player.getInventory().setBoots(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Ruestung.Fuesse") + "_BOOTS")));
	    }
//weiteres Inventar
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Inventar")) {
	    	for (String key : config.getConfigurationSection(aktive_arena + ".Klassen." + KlassePlayer + ".Inventar").getKeys(false)) {
	    		player.getInventory().addItem(new ItemStack(Material.getMaterial(config.getString(aktive_arena + ".Klassen." + KlassePlayer + ".Inventar." + key + ".Item")), config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Inventar." + key + ".Anzahl")));
	    	}
	    }
	    
//Effekte 
	    if (config.isSet(aktive_arena + ".Klassen." + KlassePlayer + ".Effekte")) {
	    	for (String key : config.getConfigurationSection(aktive_arena + ".Klassen." + KlassePlayer + ".Effekte").getKeys(false)) {
	    		PotionEffectType Effekt = PotionEffectType.getById(config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Effekte." + key + ".Typ"));
	    		Integer Dauer = config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Effekte." + key + ".Dauer");
	    		Integer Staerke = config.getInt(aktive_arena + ".Klassen." + KlassePlayer + ".Effekte." + key + ".Staerke");
	    		player.addPotionEffect(new PotionEffect(Effekt,Dauer,Staerke));
	    	}
	    }
	    return true;
	}
	public static void TeamRemove(Player p){
		Team rot = board.getTeam("rot");
		if (rot != null) {
	        if (rot.hasPlayer(p)){
	        	rot.removePlayer(p);
	        	p.setScoreboard(leeresboard);
	        	board.resetScores(p);
	        }
		}
		Team blau = board.getTeam("blau");
		if (blau != null) {
	        if (blau.hasPlayer(p)){
	        	blau.removePlayer(p);
	        	p.setScoreboard(leeresboard);
	        	board.resetScores(p);
	        }
		}
	}
	public static void AbfrageTeamsVollständig(){
		Team rot = board.getTeam("rot");
		Team blau = board.getTeam("rotblau");
		
		roteSpieler = false;
		blaueSpieler = false;
    	if (Kampf = false) return;
		if (rot != null && blau != null){
	    	if (plugin.getServer().getOnlinePlayers() != null){
		    	for(Player player : plugin.getServer().getOnlinePlayers()){
		            if (rot.hasPlayer(player)){
		            	roteSpieler = true;
		            }
		            if (blau.hasPlayer(player)){
		            	blaueSpieler = true;
		            } 
		    	}
	    	}
	    	
	    	if (roteSpieler == false) {
	        	for(Player player : plugin.getServer().getOnlinePlayers()){
	    			player.sendMessage("Kein Spieler mehr im Team Rot");
	    			player.sendMessage(ChatColor.BLUE + "Team Blau hat gewonnen!");
	    		}
	        	if (aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich")){
	        		Extras_Mensch_Aerger_Dich.Extra_Ende();
	        	}
	        	else KampfBeenden();
	        	
	    	}
	    		
	    	if (blaueSpieler == false) {
	    		for(Player player : plugin.getServer().getOnlinePlayers()){
					player.sendMessage("Kein Spieler mehr im Team Blau");
					player.sendMessage(ChatColor.RED + "Team Rot hat gewonnen!");
	    		}
	        	if (aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich")){
	        		Extras_Mensch_Aerger_Dich.Extra_Ende();
	        	}
	        	else KampfBeenden();
	        }
		}
	}
	
	public static void SpielerBeenden(Player player){
    	
    	player.setHealth(LebenBack);
		player.getInventory().clear();

		if (Playerleft == false){
		player.setScoreboard(leeresboard);
		TeamRemove(player);
		player.setGameMode(GameMode.getByValue(GamemodeBack));
		}
		board.resetScores(player);
		AbfrageTeamsVollständig();
		imKampf.remove(player.getName());
		

	}
	
	public static Location Location_ausConfig(String Objekt){

		plugin.reloadConfig();
    	FileConfiguration config = plugin.getConfig();
	    if (!plugin.getConfig().isSet(aktive_arena)) {
	    	return null;
		}
	    String strworld = (String) config.getString(Objekt + ".world");
	    org.bukkit.World world = Bukkit.getWorld(strworld);
	    double x = (double) config.getDouble(Objekt+".x");
	    double y = (double) config.getDouble(Objekt+".y");
	    double z = (double) config.getDouble(Objekt+".z");
	    float yaw  = (float) 0;
	    float pitch  = (float) 0;
	    yaw = (float) config.getDouble(Objekt+".yaw");
	    pitch = (float) config.getDouble(Objekt+".pitch");
	    Location Location = new Location(world,x,y,z,yaw,pitch);
		return Location;
	}
	public static Location Location_ausConfig_ohneWinkel(String Objekt){

		plugin.reloadConfig();
    	FileConfiguration config = plugin.getConfig();
	    if (!plugin.getConfig().isSet(aktive_arena)) {
	    	return null;
		}
	    String strworld = (String) config.getString(Objekt + ".world");
	    org.bukkit.World world = Bukkit.getWorld(strworld);
	    double x = (double) config.getDouble(Objekt+".x");
	    double y = (double) config.getDouble(Objekt+".y");
	    double z = (double) config.getDouble(Objekt+".z");
	    Location Location = new Location(world,x,y,z);
		return Location;
	}
}
