import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import ml.empee.json.JsonPersistence;
import ml.empee.modularweapons.services.ResourcepackService;
import ml.empee.modularweapons.services.WeaponService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourcepackServiceTest extends PluginTest {

  private WeaponService weaponService;
  private ResourcepackService resourcepackService;

  @BeforeEach
  @SneakyThrows
  void setup() {
    mockPlugin();

    weaponService = plugin.getIocContainer().getBean(WeaponService.class);
    resourcepackService = plugin.getIocContainer().getBean(ResourcepackService.class);
  }

  @AfterEach
  void after() {
    super.unmockPlugin();
  }

  @SneakyThrows
  private void loadCommonWeapons() {
    weaponService.loadAllWeapons(new File("src/test/resources/weapons"));
  }

  @Test
  void shouldGenerateResourcepack() throws IOException {
    loadCommonWeapons();

    resourcepackService.buildResourcepack();

    File resourcepackFolder = new File(plugin.getDataFolder(), "resourcepack");
    assertTrue(resourcepackFolder.exists());


    File modelsFolder = new File(resourcepackFolder, "assets/minecraft/models/item/");
    assertTrue(modelsFolder.exists());

    assertEquals(4, modelsFolder.listFiles().length);

    JsonPersistence jsonPersistence = new JsonPersistence();
    boolean withoutModules = false, withScopeModule = false, withChargerModule = false, withBothModules = false;
    for(File model : modelsFolder.listFiles()) {
      List<Map<String, String>> elements = (List<Map<String, String>>) jsonPersistence.deserialize(model, Map.class).get("elements");
      if(elements.stream().allMatch(v -> v.get("__comment").equals("Abstract Model"))) {
        withoutModules = true;
      } else if(elements.stream().allMatch(v -> v.get("__comment").equals("Abstract Model") || v.get("__comment").equals("Charger Model"))) {
        withChargerModule = true;
      } else if(elements.stream().allMatch(v -> v.get("__comment").equals("Abstract Model") || v.get("__comment").equals("Scope Model"))) {
        withScopeModule = true;
      } else if(elements.stream().allMatch(v -> v.get("__comment").equals("Abstract Model") || v.get("__comment").equals("Scope Model") || v.get("__comment").equals("Charger Model"))) {
        withBothModules = true;
      }
    }

    assertTrue(withBothModules && withoutModules && withChargerModule && withScopeModule);
  }

}
