package rmilosevi_zadaca_2;

import java.util.Properties;

public class ProvjeraParametara {

  public void provjeriParametre(Properties parametri, Handler prvi_handler) throws Exception {
    for (String kljucParametra : parametri.stringPropertyNames()) {
      prvi_handler.obradiZahtjev(kljucParametra, parametri.getProperty(kljucParametra));
    }
  }

}
