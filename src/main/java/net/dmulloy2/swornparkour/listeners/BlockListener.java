package net.dmulloy2.swornparkour.listeners;

import net.dmulloy2.swornparkour.SwornParkour;
import net.dmulloy2.swornparkour.parkour.objects.ParkourField;
import net.dmulloy2.swornparkour.parkour.objects.ParkourSign;
import net.dmulloy2.swornparkour.parkour.objects.ParkourZone;
import net.dmulloy2.swornparkour.permissions.Permission;
import net.dmulloy2.swornparkour.util.FormatUtil;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener implements Listener
{
	private final SwornParkour plugin;
	public BlockListener(final SwornParkour plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		Location loc = block.getLocation();
		
		for (ParkourZone zone : plugin.loadedArenas)
		{
			ParkourField field = zone.getField();
			if (field.isInside(loc))
			{
				Player player = event.getPlayer();
				if (plugin.getParkourManager().isInParkour(player))
				{
					String message = "&cYou cannot break blocks while in parkour!";
					player.sendMessage(FormatUtil.format(message));
					event.setCancelled(true);
					return;
				}
				
				if (!plugin.getPermissionHandler().hasPermission(player, Permission.BUILD))
				{
					String message = "&cYou do not have permission to edit parkour arenas!";
					player.sendMessage(FormatUtil.format(message));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		Location loc = block.getLocation();
		
		for (ParkourZone zone : plugin.loadedArenas)
		{
			ParkourField field = zone.getField();
			if (field.isInside(loc))
			{
				Player player = event.getPlayer();
				if (plugin.getParkourManager().isInParkour(player))
				{
					String message = "&cYou cannot place blocks while in parkour!";
					player.sendMessage(FormatUtil.format(message));
					event.setCancelled(true);
					return;
				}
				
				if (!plugin.getPermissionHandler().hasPermission(player, Permission.BUILD))
				{
					String message = "&cYou do not have permission to edit parkour arenas!";
					player.sendMessage(FormatUtil.format(message));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSignChange(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[SwornParkour]"))
		{
			if (plugin.getPermissionHandler().hasPermission(event.getPlayer(), Permission.BUILD))
			{
				if (event.getLine(1).equalsIgnoreCase("Click to join"))
				{
					if (event.getLine(2).equalsIgnoreCase("Auto assign"))
					{
						return;
					}
					
					int id = Integer.parseInt(event.getLine(2));
					ParkourZone pz = plugin.getParkourZone(id);
					if (pz != null)
					{
						ParkourSign sign = new ParkourSign(plugin, event.getBlock().getLocation(), pz, plugin.signs.size());
						plugin.signs.add(sign);
						sign.update();
						
						plugin.getFileHelper().updateSignSave();
						
						event.getPlayer().sendMessage(FormatUtil.format("&eCreated new Join Sign!"));
					}
					else
					{
						event.setLine(0, FormatUtil.format("[SwornParkour]"));
						event.setLine(1, FormatUtil.format("&4Invalid Game"));
						event.setLine(2, "");
						event.setLine(3, "");
					}
				}
			}
			else
			{
				event.setLine(0, FormatUtil.format("[SwornParkour]"));
				event.setLine(1, FormatUtil.format("&4No permission"));
				event.setLine(2, "");
				event.setLine(3, "");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (block.getState() instanceof Sign)
		{
			Sign s = (Sign)block.getState();
			if (s.getLine(0).equalsIgnoreCase("[SwornParkour]"))
			{
				ParkourSign sign = plugin.getParkourSign(block.getLocation());
				if (sign != null)
				{
					if (plugin.getPermissionHandler().hasPermission(player, Permission.BUILD))
					{
						plugin.removeSign(sign);
						player.sendMessage(FormatUtil.format("&eDeleted Join sign!"));
					}
					else
					{
						event.setCancelled(true);
						player.sendMessage(FormatUtil.format("&cPermission denied!"));
					}
				}
			}
		}
	}
}