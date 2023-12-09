package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandlerDatumskiParametar implements Handler {

  Handler sljedeci;

  @Override
  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception {
    if (kljucParametra.compareTo("vs") == 0) {
      try {
        LocalDateTime.parse(vrijednostParametra,
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
      } catch (Exception e) {
        throw new Exception(
            "Parametar '" + kljucParametra + "' mora biti u formatu 'dd.MM.yyyy. HH:mm:ss'");
      }
    } else {
      if (sljedeci != null) {
        sljedeci.obradiZahtjev(kljucParametra, vrijednostParametra);
      } else {
        throw new Exception("Parametar " + kljucParametra + " nije prepoznat.");
      }
    }
  }


  @Override
  public void postaviSljedeceg(Handler handler) {
    sljedeci = handler;
  }

}
