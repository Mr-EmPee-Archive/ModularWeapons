package ml.empee.modularweapons;

import java.io.File;
import lombok.Getter;
import ml.empee.commandsManager.CommandManager;
import ml.empee.commandsManager.command.CommandExecutor;
import ml.empee.ioc.SimpleIoC;
import ml.empee.modularweapons.controllers.PluginController;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

/** Boot class of this plugin. **/

public final class ModularWeapons extends JavaPlugin {

  public static final String PREFIX = "  &5MW &8»&r ";
  private static final String SPIGOT_PLUGIN_ID = "";
  private static final Integer METRICS_PLUGIN_ID = 0;

  @Getter
  private SimpleIoC iocContainer;

  /** Used by minecraft server class loader **/
  public ModularWeapons() {
    super();
  }

  /** Used by Mock-Bukkit **/
  protected ModularWeapons(
      JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file
  ) {
    super(loader, description, dataFolder, file);
  }

  /** Invoked when the plugin is being enabled. **/
  public void onEnable() {
    iocContainer = SimpleIoC.initialize(this, "relocations");
    registerCommands();

    //TODO: Metrics.of(this, METRICS_PLUGIN_ID);
    //TODO: Notifier.listenForUpdates(this, SPIGOT_PLUGIN_ID);
  }

  /** Reload the plugin. **/
  public void reload() {
    iocContainer.removeAllBeans();
    iocContainer = SimpleIoC.initialize(this, "relocations");
    registerCommands();
  }

  private void registerCommands() {
    CommandExecutor.setPrefix(PREFIX);
    CommandManager commandManager = new CommandManager(this);
    commandManager.registerCommand(iocContainer.getBean(PluginController.class));
  }

  /** Store all the plugin permission **/
  public static final class Permissions {
    public static final String ADMIN = "modularweapons.admin";
  }

}
