package ml.empee.modularweapons.services;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import ml.empee.ioc.annotations.Bean;
import ml.empee.json.JsonPersistence;
import ml.empee.modularweapons.ModularWeapons;
import ml.empee.modularweapons.config.Config;
import ml.empee.modularweapons.model.Weapon;

/** Manages the weapons **/

@Bean
public class WeaponService {

  private final ModularWeapons plugin;
  private final Config config;

  @Getter
  private Set<Weapon> weapons;

  /** IoC Constructor **/
  public WeaponService(ModularWeapons plugin, Config config) {
    this.plugin = plugin;
    this.config = config;

    loadAllWeapons(new File(plugin.getDataFolder(), "weapons"));
  }

  /** Load all weapons from a path into this service **/
  public void loadAllWeapons(File path) {
    if (!path.exists()) {
      path.mkdirs();
    }

    JsonPersistence jsonPersistence = new JsonPersistence();

    weapons = Arrays.stream(path.listFiles())
        .filter(File::isDirectory)
        .map(f -> new Weapon(plugin, f, jsonPersistence))
        .collect(Collectors.toSet());

    //TODO: Check that weapon hasn't too much modules
  }

}