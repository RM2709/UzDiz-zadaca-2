package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Tvrtka {

  private static final Tvrtka instanca = new Tvrtka();
  private LocalTime pocetak_rada;
  private LocalTime kraj_rada;
  private int vrijeme_isporuke;
  private int maksimalna_tezina;
  private LocalDateTime virtualni_sat;
  private int mnozitelj_sekundi;
  private int isporuka;
  private double gps_lat;
  private double gps_lon;
  private int broj_pogreske;
  private List<SimpleEntry<LocalDateTime, String>> podaci_paketi;
  private List<Vozilo> vozila;
  private List<VrstaPaketa> vrste_paketa;
  private List<Ulica> ulice;
  private List<Mjesto> mjesta;
  private List<Podrucje> podrucja;
  private List<Osoba> osobe;
  private double prihod;
  private UredZaPrijem uredZaPrijem;
  private UredZaDostavu uredZaDostavu;
  private List<SimpleEntry<String, String>> odjave_slusanja;
  private IspisVisitor ispisVisitor = new IspisVisitor();
  private List<SimpleEntry<LocalDateTime, String>> dnevnik = new ArrayList<>();
  private boolean pisiDnevnik = true;

  private Tvrtka() {}

  public static Tvrtka dohvatiInstancu() {
    return instanca;
  }

  public boolean inicijaliziraj(String[] args) throws Exception {
    ProvjeraArgumenata provjeraArgumenata = new ProvjeraArgumenata();
    Properties parametri = provjeraArgumenata.provjeriArgumente(args);
    ucitajPodatke(parametri);
    return true;
  }

  public void zapisiDnevnik(String zapis) {
    dnevnik.add(new SimpleEntry<>(LocalDateTime.now(), zapis));
  }

  public List<SimpleEntry<LocalDateTime, String>> dohvatiDnevnik() {
    if (!dnevnik.isEmpty() && dnevnik != null) {
      return dnevnik;
    } else {
      return null;
    }
  }

  public boolean pisiDnevnik() {
    return pisiDnevnik;
  }

  public void postaviPisanjeDnevnika(boolean pisiDnevnik) {
    this.pisiDnevnik = pisiDnevnik;
  }


  public int getIsporuka() {
    return isporuka;
  }

  public void setIsporuka(int isporuka) {
    this.isporuka = isporuka;
  }

  public List<SimpleEntry<String, String>> getOdjave_slusanja() {
    return odjave_slusanja;
  }

  public void dodajOdjavu_slusanja(SimpleEntry<String, String> odjava_slusanja) {
    odjave_slusanja.add(odjava_slusanja);
  }

  public void ukloniOdjavu_slusanja(SimpleEntry<String, String> odjava_slusanja) {
    odjave_slusanja.remove(odjava_slusanja);
  }

  public int getVrijeme_isporuke() {
    return vrijeme_isporuke;
  }

  public void setVrijeme_isporuke(int vrijeme_isporuke) {
    this.vrijeme_isporuke = vrijeme_isporuke;
  }

  public double getGps_lat() {
    return gps_lat;
  }

  public void setGps_lat(double gps_lat) {
    this.gps_lat = gps_lat;
  }

  public double getGps_lon() {
    return gps_lon;
  }

  public void setGps_lon(double gps_lon) {
    this.gps_lon = gps_lon;
  }

  public int getBroj_pogreske() {
    return broj_pogreske;
  }

  public void setBroj_pogreske(int broj_pogreske) {
    this.broj_pogreske = broj_pogreske;
  }

  public List<VrstaPaketa> getPodaci_vrste_paketa() {
    return vrste_paketa;
  }

  public void setPodaci_vrste_paketa(List<VrstaPaketa> podaci_vrste_paketa) {
    this.vrste_paketa = podaci_vrste_paketa;
  }

  public List<Osoba> getOsobe() {
    return osobe;
  }

  public void setOsobe(List<Osoba> osobe) {
    this.osobe = osobe;
  }

  public LocalTime getPocetak_rada() {
    return pocetak_rada;
  }

  public void setPocetak_rada(LocalTime pocetak_rada) {
    this.pocetak_rada = pocetak_rada;
  }

  public LocalTime getKraj_rada() {
    return kraj_rada;
  }

  public void setKraj_rada(LocalTime kraj_rada) {
    this.kraj_rada = kraj_rada;
  }

  public int getMaksimalna_tezina() {
    return maksimalna_tezina;
  }

  public void setMaksimalna_tezina(int maksimalna_tezina) {
    this.maksimalna_tezina = maksimalna_tezina;
  }

  public LocalDateTime getVirtualni_sat() {
    return virtualni_sat;
  }

  public void setVirtualni_sat(LocalDateTime virtualni_sat) {
    this.virtualni_sat = virtualni_sat;
  }

  public int getMnozitelj_sekundi() {
    return mnozitelj_sekundi;
  }

  public void setMnozitelj_sekundi(int mnozitelj_sekundi) {
    this.mnozitelj_sekundi = mnozitelj_sekundi;
  }

  public List<SimpleEntry<LocalDateTime, String>> getPodaci_paketi() {
    return podaci_paketi;
  }

  public void setPodaci_paketi(List<SimpleEntry<LocalDateTime, String>> podaci_paketi) {
    this.podaci_paketi = podaci_paketi;
  }

  public List<Vozilo> getVozila() {
    return vozila;
  }

  public void setVozila(List<Vozilo> vozila) {
    this.vozila = vozila;
  }

  public List<VrstaPaketa> getVrste_paketa() {
    return vrste_paketa;
  }

  public void setVrste_paketa(List<VrstaPaketa> vrste_paketa) {
    this.vrste_paketa = vrste_paketa;
  }

  public List<Ulica> getUlice() {
    return ulice;
  }

  public void setUlice(List<Ulica> ulice) {
    this.ulice = ulice;
  }

  public List<Mjesto> getMjesta() {
    return mjesta;
  }

  public void setMjesta(List<Mjesto> mjesta) {
    this.mjesta = mjesta;
  }

  public List<Podrucje> getPodrucja() {
    return podrucja;
  }

  public void setPodrucja(List<Podrucje> podrucja) {
    this.podrucja = podrucja;
  }

  public double getPrihod() {
    return prihod;
  }

  public void setPrihod(double prihod) {
    this.prihod = prihod;
  }

  public UredZaPrijem getUredZaPrijem() {
    return uredZaPrijem;
  }

  public void setUredZaPrijem(UredZaPrijem uredZaPrijem) {
    this.uredZaPrijem = uredZaPrijem;
  }

  public UredZaDostavu getUredZaDostavu() {
    return uredZaDostavu;
  }

  public void setUredZaDostavu(UredZaDostavu uredZaDostavu) {
    this.uredZaDostavu = uredZaDostavu;
  }

  public IspisVisitor getIspisVisitor() {
    return ispisVisitor;
  }

  public void setIspisVisitor(IspisVisitor ispisVisitor) {
    this.ispisVisitor = ispisVisitor;
  }

  public List<SimpleEntry<LocalDateTime, String>> getDnevnik() {
    return dnevnik;
  }

  public void setDnevnik(List<SimpleEntry<LocalDateTime, String>> dnevnik) {
    this.dnevnik = dnevnik;
  }

  private void ucitajPodatke(Properties parametri) throws Exception {
    UcitavanjePodataka ucitavanjePodataka = new UcitavanjePodataka();
    pocetak_rada = LocalTime.parse(parametri.getProperty("pr"));
    kraj_rada = LocalTime.parse(parametri.getProperty("kr"));
    provjeriRadnoVrijeme();
    maksimalna_tezina = Integer.parseInt(parametri.getProperty("mt"));
    vrijeme_isporuke = Integer.parseInt(parametri.getProperty("vi"));
    mnozitelj_sekundi = Integer.parseInt(parametri.getProperty("ms"));
    virtualni_sat = LocalDateTime.parse(parametri.getProperty("vs"),
        DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
    isporuka = Integer.parseInt(parametri.getProperty("isporuka"));
    gps_lat = Double.parseDouble(parametri.getProperty("gps").split(",")[0].trim());
    gps_lon = Double.parseDouble(parametri.getProperty("gps").split(",")[1].trim());
    vrste_paketa = ucitavanjePodataka.ucitajVrstePaketa(parametri.getProperty("vp"));
    ulice = ucitavanjePodataka.ucitajUlice(parametri.getProperty("pu"));
    mjesta = ucitavanjePodataka.ucitajMjesta(parametri.getProperty("pm"), ulice);
    podrucja = ucitavanjePodataka.ucitajPodrucja(parametri.getProperty("pmu"), mjesta, ulice);
    vozila = ucitavanjePodataka.ucitajVozila(parametri.getProperty("pv"), podrucja);
    osobe = ucitavanjePodataka.ucitajOsobe(parametri.getProperty("po"), mjesta, ulice);
    podaci_paketi = ucitavanjePodataka.ucitajPodatkePaketi(parametri.getProperty("pp"),
        vrste_paketa, osobe, maksimalna_tezina);
    uredZaPrijem = new UredZaPrijem(podaci_paketi);
    uredZaDostavu = new UredZaDostavu(vozila, podrucja);
    odjave_slusanja = new ArrayList<>();
  }

  private void provjeriRadnoVrijeme() throws Exception {
    if (pocetak_rada.compareTo(kraj_rada) > -1) {
      throw new Exception("Vrijeme početka rada (pr) mora biti manje od vremena kraja rada (kr)");
    }
  }

  public void azurirajPrihod(double iznos) {
    prihod += iznos;
  }

  public double prijaviPrihod() {
    return prihod;
  }

  public void rad() {
    ServisObradeKomande obradaKomandeDnevnik = new ObradaKomandeDnevnik();
    Scanner skener = new Scanner(System.in);
    String komanda;
    do {
      komanda = skener.nextLine();
      obradaKomandeDnevnik.obradiKomandu(komanda);
    } while (komanda.compareTo("Q") != 0);
    skener.close();
  }


}
