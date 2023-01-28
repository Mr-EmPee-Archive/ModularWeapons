package ml.empee.modularweapons.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ml.empee.configurator.Configuration;
import ml.empee.configurator.annotations.Path;
import ml.empee.ioc.annotations.Bean;
import ml.empee.modularweapons.ModularWeapons;

/** Configuration of the plugin **/

@Bean
@Getter
@Setter(AccessLevel.PRIVATE)
public class Config extends Configuration {

  @Path("weapons.modules.max")
  private Integer maxModules;

  public Config(ModularWeapons plugin) {
    super(plugin, "config.yml", 1);
  }
}
