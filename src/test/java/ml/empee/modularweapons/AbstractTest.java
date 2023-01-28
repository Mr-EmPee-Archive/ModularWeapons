package ml.empee.modularweapons;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import ml.empee.modularweapons.ModularWeapons;

public abstract class AbstractTest {

  protected ServerMock server;
  protected ModularWeapons plugin;

  void mockPlugin() {
    server = MockBukkit.mock();
    plugin = MockBukkit.loadSimple(ModularWeapons.class);
  }

  void unmockPlugin() {
    MockBukkit.unmock();
  }

}
