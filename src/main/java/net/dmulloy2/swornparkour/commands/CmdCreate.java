package net.dmulloy2.swornparkour.commands;

import net.dmulloy2.swornparkour.SwornParkour;
import net.dmulloy2.swornparkour.permissions.Permission;

/**
 * @author dmulloy2
 */

public class CmdCreate extends SwornParkourCommand
{
	public CmdCreate(SwornParkour plugin)
	{
		super(plugin);
		this.name = "create";
		this.aliases.add("c");
		this.description = "Creates an arena";
		this.permission = Permission.CMD_CREATE;
		
		this.mustBePlayer = true;
	}
	
	@Override
	public void perform()
	{
		if (getManager().isCreatingArena(player))
		{
			err("&cYou are already creating an arena!");
			return;
		}
		
		if (getManager().isInParkour(player))
		{
			err("&cYou cannot create an arena while ingame!");
			return;
		}
		
		getManager().createNewParkourGame(player);
	}
}