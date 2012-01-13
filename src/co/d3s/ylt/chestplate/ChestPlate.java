package co.d3s.ylt.chestplate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestPlate extends JavaPlugin {
	public CP_EntityListener el = new CP_EntityListener(this);
	public CP_PlayerListener pl = new CP_PlayerListener(this);
	public CP_BlockListener bl = new CP_BlockListener(this);
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		getServer().getPluginManager().registerEvent(Type.ENTITY_INTERACT , el, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT , pl, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Type.BLOCK_CANBUILD  , bl, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Type.BLOCK_PHYSICS   , bl, Priority.Highest, this);
		//getServer().getPluginManager().registerEvent(Type.REDSTONE_CHANGE , bl, Priority.Highest, this);
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
	    Thread t = Thread.currentThread();
	    System.out.println("current thread: " + t);
	    int active = Thread.activeCount();
	    System.out.println("currently active threads: " + active);
	    Thread all[] = new Thread[active];
	    Thread.enumerate(all);
	    for (int i = 0; i < active; i++) {
	      System.out.println(i + ": " + all[i]);
	    }
	    //Thread.dumpStack();
	    
		return true;
	}
}
