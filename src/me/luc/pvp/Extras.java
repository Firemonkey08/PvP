package me.luc.pvp;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class Extras {

	public static HashMap<Location, Boolean> Gedrückt = new HashMap<Location, Boolean>();
	
	public void Dropper(String Itemart, String Dauer, String Anzahl, Player player, Block Schild){
		
    	if( Material.getMaterial(Itemart) == null || !Extras.isInt(Dauer) || !Extras.isInt(Anzahl)){
    		player.sendMessage("Schild falsch beschriftet!");
    		return;
    	}
    	
        org.bukkit.material.Sign s = (org.bukkit.material.Sign) Schild.getState().getData();
        Location locitem = Schild.getRelative(s.getFacing(), 2).getLocation();
        ItemStack item = new ItemStack(Material.getMaterial(Itemart), Integer.parseInt(Anzahl));
        
		if (!Gedrückt.containsKey(locitem)) Gedrückt.put(locitem, false);
		if (Gedrückt.get(locitem) == false){
			Gedrückt.put(locitem, true);
	    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
	    		if (Methoden.imKampf.contains(p.getName())){
	    			p.sendMessage(Itemart + " werden in " + Dauer + " Sekunden gedropped");
	    		}
	    	}
		    Bukkit.getScheduler().scheduleSyncDelayedTask(pvp.getInstance(), new Runnable() {
		        public void run() {
	        		Gedrückt.put(locitem, false);
		    		World world = locitem.getWorld();
		    		world.dropItem(locitem, item);
			    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
			    		if (Methoden.imKampf.contains(p.getName())){
			    			p.sendMessage(Itemart + " wurden gedropped");
			    		}
			    	}
		        }
		    }, Long.parseLong(Dauer) * 20L);
		}
	}
	
	public void Verzauberer(String Itemart, String ID, String Staerke, Player player){
	
		if( Material.getMaterial(Itemart) == null || !Extras.isInt(ID) || !Extras.isInt(Staerke)){
			player.sendMessage("Schild falsch beschriftet!");
			return;
		}
			for(int x = 0; x < player.getInventory().getContents().length; x++)
		{
			ItemStack item = player.getInventory().getItem(x);
			if(item != null)
			{
				if(item.getType() == Material.getMaterial(Itemart))
				{
					item.addEnchantment(Enchantment.getById(Integer.parseInt(ID)), Integer.parseInt(Staerke));
		    		for(Player p : Bukkit.getServer().getOnlinePlayers()){
		    			if (Methoden.imKampf.contains(p.getName())){
		    				p.sendMessage(Itemart + " wurde verzaubert");
		    			}
		    		}
				}
			}
		}
	}
		
		
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
