package net.dmulloy2.swornparkour.commands;

import java.util.List;

import net.dmulloy2.swornparkour.SwornParkour;

import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class CmdClaim extends SwornParkourCommand
{
	public CmdClaim(SwornParkour plugin)
	{
		super(plugin);
		this.name = "claim";
		this.description = "Claim parkour rewards";
		
		this.mustBePlayer = true;
	}
	
	@Override
	public void perform()
	{
		if (!getManager().redemption.containsKey(player.getName()))
		{
			err("&cYou have no rewards to claim!");
			return;
		}
		
		sendMessage("&eClaiming rewards...");
		
		List<ItemStack> items = getManager().redemption.get(player.getName());
		
		for (ItemStack item : items)
		{
			player.getInventory().addItem(item);
		}
		
		getManager().redemption.remove(player.getName());
	}
}