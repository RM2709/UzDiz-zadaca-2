package rmilosevi_zadaca_2;

public class HandlerParametarIsporuke implements Handler {

  Handler sljedeci;

  @Override
  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception {
    if (kljucParametra.compareTo("isporuka") == 0) {
      try {
        int isporuka = Integer.parseInt(vrijednostParametra);
        if (isporuka != 1 && isporuka != 2) {
          throw new Exception("Vrijednost parametra '" + kljucParametra + "' mora biti 1 ili 2!");
        }
      } catch (Exception e) {
        throw new Exception("Vrijednost parametra '" + kljucParametra + "' mora biti 1 ili 2!");
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
