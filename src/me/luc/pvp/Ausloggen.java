package me.luc.pvp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

public class Ausloggen implements Listener {
	
	Team blau = Methoden.board.getTeam("blau");
	Team rot = Methoden.board.getTeam("rot");
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
		if (Methoden.imKampf.contains(event.getPlayer().getName())){
			Methoden.Playerleft = true;
			Methoden.SpielerBeenden(event.getPlayer());
			Methoden.Playerleft = false;
	    }
	}
	
    public void onChangeWorld(PlayerChangedWorldEvent event){
		if (event.getPlayer().getWorld().getName() != Methoden.aktive_welt){
			if (Methoden.imKampf.contains(event.getPlayer().getName())){
				Methoden.SpielerBeenden(event.getPlayer());
			}
		}
    }
}

