package rmilosevi_zadaca_2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HandlerDatotecniParametar implements Handler {

  Handler sljedeci;

  @Override
  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception {
    if (kljucParametra.compareTo("pp") == 0 || kljucParametra.compareTo("pv") == 0
        || kljucParametra.compareTo("vp") == 0 || kljucParametra.compareTo("po") == 0
        || kljucParametra.compareTo("pm") == 0 || kljucParametra.compareTo("pu") == 0
        || kljucParametra.compareTo("pmu") == 0) {
      try (
          BufferedReader bufferedReader = new BufferedReader(new FileReader(vrijednostParametra))) {
        bufferedReader.readLine();
      } catch (FileNotFoundException e) {
        throw new FileNotFoundException("Datoteka naziva '" + vrijednostParametra
            + "' ne postoji (parametar '" + kljucParametra + "')");
      } catch (IOException e) {
        throw new IOException("Greška pri čitanju datoteke '" + vrijednostParametra
            + "' (parametar '" + kljucParametra + "')");
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
