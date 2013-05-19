package co.d3s.ylt.chestplate.plate.object;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import co.d3s.ylt.chestplate.ChestPlate;
import co.d3s.ylt.chestplate.plate.CP_Return;

public class CP_Sorter extends CP_Object {
	public CP_Sorter(ChestPlate chestPlate) {
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
		if (mat == Material.SIGN_POST)
			return true;
		return false;
	}

    private Block findContainer(Block sign) {

        Material m = sign.getType();
        MaterialData md = m.getNewData(sign.getData());

        if (!(md instanceof org.bukkit.material.Sign))
            return null;

        org.bukkit.material.Sign s = (org.bukkit.material.Sign)md;

        Block current = sign;
        current = current.getRelative(BlockFace.DOWN);
        current = current.getRelative(s.getFacing());

        if (isContainer(current))
        {
            return current;
        }

        return null;
    }
	
	public CP_Return interact(Block plate, Entity entity) {
		if (!(/*entity instanceof ExperienceOrb || */ entity instanceof Item)) {
			return CP_Return.pass;
		}
		
		Block block = find(plate);
		if (block == null)
			return CP_Return.pass;
		
		BlockState blockstate = block.getState();
		if (!(blockstate instanceof Sign))
			return CP_Return.pass;

				
		Item item = (Item)entity;
		ItemStack stack = item.getItemStack();
		
		Byte data = block.getData();
		double srad = Math.PI*(((2F/16F*data.doubleValue()))+0.5);
		
		//double srad = Math.toRadians(((360F/16F)*(data.doubleValue()))+90);
		//System.out.println("angle "+srad+" "+Math.cos(srad)+", "+Math.sin(srad));
		
		Sign sign = (Sign)block.getState();
		
		
		Vector a = item.getVelocity();
		double angle = Math.atan2(a.getZ(), a.getX());
		boolean pulse = false;
		boolean redirect = false;
		
		String[] lines = sign.getLines();
		for(String line : lines) {
            for (String str : line.split(",") ) {
                str = str.trim();
                boolean negate = false;
                if (str.startsWith("-")) {
                    negate = true;
                    str = str.substring(1);
                }
                boolean match = cp.itemmatch.Type_Match(str.toUpperCase(), stack.getTypeId(), stack.getData().getData());
                if (match) {
                    redirect = !negate;
                }
            }
		}

        Block container = findContainer(block);
        if (container != null && redirect == true) {
            return pickup(plate, container, item);
        }
		if (redirect == true) {
			angle = srad;
			pulse = true;
        }


		/*{
			Location loc = item.getLocation();
			double nx = block.getX()-0.5+Math.cos(angle);
			double ny = block.getY()-0.5+Math.sin(angle);
			angle = Math.atan2(ny-loc.getY(), nx-loc.getX());
		}*/
		
		a.setX(Math.cos(angle)*0.25);
		a.setZ(Math.sin(angle)*0.25);
		
		a.setY(0.05);
		
		item.setVelocity(a);
		if (pulse == true) {
			depressPlate(plate);
			return CP_Return.done;
		}
		return CP_Return.cancel;
	}
}


