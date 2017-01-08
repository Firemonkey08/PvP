package me.luc.pvp;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public class Command implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args){

		if(cmd.getName().equalsIgnoreCase("pvp")){ // Wenn der Spieler /addsoundzeiten eingibt, dann 
			Methoden.pvpVorbereiten(Bukkit.getPlayer(sender.getName()));
		}
		return false;
	}


}
