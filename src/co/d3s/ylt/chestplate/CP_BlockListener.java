package co.d3s.ylt.chestplate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CP_BlockListener extends BlockListener {
	public ChestPlate cp = null;
	public CP_BlockListener(ChestPlate cp) {
		this.cp = cp;
	}
	public void onBlockCanBuild(BlockCanBuildEvent event) {
		if (event.isBuildable() == false && event.getMaterial() == Material.WOOD_PLATE) {
			if (event.getBlock().getRelative(BlockFace.DOWN).getState() instanceof ContainerBlock) {
				event.setBuildable(true);
			}
		}
	}
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled() == true) return;
		
		Block block = event.getBlock();
		if (block.getType() == Material.WOOD_PLATE) {
		//if (event.getChangedType() == Material.WOOD_PLATE)
			if (block.getRelative(BlockFace.DOWN).getState() instanceof ContainerBlock) {
				event.setCancelled(true);
			}
		}
	}
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		//event.getBlock().getRelative(BlockFace.UP).setType(Material.SPONGE);
		Block rblock = event.getBlock();
		BlockFace faces[] = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(BlockFace face : faces) {
			Block block = rblock.getRelative(face);
			
			if (block.getType() != Material.FURNACE) continue;
			
			System.out.println(block+" powered: "+block.isBlockPowered());
			if (!block.isBlockPowered()) {
				
				Inventory inv = ((ContainerBlock)block.getState()).getInventory();
				
				byte d = block.getData();
				
				byte b0 = 0;
				byte b1 = 0;
				
				if (d == 3) b1 = 1;
				else if (d == 2) b1 = -1;
				else if (d == 5) b0 = 1;
				else d = -1;
				
				Vector correct = new Vector(b0 * 0.6D + 0.5D,0.5D,(double) b1 * 0.6D + 0.5D);
				
				
				ItemStack is = inv.getItem(2);
				if (is.getType() == Material.AIR) continue;
				
				if (is.getAmount() > 0) {
					is.setAmount(is.getAmount()-1);
					
					ItemStack p = new ItemStack(is.getType(),1);
					Vector vec = new Vector((double)b0*1.0D, 0, (double)b1*1.0D);
					Location loc = block.getLocation().add(correct);
					Item item = block.getWorld().dropItem(loc, p);
					item.setVelocity(vec);
				}
			}
		}
	}
}

