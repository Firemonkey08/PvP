package me.luc.pvp;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;

@SuppressWarnings("deprecation")
public class ClickSign implements Listener {

	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) throws MaxChangedBlocksException, DataException, IOException{
    	// bei Rechtsklick
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_AIR){
	        if (Methoden.aktive_arena.equalsIgnoreCase("Mensch_Aerger_Dich") && Methoden.Kampf == true) {
	        	if  (player.getInventory().getItemInHand().getType().equals(Material.SPIDER_EYE)){
	        		Extras_Mensch_Aerger_Dich.Glowing();
	        	}
	        }
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            //Speichere clickedBlock in eine Variable (i)
            Block i = event.getClickedBlock();
            //Check Schild
            if(i.getState() instanceof Sign)
            {
                BlockState stateBlock = i.getState();
                Sign sign = (Sign) stateBlock;
                
//Dropper
                if(sign.getLine(0).equalsIgnoreCase("Dropper")){       
	                Extras instance = new Extras();
	                instance.Dropper(sign.getLine(1), sign.getLine(2), sign.getLine(3), player, i);
                }		
                
//enchant
                if(sign.getLine(0).equalsIgnoreCase("Verzauberer")){  
	                Extras instance = new Extras();
	                instance.Verzauberer(sign.getLine(1), sign.getLine(2), sign.getLine(3), player);
                }		
                
                //Check PVP
                if(!sign.getLine(0).equalsIgnoreCase("PvP")) return;
                
//Arena auswählen
                if(sign.getLine(1).equalsIgnoreCase("Arena")){
                	Methoden.ArenaAuswaehlen(sign.getLine(2), player);
                }
//Vorbereitungen 
                if(sign.getLine(1).equalsIgnoreCase("Vorbereitungen")){
                	Methoden.pvpVorbereiten(player);
                }
//Teamzuweisung ROT
                if(sign.getLine(1).equalsIgnoreCase("rotes Team")){
                    Methoden.TeamzuweisungRot(player);
                }
//Teamzuweisung BLAU
                if(sign.getLine(1).equalsIgnoreCase("blaues Team")){
                    Methoden.TeamzuweisungBlau(player);
                }
//Start                 
                if(sign.getLine(1).equalsIgnoreCase("Kampf starten")){
                	Methoden.KampfStarten(player);
                }
                
//Beenden      
                if(sign.getLine(1).equalsIgnoreCase("Kampf beenden")){
                	Methoden.KampfBeenden();
                }
//Klassenauswahl
                if(sign.getLine(1).equalsIgnoreCase("Klasse")){
            	    if (Methoden.aktive_arena == "" ){         		    
            	    	player.sendMessage("Zuerst Arena auswählen");
            	    	return;
            	    }
                	Methoden.Klassenart.put(player.getName(), sign.getLine(2));
                	if (Methoden.InventarGeben(player)) player.sendMessage("Klasse " + ChatColor.AQUA + sign.getLine(2)+ ChatColor.WHITE + " ausgewählt");
                }              

//andere                
                if(sign.getLine(1).equalsIgnoreCase("")){ 
                }
            }
            else{
            }
        }
	}
}
