package me.luc.pvp;

import org.bukkit.scheduler.BukkitRunnable;

public class �berpr�fung_Ende extends BukkitRunnable {
		
	public void run() {
				
		if (Methoden.Kampf == false && Methoden.KampfEnde == true) Methoden.KampfEnde = false;
	}
}
