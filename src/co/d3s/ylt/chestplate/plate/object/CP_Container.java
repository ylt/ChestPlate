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
        return isContainer(block);
    }
    
    @Override
    public CP_Return interact(Block plate, Entity entity) {

        if ((entity instanceof ExperienceOrb))
            slidePickup(entity, 0.25);
        if (!(entity instanceof Item))
            return CP_Return.pass;

        Block container = find(plate);
        if (container == null)
            return CP_Return.pass;

        return pickup(plate, container, (Item)entity);
    }
}
