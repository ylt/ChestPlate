package co.d3s.ylt.chestplate.plate.object;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import co.d3s.ylt.chestplate.ChestPlate;
import co.d3s.ylt.chestplate.plate.CP_Event;
import co.d3s.ylt.chestplate.plate.CP_Return;

public class CP_Container extends CP_Object {
	public CP_Container(ChestPlate cp) {
		super(cp);
	}

	public Block find(Block block) {
		Block current;
		
		current = block.getRelative(BlockFace.UP);
		if (check(current))
			return current;
		
		current = block.getRelative(BlockFace.DOWN);
		if (check(current))
			return current;
		return null;
	}
	
	public boolean check(Block block) {
		Material mat = block.getType();
		if (mat == Material.CHEST || mat == Material.DISPENSER)
			return true;
		return false;
	}
	
	@Override
	public CP_Return interact(Block plate, Entity entity) {
		
		if ((entity instanceof ExperienceOrb))
			slidePickup(entity, 0.25);
		if (!(entity instanceof Item))
			return CP_Return.pass;
		
		Block block = find((Block) plate);
		if (block == null)
			return CP_Return.pass;
			
		BlockState blockstate = block.getState();
		if (!(blockstate instanceof InventoryHolder))
			return CP_Return.pass;

		
		
		Item item = (Item)entity;
		ItemStack stack = item.getItemStack();
		Inventory inv = ((InventoryHolder)blockstate).getInventory();
		
		HashMap<Integer, ItemStack> ret = inv.addItem(stack);
		if (ret.size() == 0) {
			stack.setAmount(0);
			item.remove();
			//this.depressPlate(block);
			//return CP_Return.done;
		}
		else {
			int total = 0;
			for (ItemStack s : ret.values()) {
				total += s.getAmount();
			}
			stack.setAmount(total);
			item.setItemStack(stack);
			if (total == stack.getAmount()) {
				slidePickup(item, 0.25);
				return CP_Return.cancel;
			}
		}
		depressPlate(plate);
		
		slidePickup(item, 0.25);
		return CP_Return.done;
	}
}
