package co.d3s.ylt.chestplate;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
//import org.bukkit.block.Chest;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CP_EntityListener extends EntityListener {
	public ChestPlate cp = null;
	public CP_EntityListener(ChestPlate cp) {
		this.cp = cp;
	}
	
	public boolean checkBlock(Block block) {
		if (block.getType() == Material.CHEST)
			return true;
		else if (block.getType() == Material.DISPENSER)
			return true;
		else if (block.getType() == Material.SIGN_POST)
			return true;
		else if (block.getType() == Material.SAND)
			return true;
		return false;
	}
	
	public void onEntityInteract(EntityInteractEvent event) {
		Block plate = event.getBlock();
		//Location loc = plate.getLocation();
		Entity entity = event.getEntity();
		
		if (plate.getType() != Material.WOOD_PLATE) { return; }
		
		Item item;
		ItemStack stack;
		
		Block block = plate.getRelative(BlockFace.UP);
		if (checkBlock(block)) {
			event.setCancelled(true);
			if (!(entity instanceof Item)) { return; }
			item = (Item)entity;
			stack = item.getItemStack();
			
			
			if (block.getType() == Material.SIGN_POST) {
				Byte data = block.getData();
				double srad = Math.PI*(((2F/16F*data.doubleValue()))+0.5);
				//double srad = Math.toRadians(((360F/16F)*(data.doubleValue()))+90);
				//System.out.println("angle "+srad+" "+Math.cos(srad)+", "+Math.sin(srad));
				
				Sign sign = (Sign)block.getState();
				String line = sign.getLine(0);
				
				
				Vector a = item.getVelocity();
				double angle = Math.atan2(a.getZ(), a.getX());
				
				try {
					int itemid = Integer.parseInt(line);
					
					if (itemid == stack.getTypeId()) {
						angle = srad;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					return;
				}
				
				a.setX(Math.cos(angle)*0.25);
				a.setZ(Math.sin(angle)*0.25);
				
				a.setY(0.05);
				
				item.setVelocity(a);
				return;
			}
			else if (block.getType() == Material.SAND) {
				Block cblock = block;
				for (int i = 0; i < 5 && cblock.getType() == Material.SAND; i++) {
					cblock = cblock.getRelative(BlockFace.UP);
				}
				
				Location loc = item.getLocation();
				loc.setY(cblock.getLocation().getY()+0.5);
				
				item.teleport(loc);
				return;
			}
		}
		else {
			block = plate.getRelative(BlockFace.DOWN);
			if (!checkBlock(block)) {
				return;
			}
			
			event.setCancelled(true);
			if (!(entity instanceof Item)) { return; }
			item = (Item)entity;
			stack = item.getItemStack();
			
		}
		
		
		
		if (block.getType() == Material.FURNACE) {
			/*Inventory inv = ((ContainerBlock)block.getState()).getInventory();
			System.out.println("Furnace");
			int islot = 1;
			if (plate.getY() > block.getY()) islot = 0; //above
			ItemStack slot = inv.getItem(islot);
			
			System.out.println(inv.getItem(0)+", "+inv.getItem(1)+", "+inv.getItem(2));
			if (slot.getType() == Material.AIR) slot.setType(stack.getType());
			if (slot.getType() == Material.AIR || slot.getType() == stack.getType()) {
				int vol = slot.getAmount()+stack.getAmount();
				if (vol <= slot.getMaxStackSize()) {
					slot.setAmount(vol);
					stack.setAmount(0);
					item.remove();
					
					inv.setItem(islot, slot);
					return;
				}
				else {
					slot.setAmount(slot.getMaxStackSize());
					stack.setAmount(vol-slot.getMaxStackSize());
					
					inv.setItem(islot, slot);
				}
			}*/
			
			Vector a = item.getVelocity();
			
			double angle = Math.atan2(a.getX(), a.getZ());
			
			a.setX(Math.sin(angle)*0.25);
			a.setZ(Math.cos(angle)*0.25);
			
			a.setY(0.05);
			
			item.setVelocity(a);
			
		}
		else {
			Inventory inv = ((ContainerBlock)block.getState()).getInventory();
			HashMap<Integer, ItemStack> ret = inv.addItem(stack);
			if (ret.size() == 0) {
				stack.setAmount(0);
				item.remove();
				return;
			}
			else {
				int total = 0;
				for (ItemStack s : ret.values()) {
					total += s.getAmount();
				}
				stack.setAmount(total);
				item.setItemStack(stack);
			}
			
			Vector a = item.getVelocity();
			
			double angle = Math.atan2(a.getX(), a.getZ());
			
			a.setX(Math.sin(angle)*0.25);
			a.setZ(Math.cos(angle)*0.25);
			
			a.setY(0.05);
			
			item.setVelocity(a);
		}
	
		
	}
	public void message(Location loc, String message) {
		Player[] players = cp.getServer().getOnlinePlayers();
		for (Player player : players) {
			if (loc.distance(player.getLocation()) < 100) {
				player.sendMessage(message);
			}
		}
	}
}
