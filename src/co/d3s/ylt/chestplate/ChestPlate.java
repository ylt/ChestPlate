package co.d3s.ylt.chestplate;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.PressurePlate;
import org.bukkit.plugin.java.JavaPlugin;

import co.d3s.ylt.chestplate.plate.CP_Return;
import co.d3s.ylt.chestplate.plate.object.CP_Container;
import co.d3s.ylt.chestplate.plate.object.CP_Lift;
import co.d3s.ylt.chestplate.plate.object.CP_Object;
import co.d3s.ylt.chestplate.plate.object.CP_Sorter;
import co.d3s.ylt.chestplate.util.ItemMatcher;

public class ChestPlate extends JavaPlugin implements Listener {
	
	public CP_Object[] objects = {
		new CP_Sorter(this),
		new CP_Lift(this),
		new CP_Container(this)
	};
	
	public ItemMatcher itemmatch;
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		itemmatch = new ItemMatcher(this);
	}
	public void onDisable() {
		
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getClickedBlock().getState() instanceof InventoryHolder) {
					if (event.getMaterial() == Material.WOOD_PLATE && event.getBlockFace() == BlockFace.UP) {
						event.setUseItemInHand(Result.ALLOW);
						event.setUseInteractedBlock(Result.DENY);
					}
					/* Didn't work - couldn't edit text
					 * if ((event.getMaterial() == Material.WALL_SIGN || event.getMaterial() == Material.SIGN) &&
							(event.getBlockFace() == BlockFace.NORTH |
							event.getBlockFace() == BlockFace.EAST |
							event.getBlockFace() == BlockFace.SOUTH |
							event.getBlockFace() == BlockFace.WEST)) {
						event.setUseItemInHand(Result.ALLOW);
						event.setUseInteractedBlock(Result.DENY);
					}*/
				}
			}
			if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null) {
				onInteract(event.getPlayer(), event.getClickedBlock(), event);
			}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityInteract(EntityInteractEvent event) {
		onInteract(event.getEntity(), event.getBlock(), event);
	}
	
	public void onInteract(Entity entity, Block plate, Cancellable event) {		
		if (plate.getType() != Material.WOOD_PLATE)
			return;
		out:
		for (CP_Object cpo : objects) {
			CP_Return ret = cpo.interact(plate, entity);
			switch(ret) {
				case cancel:
					event.setCancelled(true);
				case done:
					break out;
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockCanBuild(BlockCanBuildEvent event) {
		if (event.isBuildable() == false && event.getMaterial() == Material.WOOD_PLATE) {
			if (event.getBlock().getRelative(BlockFace.DOWN).getState() instanceof InventoryHolder) {
				event.setBuildable(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled() == true) return;
		
		Block block = event.getBlock();
		if (block.getType() == Material.WOOD_PLATE) {
		//if (event.getChangedType() == Material.WOOD_PLATE)
			if (block.getRelative(BlockFace.DOWN).getState() instanceof InventoryHolder) {
				event.setCancelled(true);
			}
		}
	}
}