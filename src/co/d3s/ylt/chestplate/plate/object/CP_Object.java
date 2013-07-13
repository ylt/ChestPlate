package co.d3s.ylt.chestplate.plate.object;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.PressurePlate;
import org.bukkit.util.Vector;

import co.d3s.ylt.chestplate.ChestPlate;
import co.d3s.ylt.chestplate.plate.CP_Event;
import co.d3s.ylt.chestplate.plate.CP_Return;

public abstract class CP_Object {
    public ChestPlate cp;
    public CP_Object(ChestPlate cp) {
        this.cp = cp;
    }
    
    public abstract Block find(Block block);
    public abstract boolean check(Block block);
    public abstract CP_Return interact(Block block, Entity item);
    
    public void slidePickup(Entity item, double velocity) {
        Vector a = item.getVelocity();
        
        double angle = Math.atan2(a.getX(), a.getZ());
        
        a.setX(Math.sin(angle)*velocity);
        a.setZ(Math.cos(angle)*velocity);
        
        a.setY(0.05);
        
        item.setVelocity(a);
    }
    
    public void depressPlate(Block block) {
        //*MAY* be inefficient, although unsure yet
        
        cp.getServer().getScheduler().scheduleSyncDelayedTask(cp, new Plate_Depress(block.getLocation()), 5);
    }

    protected boolean isContainer(Block block) {
        Material mat = block.getType();
        switch (mat) {
            case CHEST:
            case DISPENSER:
            case DROPPER:
            case HOPPER:
                return true;
        }
        return false;
    }

    protected CP_Return pickup(Block plate, Block container, Item item) {
        BlockState blockstate = container.getState();
        if (!(blockstate instanceof InventoryHolder))
            return CP_Return.pass;
        
        ItemStack stack = item.getItemStack();
        Inventory inv = ((InventoryHolder)blockstate).getInventory();
        
        HashMap<Integer, ItemStack> ret = inv.addItem(stack);
        if (ret.size() == 0) {
            stack.setAmount(0);
            item.remove();
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

class Plate_Depress implements Runnable {
    Location loc;
    public Plate_Depress(Location loc) {
        this.loc = loc;
    }

    public void run() {
        Block plate = loc.getBlock();
        if (plate.getType() == Material.WOOD_PLATE) {
            plate.setData((byte) 0);
        }
    }
}
