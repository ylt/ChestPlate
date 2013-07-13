package co.d3s.ylt.chestplate.plate.object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.material.PressurePlate;

import co.d3s.ylt.chestplate.ChestPlate;
import co.d3s.ylt.chestplate.plate.CP_Event;
import co.d3s.ylt.chestplate.plate.CP_Return;

public class CP_Lift extends CP_Object  {
    public CP_Lift(ChestPlate chestPlate) {
        super(chestPlate);
    }

    public Block find(Block block) {
        Block current;
        current = block.getRelative(BlockFace.UP);
        if (check(current))
            return current;

        return null;
    }
    
    public boolean check(Block block) {
        Material mat = block.getType();
        if (mat == Material.SAND)
            return true;
        return false;
    }
    
    public CP_Return interact(Block plate, Entity entity) {
        if (!(/*entity instanceof ExperienceOrb || */ entity instanceof Item)) {
            return CP_Return.pass;
        }
        
        Block block = find(plate);
        if (block == null)
            return CP_Return.pass;
        
        BlockState blockstate = block.getState();
        //if (!(blockstate instanceof SAND))
        //    return CP_Return.pass;
        
        Item item = (Item)entity;
        
        Block cblock = block;
        for (int i = 0; i < 5 && cblock.getType() == Material.SAND; i++) {
            cblock = cblock.getRelative(BlockFace.UP);
        }
        
        Location loc = item.getLocation();
        loc.setY(cblock.getLocation().getY()+0.5);
        
        item.teleport(loc);
        
        depressPlate(plate);
        return CP_Return.pass;
    }

}
