package co.d3s.ylt.chestplate;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ContainerBlock;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class CP_PlayerListener extends PlayerListener {
	public ChestPlate cp = null;
	public CP_PlayerListener(ChestPlate cp) {
		this.cp = cp;
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				event.getMaterial() == Material.WOOD_PLATE &&
				event.getBlockFace() == BlockFace.UP &&
				event.getClickedBlock().getState() instanceof ContainerBlock) {
				event.setUseItemInHand(Result.ALLOW);
				event.setUseInteractedBlock(Result.DENY);
			}
	}
}