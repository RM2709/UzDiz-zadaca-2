package rmilosevi_zadaca_2;

import java.util.regex.Pattern;

public class HandlerGpsParametar implements Handler {

  Handler sljedeci;

  @Override
  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception {
    if (kljucParametra.compareTo("gps") == 0) {
      Pattern uzorak = Pattern.compile("^[1-9]{1,2}\\.[0-9]{6}, [1-9]{1,2}\\.[0-9]{6}$");
      if (!uzorak.matcher(vrijednostParametra).find()) {
        throw new Exception("Vrijednost parametra '" + kljucParametra
            + "' mora biti u formatu ' (X)X.XXXXXX, (Y)Y.YYYYYY'");
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
