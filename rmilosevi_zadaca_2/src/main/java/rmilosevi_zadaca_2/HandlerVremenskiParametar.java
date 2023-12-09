package rmilosevi_zadaca_2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HandlerVremenskiParametar implements Handler {

  Handler sljedeci;

  @Override
  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception {
    if (kljucParametra.compareTo("kr") == 0 || kljucParametra.compareTo("pr") == 0) {
      try {
        LocalTime.parse(vrijednostParametra, DateTimeFormatter.ofPattern("HH:mm"));
      } catch (Exception e) {
        throw new Exception("Parametar '" + kljucParametra + "' mora biti u formatu 'HH:mm'");
      }
    } else {
      if (sljedeci != null) {
        sljedeci.obradiZahtjev(kljucParametra, vrijednostParametra);
      } else {
        throw new Exception("Parametar " + kljucParametra + " nije prepoznat");
      }
    }
  }


  @Override
  public void postaviSljedeceg(Handler handler) {
    sljedeci = handler;
  }

}
