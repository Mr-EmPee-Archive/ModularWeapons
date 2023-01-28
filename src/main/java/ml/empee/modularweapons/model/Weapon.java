package ml.empee.modularweapons.model;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.Delegate;
import ml.empee.itembuilder.utils.ItemNbt;
import ml.empee.json.JsonPersistence;
import ml.empee.modularweapons.model.dto.WeaponData;
import ml.empee.modularweapons.utils.helpers.PluginItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A weapon configuration
 **/

public class Weapon {

  @Delegate
  private final WeaponData data;
  @Delegate
  private final PluginItem item;
  private final JavaPlugin plugin;
  @Getter
  private final Map<String, Object> model;
  @Getter
  private final Set<WeaponModule> weaponModules;

  /**
   * Parse a weapon configuration from the fs
   **/
  public Weapon(JavaPlugin plugin, File weaponFolder, JsonPersistence jsonPersistence) {
    this.plugin = plugin;

    this.data = loadWeaponData(new File(weaponFolder, "data.json"), jsonPersistence);
    this.model = loadModel(new File(weaponFolder, "model.json"), jsonPersistence);
    this.weaponModules = loadAllModules(new File(weaponFolder, "modules"), jsonPersistence);
    this.item = PluginItem.of(plugin, data.getId(), "1", Material.IRON_AXE);
  }

  /** Compute an id that doesn't start with a 0 and it is at max 7 digits **/
  public static int computeModelId(String weaponUuid) {
    int sumOfValues = 0;
    for (int i = 0; i < weaponUuid.length(); i++) {
      // Get the ASCII value of each character in the string
      sumOfValues += weaponUuid.charAt(i);
    }

    // Multiply by a large prime number
    int hashValue = sumOfValues * 997;
    // Ensure the resulting value has at most 7 digits by taking the last 7 digits
    return hashValue % 10000000;
  }

  private WeaponData loadWeaponData(File path, JsonPersistence jsonPersistence) {
    WeaponData data = jsonPersistence.deserialize(path, WeaponData.class);
    if (data == null) {
      data = new WeaponData();
    }

    return data;
  }

  private Map<String, Object> loadModel(File path, JsonPersistence jsonPersistence) {
    Map<String, Object> data = jsonPersistence.deserialize(path, Map.class);
    if (data == null) {
      return Collections.emptyMap();
    }

    return data;
  }

  private Set<WeaponModule> loadAllModules(File path, JsonPersistence jsonPersistence) {
    if (!path.exists()) {
      path.mkdirs();
    }

    return Arrays.stream(path.listFiles())
        .filter(File::isDirectory)
        .map(f -> new WeaponModule(f, jsonPersistence))
        .collect(Collectors.toSet());
  }

  /** Add modules to a weapon **/
  public void equipModules(ItemStack target, WeaponModule... modules) {
    String equippedModules = Objects.requireNonNullElse(
        ItemNbt.getString(plugin, target, "modules"),
        ""
    );

    for (WeaponModule module : modules) {
      equippedModules += ";" + module.getId() + ";";
    }

    ItemNbt.setString(plugin, target, "modules", equippedModules);
    setModelId(target);
  }

  /** Remove modules from a weapon **/
  public void unequipModules(ItemStack target, WeaponModule... modules) {
    String equippedModules = Objects.requireNonNullElse(
        ItemNbt.getString(plugin, target, "modules"),
        ""
    );

    for (WeaponModule module : modules) {
      equippedModules = equippedModules.replace(";" + module.getId() + ";", "");
    }

    ItemNbt.setString(plugin, target, "modules", equippedModules);
    setModelId(target);
  }

  /** Check if it has modules **/
  public boolean hasModule(ItemStack target, WeaponModule module) {
    String equippedModules = Objects.requireNonNullElse(
        ItemNbt.getString(plugin, target, "modules"),
        ""
    );

    return equippedModules.contains(";" + module.getId() + ";");
  }

  /** Get the model id of a weapon **/
  public int getModelId(ItemStack target) {
    if (!target.hasItemMeta()) {
      return -1;
    }

    return target.getItemMeta().getCustomModelData();
  }

  private void setModelId(ItemStack target) {
    if (!target.hasItemMeta()) {
      return;
    }

    target.getItemMeta().setCustomModelData(computeModelId(target));
  }

  private int computeModelId(ItemStack target) {
    String weaponUuid = data.getId();
    weaponUuid += Objects.requireNonNullElse(
        ItemNbt.getString(plugin, target, "modules"),
        ""
    );

    return computeModelId(weaponUuid);
  }

  /** Build an uuid for this type of weapon based on the given modules **/
  public String getUuid(Collection<WeaponModule> modules) {
    String weaponUuid = data.getId();

    for (WeaponModule module : modules) {
      weaponUuid += ";" + module.getId() + ";";
    }

    return weaponUuid;
  }

}