package rmilosevi_zadaca_2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UcitavanjePodataka {

  Tvrtka tvrtka;

  public UcitavanjePodataka() {
    tvrtka = Tvrtka.dohvatiInstancu();
  }

  public List<VrstaPaketa> ucitajVrstePaketa(String naziv_datoteke) throws Exception {
    List<VrstaPaketa> vrste_paketa = new ArrayList<>();
    VrstaPaketa vrsta_paketa;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo(
          "Oznaka;Opis;Visina;Širina;Dužina;Maksimalna težina;Cijena;Cijena hitno;CijenaP;CijenaT") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske = provjeriIspravnostVrstePaketa(vrijednosti);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        vrsta_paketa = new VrstaPaketa(vrijednosti[0], vrijednosti[1],
            Double.parseDouble(vrijednosti[2].replace(',', '.')),
            Double.parseDouble(vrijednosti[3].replace(',', '.')),
            Double.parseDouble(vrijednosti[4].replace(',', '.')),
            Double.parseDouble(vrijednosti[5].replace(',', '.')),
            Double.parseDouble(vrijednosti[6].replace(',', '.')),
            Double.parseDouble(vrijednosti[7].replace(',', '.')),
            Double.parseDouble(vrijednosti[8].replace(',', '.')),
            Double.parseDouble(vrijednosti[9].replace(',', '.')));
        vrste_paketa.add(vrsta_paketa);
      }
      return vrste_paketa;
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private String provjeriIspravnostVrstePaketa(String[] vrijednosti) {
    try {
      if (vrijednosti.length != 10) {
        throw new Exception("Redak mora imati točno 10 atributa");
      }
      double visina;
      double sirina;
      double duzina;
      double tezina;
      double cijena;
      double cijena_hitno;
      double cijenaP;
      double cijenaT;
      try {
        visina = Double.parseDouble(vrijednosti[2].replace(',', '.'));
        sirina = Double.parseDouble(vrijednosti[3].replace(',', '.'));
        duzina = Double.parseDouble(vrijednosti[4].replace(',', '.'));
        tezina = Double.parseDouble(vrijednosti[5].replace(',', '.'));
      } catch (Exception e) {
        throw new Exception(
            "Vrijednosti dimenzija i maksimalne težine moraju biti u obliku double");
      }
      try {
        cijena = Double.parseDouble(vrijednosti[6].replace(',', '.'));
        cijena_hitno = Double.parseDouble(vrijednosti[7].replace(',', '.'));
        cijenaP = Double.parseDouble(vrijednosti[8].replace(',', '.'));
        cijenaT = Double.parseDouble(vrijednosti[9].replace(',', '.'));
      } catch (Exception e) {
        throw new Exception("Vrijednosti cijene moraju biti u obliku double");
      }
      if (visina <= 0 || sirina <= 0 || duzina <= 0 || tezina < 0) {
        throw new Exception("Vrijednosti dimenzija moraju biti veće od nule");
      }
      if (cijena < 0 || cijena_hitno < 0 || cijenaP < 0 || cijenaT < 0) {
        throw new Exception("Vrijednosti cijene moraju biti veće ili jednake nuli");
      }
      return null;
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public List<SimpleEntry<LocalDateTime, String>> ucitajPodatkePaketi(String naziv_datoteke,
      List<VrstaPaketa> podaci_vrste_paketa, List<Osoba> osobe, int maksimalna_tezina)
      throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      List<SimpleEntry<LocalDateTime, String>> podaci_paketi =
          new ArrayList<SimpleEntry<LocalDateTime, String>>();
      if (prvi_red.compareTo(
          "Oznaka;Vrijeme prijema;Pošiljatelj;Primatelj;Vrsta paketa;Visina;Širina;Dužina;Težina;Usluga dostave;Iznos pouzeća") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        String[] vrijednosti = red.split(";");
        String opis_greske =
            provjeriIspravnostPaketa(vrijednosti, podaci_vrste_paketa, maksimalna_tezina, osobe);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        LocalDateTime vrijeme = LocalDateTime.parse(vrijednosti[1],
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
        podaci_paketi.add(new SimpleEntry<LocalDateTime, String>(vrijeme, red));
      }
      podaci_paketi.sort(Comparator.comparing(SimpleEntry<LocalDateTime, String>::getKey));
      return podaci_paketi;
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("Datoteka naziva '" + naziv_datoteke + "' ne postoji");
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  public List<Paket> ucitajPakete(String naziv_datoteke, List<VrstaPaketa> podaci_vrste_paketa,
      List<Osoba> osobe, int maksimalna_tezina) throws Exception {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      List<Paket> paketi = new ArrayList<>();
      PaketBuilder builder;
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo(
          "Oznaka;Vrijeme prijema;Pošiljatelj;Primatelj;Vrsta paketa;Visina;Širina;Dužina;Težina;Usluga dostave;Iznos pouzeća") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske =
            provjeriIspravnostPaketa(vrijednosti, podaci_vrste_paketa, maksimalna_tezina, osobe);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        if (vrijednosti[4].compareTo("X") == 0) {
          builder = new SlobodniPaketBuilder();
        } else {
          builder = new TipskiPaketBuilder();
        }
        Paket paket = builder.resetiraj().postaviVrstuPaketa(vrijednosti[4], podaci_vrste_paketa)
            .postaviOznaku(vrijednosti[0])
            .postaviVrijemePrijema(LocalDateTime.parse(vrijednosti[1],
                DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")))
            .postaviPosiljatelja(dohvatiOsobu(vrijednosti[2], osobe))
            .postaviPrimatelja(dohvatiOsobu(vrijednosti[3], osobe)).postaviVisinu(vrijednosti[5])
            .postaviSirinu(vrijednosti[6]).postaviDuzinu(vrijednosti[7])
            .postaviTezinu(vrijednosti[8]).postaviUsluguDostave(vrijednosti[9])
            .naplata(vrijednosti[10]).postaviStatusIsporuke("Nezaprimljen").izgradi();
        paketi.add(paket);
        paket.paketObavijestavanje.prijaviSlusanje(paket.getPosiljatelj());
        paket.paketObavijestavanje.prijaviSlusanje(paket.getPrimatelj());
      }
      paketi.sort(Comparator.comparing(Paket::getVrijeme_prijema));
      return paketi;
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private Osoba dohvatiOsobu(String osoba_string, List<Osoba> osobe) {
    for (Osoba osoba : osobe) {
      if (osoba.getNaziv().compareTo(osoba_string) == 0) {
        return osoba;
      }
    }
    return null;
  }

  private String provjeriIspravnostPaketa(String[] vrijednosti,
      List<VrstaPaketa> podaci_vrste_paketa, int maksimalna_tezina, List<Osoba> osobe) {
    if (vrijednosti.length != 11) {
      return "Redak mora imati točno 11 atributa";
    }
    double visina;
    double sirina;
    double duzina;
    double tezina;
    double iznos_pouzeca;
    try {
      visina = Double.parseDouble(vrijednosti[5].replace(',', '.'));
      sirina = Double.parseDouble(vrijednosti[6].replace(',', '.'));
      duzina = Double.parseDouble(vrijednosti[7].replace(',', '.'));
      tezina = Double.parseDouble(vrijednosti[8].replace(',', '.'));
      iznos_pouzeca = Double.parseDouble(vrijednosti[10].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednosti dimenzija, maksimalne težine, i  iznosa pouzeća moraju biti u obliku double";
    }
    if (vrijednosti[9].compareTo("S") != 0 && vrijednosti[9].compareTo("H") != 0
        && vrijednosti[9].compareTo("P") != 0 && vrijednosti[9].compareTo("R") != 0) {
      return "Usluga dostave može biti samo tipa S, H, P, ili R";
    }
    if (visina < 0 || sirina < 0 || duzina < 0 || tezina < 0) {
      return "Vrijednosti dimenzija ne smiju biti negativne";
    }
    if (iznos_pouzeca < 0) {
      return "Vrijednost iznosa pouzeća ne smije biti negativna";
    }
    if (!postojiLiVrstaPaketa(vrijednosti, podaci_vrste_paketa)) {
      return "Vrsta paketa '" + vrijednosti[4] + "' ne postoji";
    }
    if ((vrijednosti[4].compareTo("X") != 0) && (visina != 0 || sirina != 0 || duzina != 0)) {
      return "Visina, širina i dužina tipskih paketa mora biti 0";
    }
    if (vrijednosti[9].compareTo("P") != 0 && iznos_pouzeca != 0) {
      return "Iznos pouzeća može biti veći od 0 samo kod paketa sa uslugom pouzeća";
    }
    if (!provjeriDatum(vrijednosti[1])) {
      return "Vrijeme prijema mora biti u formatu 'dd.mm.yyyy. hh:mm:ss'";
    }
    String odgovor = provjeriDimenzijePaketa(vrijednosti, podaci_vrste_paketa, maksimalna_tezina);
    if (odgovor != null) {
      return odgovor;
    }
    return provjeriPosiljateljaPrimatelja(vrijednosti, osobe);
  }

  private String provjeriPosiljateljaPrimatelja(String[] vrijednosti, List<Osoba> osobe) {
    Osoba posiljatelj = dohvatiOsobu(vrijednosti[2], osobe);
    Osoba primatelj = dohvatiOsobu(vrijednosti[3], osobe);
    if (posiljatelj == null) {
      return "Pošiljatelj naziva '" + vrijednosti[2] + "' ne postoji";
    }
    if (primatelj == null) {
      return "Primatelj naziva '" + vrijednosti[3] + "' ne postoji";
    }
    return null;
  }

  private String provjeriDimenzijePaketa(String[] vrijednosti,
      List<VrstaPaketa> podaci_vrste_paketa, int maksimalna_tezina) {
    double visina = Double.parseDouble(vrijednosti[5].replace(',', '.'));
    double sirina = Double.parseDouble(vrijednosti[6].replace(',', '.'));
    double duzina = Double.parseDouble(vrijednosti[7].replace(',', '.'));
    double tezina = Double.parseDouble(vrijednosti[8].replace(',', '.'));
    VrstaPaketa vrsta_paketa = null;
    for (VrstaPaketa vrsta : podaci_vrste_paketa) {
      if (vrsta.oznaka().compareTo(vrijednosti[4]) == 0)
        vrsta_paketa = vrsta;
    }
    if (vrsta_paketa.oznaka().compareTo("X") == 0) {
      if (visina > vrsta_paketa.visina()) {
        return "Visina slobodnog paketa ne može biti veća od propisane";
      }
      if (sirina > vrsta_paketa.sirina()) {
        return "Širina slobodnog paketa ne može biti veća od propisane";
      }
      if (duzina > vrsta_paketa.duzina()) {
        return "Dužina slobodnog paketa ne može biti veća od propisane";
      }
      if (tezina > maksimalna_tezina) {
        return "Maksimalna težina slobodnog paketa ne može biti veća od propisane";
      }
    } else {
      if (visina != 0 || sirina != 0 || duzina != 0) {
        return "Visina, širina, i dužina tipskog paketa moraju biti 0";
      }
      if (tezina > vrsta_paketa.maksimalna_tezina()) {
        return "Težina tipskog paketa ne može biti veća od propisane maksimalne težine";
      }
    }
    return null;
  }

  private boolean postojiLiVrstaPaketa(String[] vrijednosti,
      List<VrstaPaketa> podaci_vrste_paketa) {
    for (VrstaPaketa vrsta_paketa : podaci_vrste_paketa) {
      if (vrsta_paketa.oznaka().compareTo(vrijednosti[4]) == 0) {
        return true;
      }
    }
    return false;
  }

  private boolean provjeriDatum(String vrijednost) {
    try {
      LocalDateTime.parse(vrijednost, DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public List<Vozilo> ucitajVozila(String naziv_datoteke, List<Podrucje> podrucja)
      throws IOException {
    Vozilo vozilo;
    List<Vozilo> podaci_vozila = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo(
          "Registracija;Opis;Kapacitet težine u kg;Kapacitet prostora u m3;Redoslijed;Prosječna brzina;Područja po rangu;Status") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske = provjeriIspravnostVozila(vrijednosti, podrucja);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        vozilo = new Vozilo(vrijednosti[0], vrijednosti[1],
            Double.parseDouble(vrijednosti[2].replace(',', '.')),
            Double.parseDouble(vrijednosti[3].replace(',', '.')), Integer.parseInt(vrijednosti[4]),
            Integer.parseInt(vrijednosti[5]), dohvatiPodrucja(vrijednosti[6], podrucja),
            dohvatiStatus(vrijednosti[7]));
        podaci_vozila.add(vozilo);
      }
      return podaci_vozila;
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private Status dohvatiStatus(String status_string) {
    switch (status_string.trim()) {
      case "A": {
        return new Aktivno();
      }
      case "NA": {
        return new NijeAktivno();
      }
      case "NI": {
        return new NijeIspravno();
      }
    }
    return null;
  }

  private List<Podrucje> dohvatiPodrucja(String podrucja_string, List<Podrucje> podrucja) {
    List<Podrucje> podrucja_po_rangu = new ArrayList<>();
    String[] podrucja_polje = podrucja_string.split(",");
    for (String podrucje_string : Arrays.asList(podrucja_polje)) {
      for (Podrucje podrucje : podrucja) {
        if (podrucje_string.trim().compareTo(podrucje.getId().toString()) == 0) {
          podrucja_po_rangu.add(podrucje);
        }
      }
    }
    return podrucja_po_rangu;
  }

  private String provjeriIspravnostVozila(String[] vrijednosti, List<Podrucje> podrucja) {
    if (vrijednosti.length != 8) {
      return "Redak mora imati točno 8 atributa";
    }
    double tezina;
    double prostor;
    try {
      tezina = Double.parseDouble(vrijednosti[2].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost kapaciteta težine mora biti u obliku double";
    }
    try {
      prostor = Double.parseDouble(vrijednosti[3].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost kapaciteta prostora mora biti u obliku double";
    }
    try {
      Integer.parseInt(vrijednosti[5]);
    } catch (Exception e) {
      return "Vrijednost prosječne brzine mora biti u obliku integer";
    }
    try {
      Integer.parseInt(vrijednosti[4]);
    } catch (Exception e) {
      return "Vrijednost redoslijeda mora biti u obliku integer";
    }
    if (tezina <= 0) {
      return "Kapacitet težine mora biti veći od nula";
    }
    if (prostor <= 0) {
      return "Kapacitet prostora mora biti veći od nula";
    }
    return provjeriStringVrijednostiVozila(vrijednosti, podrucja);
  }

  private String provjeriStringVrijednostiVozila(String[] vrijednosti, List<Podrucje> podrucja) {
    if (vrijednosti[7].trim().compareTo("A") != 0 && vrijednosti[7].trim().compareTo("NI") != 0
        && vrijednosti[7].trim().compareTo("NA") != 0) {
      return "Status vozila može biti samo A, NA, ili NI";
    }
    try {
      String[] podrucja_string = vrijednosti[6].trim().split(",");
      for (int i = 0; i < podrucja_string.length; i++) {
        if (!postojiLiPodrucje(podrucja_string[i].trim(), podrucja)) {
          return "Područje '" + podrucja_string[i] + "' ne postoji!";
        }
      }
    } catch (Exception e) {
      return "Vrijednost prosječne brzine mora biti u obliku integer";
    }
    return null;
  }

  private boolean postojiLiPodrucje(String podrucje_string, List<Podrucje> podrucja) {
    for (Podrucje podrucje : podrucja) {
      if (podrucje.getId().toString().compareTo(podrucje_string) == 0) {
        return true;
      }
    }
    return false;
  }

  public List<Ulica> ucitajUlice(String naziv_datoteke) throws IOException {
    List<Ulica> ulice = new ArrayList<>();
    Ulica ulica;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo(
          "id; naziv; gps_lat_1; gps_lon_1; gps_lat_2; gps_lon_2; najveći kućni broj") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske = provjeriIspravnostUlice(vrijednosti);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        ulica = new Ulica(Integer.parseInt(vrijednosti[0]), vrijednosti[1],
            Double.parseDouble(vrijednosti[2].replace(',', '.')),
            Double.parseDouble(vrijednosti[3].replace(',', '.')),
            Double.parseDouble(vrijednosti[4].replace(',', '.')),
            Double.parseDouble(vrijednosti[5].replace(',', '.')), Integer.parseInt(vrijednosti[6]));
        ulice.add(ulica);
      }
      return ulice;
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("Datoteka naziva '" + naziv_datoteke + "' ne postoji");
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private String provjeriIspravnostUlice(String[] vrijednosti) {
    if (vrijednosti.length != 7) {
      return "Redak mora imati točno 7 atributa";
    }
    try {
      Integer.parseInt(vrijednosti[0]);
    } catch (Exception e) {
      return "Vrijednost id mora biti u obliku integer";
    }
    try {
      Double.parseDouble(vrijednosti[2].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost prve geografske širine (lat_1) mora biti u obliku double";
    }
    try {
      Double.parseDouble(vrijednosti[3].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost prve geografske dužine (lon_1) mora biti u obliku double";
    }
    try {
      Double.parseDouble(vrijednosti[4].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost druge geografske širine (lat_2) mora biti u obliku double";
    }
    try {
      Double.parseDouble(vrijednosti[5].replace(',', '.'));
    } catch (Exception e) {
      return "Vrijednost druge geografske dužine (lon_2) mora biti u obliku double";
    }
    try {
      Integer.parseInt(vrijednosti[6]);
    } catch (Exception e) {
      return "Vrijednost najvećeg kućnog broja mora biti u obliku integer";
    }
    return null;
  }

  public List<Mjesto> ucitajMjesta(String naziv_datoteke, List<Ulica> ulice) throws IOException {
    List<Mjesto> mjesta = new ArrayList<>();
    Mjesto mjesto;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo("id; naziv; ulica,ulica,ulica,...") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        List<Komponent> odabrane_ulice = odaberiUlice(vrijednosti, ulice);
        String opis_greske = provjeriIspravnostMjesta(vrijednosti, ulice);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        mjesto = new Mjesto(Integer.parseInt(vrijednosti[0]), vrijednosti[1], odabrane_ulice);
        mjesta.add(mjesto);
      }
      return mjesta;
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private List<Komponent> odaberiUlice(String[] vrijednosti, List<Ulica> ulice) {
    List<Komponent> odabrane_ulice = new ArrayList<>();
    String[] ulice_string = vrijednosti[2].trim().split(",");
    for (Ulica ulica : ulice) {
      if (Arrays.asList(ulice_string).contains(ulica.getId().toString())) {
        odabrane_ulice.add(ulica);
      }
    }
    return odabrane_ulice;
  }

  private String provjeriIspravnostMjesta(String[] vrijednosti, List<Ulica> ulice) {
    if (vrijednosti.length != 3) {
      return "Redak mora imati točno 3 atributa";
    }
    try {
      Integer.parseInt(vrijednosti[0]);
    } catch (Exception e) {
      return "Vrijednost id mora biti u obliku integer";
    }
    return null;
  }

  public List<Podrucje> ucitajPodrucja(String naziv_datoteke, List<Mjesto> mjesta,
      List<Ulica> ulice) throws IOException {
    List<Podrucje> podrucja = new ArrayList<>();
    Podrucje podrucje;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo("id;grad:ulica,grad:ulica,grad:*,...") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske = provjeriIspravnostPodrucja(vrijednosti, mjesta, ulice);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        podrucje = new Podrucje(Integer.parseInt(vrijednosti[0]));
        dodajLokacijePodrucju(podrucje, vrijednosti[1], mjesta, ulice);
        podrucja.add(podrucje);
      }
      return podrucja;
    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private void dodajLokacijePodrucju(Podrucje podrucje, String string, List<Mjesto> mjesta,
      List<Ulica> ulice) {
    String[] lokacije = string.trim().split(",");
    List<String> lista_lokacija = Arrays.asList(lokacije);
    Collections.sort(lista_lokacija);
    String[] lokacija_split;
    for (String lokacija : lista_lokacija) {
      lokacija_split = lokacija.trim().split(":");
      for (Mjesto mjesto : mjesta) {
        if (mjesto.getId().toString().compareTo(lokacija_split[0]) == 0) {
          podrucje.dodajDijete(mjesto);
          continue;
        }
      }
      for (Ulica ulica : ulice) {
        if (ulica.getId().toString().compareTo(lokacija_split[1]) == 0) {
          podrucje.dodajDijete(ulica);
          continue;
        }
      }
    }
  }

  private String provjeriIspravnostPodrucja(String[] vrijednosti, List<Mjesto> mjesta,
      List<Ulica> ulice) {
    if (vrijednosti.length != 2) {
      return "Redak mora imati točno 2 atributa";
    }
    try {
      Integer.parseInt(vrijednosti[0]);
    } catch (Exception e) {
      return "Vrijednost id mora biti u obliku integer";
    }
    List<String> lokacije_string = Arrays.asList(vrijednosti[1].trim().split(","));
    String[] lokacija_split;
    for (String lokacija : lokacije_string) {
      lokacija_split = lokacija.split(":");
      Mjesto ciljano_mjesto = null;
      Ulica ciljana_ulica = null;
      for (Mjesto mjesto : mjesta) {
        if (mjesto.getId().toString().compareTo(lokacija_split[0]) == 0) {
          ciljano_mjesto = mjesto;
        }
      }
      for (Ulica ulica : ulice) {
        if (ulica.getId().toString().compareTo(lokacija_split[1]) == 0) {
          ciljana_ulica = ulica;
        }
      }
      if (ciljana_ulica == null && lokacija_split[1].compareTo("*") != 0) {
        return "Ulica s id vrijednosti " + lokacija_split[1] + " ne postoji";
      }
      if (ciljano_mjesto == null) {
        return "Mjesto s id vrijednosti " + lokacija_split[0] + " ne postoji";
      }
      if (!ciljano_mjesto.djeca.contains(ciljana_ulica) && lokacija_split[1].compareTo("*") != 0) {
        return "Ulica '" + ciljana_ulica.getNaziv().trim() + "' se ne nalazi u mjestu '"
            + ciljano_mjesto.getNaziv().trim() + "'";
      }
    }
    return null;
  }

  public List<Osoba> ucitajOsobe(String naziv_datoteke, List<Mjesto> mjesta, List<Ulica> ulice)
      throws IOException {
    List<Osoba> osobe = new ArrayList<>();
    Osoba osoba;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(naziv_datoteke))) {
      String prvi_red = bufferedReader.readLine();
      if (prvi_red.compareTo("osoba; grad; ulica; kbr") != 0) {
        throw new IOException();
      }
      String red;
      while ((red = bufferedReader.readLine()) != null) {
        if (red.isBlank() || red.isEmpty()) {
          continue;
        }
        String[] vrijednosti = red.split(";");
        String opis_greske = provjeriIspravnostOsobe(vrijednosti, mjesta, ulice);
        if (opis_greske != null) {
          tvrtka.setBroj_pogreske(tvrtka.getBroj_pogreske() + 1);
          System.out.println(
              "Pogreška " + tvrtka.getBroj_pogreske() + ": " + red + " [" + opis_greske + "]");
          continue;
        }
        osoba = new Osoba(vrijednosti[0], Integer.parseInt(vrijednosti[3]));
        dodajLokacijeOsobi(osoba, vrijednosti, mjesta, ulice);
        osobe.add(osoba);
      }
      return osobe;

    } catch (IOException e) {
      throw new IOException("Greška pri čitanju datoteke '" + naziv_datoteke + "'");
    }
  }

  private void dodajLokacijeOsobi(Osoba osoba, String[] vrijednosti, List<Mjesto> mjesta,
      List<Ulica> ulice) {
    for (Mjesto mjesto : mjesta) {
      if (mjesto.getId().toString().compareTo(vrijednosti[1]) == 0) {
        osoba.setGrad(mjesto);
      }
    }
    for (Ulica ulica : ulice) {
      if (ulica.getId().toString().compareTo(vrijednosti[2]) == 0) {
        osoba.setUlica(ulica);
      }
    }

  }

  private String provjeriIspravnostOsobe(String[] vrijednosti, List<Mjesto> mjesta,
      List<Ulica> ulice) {
    if (vrijednosti.length != 4) {
      return "Redak mora imati točno 4 atributa";
    }
    try {
      Integer.parseInt(vrijednosti[3]);
    } catch (Exception e) {
      return "Vrijednost kućnog broja mora biti u obliku integer";
    }
    Mjesto odabrano_mjesto = null;
    Ulica odabrana_ulica = null;
    for (Mjesto mjesto : mjesta) {
      if (mjesto.getId().toString().compareTo(vrijednosti[1]) == 0) {
        odabrano_mjesto = mjesto;
      }
    }
    for (Ulica ulica : ulice) {
      if (ulica.getId().toString().compareTo(vrijednosti[2]) == 0) {
        odabrana_ulica = ulica;
      }
    }
    if (odabrana_ulica == null) {
      return "Ulica s id vrijednosti " + vrijednosti[2] + " ne postoji";
    }
    if (odabrano_mjesto == null) {
      return "Mjesto s id vrijednosti " + vrijednosti[1] + " ne postoji";
    }
    if (!odabrano_mjesto.djeca.contains(odabrana_ulica)) {
      return "Ulica " + odabrana_ulica.getNaziv().trim() + " se ne nalazi u mjestu "
          + odabrano_mjesto.getNaziv().trim();
    }
    return null;
  }

}
