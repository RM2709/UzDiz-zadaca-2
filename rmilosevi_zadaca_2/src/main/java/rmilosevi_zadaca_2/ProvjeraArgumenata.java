package rmilosevi_zadaca_2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ProvjeraArgumenata {

  public Properties provjeriArgumente(String[] args) throws Exception {
    if (args.length == 1) {
      Handler prvi_handler = kreirajHandlere();
      ProvjeraParametara provjeraParametara = new ProvjeraParametara();
      Properties parametri = procitajParametre(args[0]);
      provjeraParametara.provjeriParametre(parametri, prvi_handler);
      provjeriNoveParametre(parametri, prvi_handler);
      List<String> imenaTrazenihParametara = new ArrayList<>(Arrays.asList("vp", "pv", "pp", "mt",
          "vi", "vs", "ms", "pr", "kr", "po", "pm", "pu", "pmu", "gps", "isporuka"));
      for (String ime_parametra : imenaTrazenihParametara) {
        if (!parametri.containsKey(ime_parametra)) {
          throw new Exception("Nije pronađen obavezan parametar " + ime_parametra);
        }
      }
      return parametri;
    } else {
      throw new IOException(
          "Netočan broj argumenata (" + args.length + "). Potrebno je unijeti 1 argument.");
    }
  }

  private Handler kreirajHandlere() {
    Handler handlerCjelobrojni = new HandlerCjelobrojniParametar();
    Handler handlerDatotecni = new HandlerDatotecniParametar();
    Handler handlerDatumski = new HandlerDatumskiParametar();
    Handler handlerGps = new HandlerGpsParametar();
    Handler handlerIsporuka = new HandlerParametarIsporuke();
    Handler handlerVremenski = new HandlerVremenskiParametar();
    handlerCjelobrojni.postaviSljedeceg(handlerDatotecni);
    handlerDatotecni.postaviSljedeceg(handlerDatumski);
    handlerDatumski.postaviSljedeceg(handlerGps);
    handlerGps.postaviSljedeceg(handlerIsporuka);
    handlerIsporuka.postaviSljedeceg(handlerVremenski);
    handlerVremenski.postaviSljedeceg(null);
    return handlerCjelobrojni;
  }

  private void provjeriNoveParametre(Properties parametri, Handler prvi_handler) throws Exception {
    for (String kljucParametra : parametri.stringPropertyNames()) {
      prvi_handler.obradiZahtjev(kljucParametra, parametri.getProperty(kljucParametra));
    }

  }

  private Properties procitajParametre(String nazivDatoteke)
      throws FileNotFoundException, IOException {
    Properties parametri = new Properties();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nazivDatoteke))) {
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (!red.isBlank() && !red.isEmpty()) {
          parametri.put(red.split("=")[0].trim(), red.split("=")[1].trim());
        }
      }
      if (!parametri.isEmpty()) {
        return parametri;
      } else {
        throw new IOException("Datoteka '" + nazivDatoteke + "' je prazna.");
      }

    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("Datoteka naziva '" + nazivDatoteke + "' ne postoji.");
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + nazivDatoteke + "'.");
    }
  }
}
