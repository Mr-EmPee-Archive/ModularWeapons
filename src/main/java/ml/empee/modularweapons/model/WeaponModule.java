package ml.empee.modularweapons.model;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Delegate;
import ml.empee.json.JsonPersistence;
import ml.empee.modularweapons.model.dto.ModuleData;

/**
 * A weapon's module configuration
 **/

public class WeaponModule {

  @Delegate
  private final ModuleData data;
  @Getter
  private final Map<String, Object> model;

  public WeaponModule(File moduleFolder, JsonPersistence jsonPersistence) {
    data = loadModuleData(new File(moduleFolder, "data.json"), jsonPersistence);
    model = loadModelElements(new File(moduleFolder, "model.json"), jsonPersistence);
  }

  private ModuleData loadModuleData(File path, JsonPersistence jsonPersistence) {
    ModuleData data = jsonPersistence.deserialize(path, ModuleData.class);
    if (data == null) {
      data = new ModuleData();
    }

    return data;
  }

  private Map<String, Object> loadModelElements(File path, JsonPersistence jsonPersistence) {
    Map<String, Object> data = jsonPersistence.deserialize(path, Map.class);
    if (data == null) {
      return Collections.emptyMap();
    }

    return data;
  }

}
