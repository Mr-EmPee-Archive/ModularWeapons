package ml.empee.modularweapons.controllers;

import lombok.RequiredArgsConstructor;
import ml.empee.commandsManager.command.CommandExecutor;
import ml.empee.commandsManager.command.annotations.CommandNode;
import ml.empee.ioc.annotations.Bean;
import ml.empee.modularweapons.ModularWeapons.Permissions;

/** Controller used for managing the plugin. **/

@Bean
@RequiredArgsConstructor
@CommandNode(label = "mw", permission = Permissions.ADMIN, exitNode = false)
public class PluginController extends CommandExecutor {


}
