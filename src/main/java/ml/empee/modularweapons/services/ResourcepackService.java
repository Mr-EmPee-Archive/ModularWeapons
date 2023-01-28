package ml.empee.modularweapons.services;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ml.empee.ioc.annotations.Bean;
import ml.empee.json.JsonPersistence;
import ml.empee.modularweapons.ModularWeapons;
import ml.empee.modularweapons.model.Weapon;
import ml.empee.modularweapons.model.WeaponModule;
import org.apache.commons.io.FileUtils;

/** Manage the plugin resourcepack **/

@Bean
public class ResourcepackService {

  private final File resourcePackFolder;
  private final WeaponService weaponService;

  /** IoC Constructor **/
  public ResourcepackService(ModularWeapons plugin, WeaponService weaponService) {
    this.weaponService = weaponService;

    resourcePackFolder = new File(plugin.getDataFolder(), "resourcepack");
  }

  /** Generate a new resourcepack at the path <b>plugin-folder/resourcepack</b>  **/
  public void buildResourcepack() throws IOException {
    deleteResourcepack();

    resourcePackFolder.mkdirs();
    buildWeaponModels();
  }

  private void buildWeaponModels() {
    JsonPersistence jsonPersistence = new JsonPersistence();
    File modelsDirectory = new File(resourcePackFolder, "assets/minecraft/models/item/");

    for (Weapon weapon : weaponService.getWeapons()) {
      for (Set<WeaponModule> modules : Sets.powerSet(weapon.getWeaponModules())) {
        var model = buildWeaponModel(weapon, modules);
        int weaponID = Weapon.computeModelId(weapon.getUuid(modules));

        jsonPersistence.serialize(
            model, new File(modelsDirectory, weapon.getId() + "_" + weaponID + ".json")
        );
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> buildWeaponModel(Weapon weapon, Set<WeaponModule> modules) {
    ArrayList<Object> elements = new ArrayList<>();
    Map<String, Object> model = new HashMap<>(weapon.getModel());
    model.put("elements", elements);
    elements.addAll(
        (List<Object>) weapon.getModel().getOrDefault("elements", Collections.emptyList())
    );

    for (WeaponModule module : modules) {
      List<Object> modelElements = (List<Object>) module.getModel().getOrDefault(
          "elements", Collections.emptyList()
      );

      //TODO: give error if a texture with that id doesn't exists

      elements.addAll(modelElements);
    }

    //TODO: Copy the texture inside the texture folder
    return model;
  }

  public void deleteResourcepack() throws IOException {
    FileUtils.deleteDirectory(resourcePackFolder);
  }

}
