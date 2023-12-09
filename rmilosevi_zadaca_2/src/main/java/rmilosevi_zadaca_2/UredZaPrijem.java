package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public class UredZaPrijem {

  private List<SimpleEntry<LocalDateTime, String>> podaci_paketi;
  private List<Paket> paketi;
  private boolean zapoceo_rad = false;


  public UredZaPrijem(List<SimpleEntry<LocalDateTime, String>> podaci_paketi) {
    this.podaci_paketi = podaci_paketi;
    this.paketi = new ArrayList<>();
  }


  public void rad(LocalDateTime od_vremena, LocalDateTime do_vremena) {
    if (!zapoceo_rad) {
      zapoceo_rad = true;
      zaprimiPaketeNaCekanju(od_vremena);
    }
    for (SimpleEntry<LocalDateTime, String> podaci_paket : podaci_paketi) {
      if (podaci_paket.getKey().isAfter(od_vremena) && podaci_paket.getKey().isBefore(do_vremena)) {
        zaprimiPaket(podaci_paket);
      }
    }
  }


  private void zaprimiPaketeNaCekanju(LocalDateTime do_vremena) {
    for (SimpleEntry<LocalDateTime, String> podaci_paket : podaci_paketi) {
      if (podaci_paket.getKey().isBefore(do_vremena)) {
        zaprimiPaket(podaci_paket);
      }
    }
  }


  private void zaprimiPaket(SimpleEntry<LocalDateTime, String> podaci_paket) {
    PaketBuilder builder;
    String[] vrijednosti = podaci_paket.getValue().split(";");
    if (vrijednosti[4].trim().compareTo("X") == 0) {
      builder = new SlobodniPaketBuilder();
    } else {
      builder = new TipskiPaketBuilder();
    }
    Paket paket = kreirajPaket(builder, vrijednosti);
    if (!Tvrtka.dohvatiInstancu().getOdjave_slusanja()
        .contains(new SimpleEntry<String, String>(paket.getOznaka(),
            dohvatiOsobu(vrijednosti[2], Tvrtka.dohvatiInstancu().getOsobe()).getNaziv()))) {
      paket.paketObavijestavanje.prijaviSlusanje(paket.getPosiljatelj());
    }
    if (!Tvrtka.dohvatiInstancu().getOdjave_slusanja()
        .contains(new SimpleEntry<String, String>(paket.getOznaka(),
            dohvatiOsobu(vrijednosti[3], Tvrtka.dohvatiInstancu().getOsobe()).getNaziv()))) {
      paket.paketObavijestavanje.prijaviSlusanje(paket.getPrimatelj());
    }
    paketi.add(paket);
    System.out.print("Paket " + paket.getOznaka() + " je zaprimljen u "
        + paket.getVrijeme_prijema().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
    if (paket.getUsluga_dostave().compareTo("P") == 0) {
      System.out.println();
    } else {
      Tvrtka.dohvatiInstancu().azurirajPrihod(paket.getIznos_dostave());
      System.out.println(". Naplaćena je dostava u iznosu " + paket.getIznos_dostave());
    }
    paket.paketObavijestavanje.obavijestiSlusace(paket, "prijem");
  }


  private Paket kreirajPaket(PaketBuilder builder, String[] vrijednosti) {
    return builder.resetiraj()
        .postaviVrstuPaketa(vrijednosti[4], Tvrtka.dohvatiInstancu().getPodaci_vrste_paketa())
        .postaviOznaku(vrijednosti[0])
        .postaviVrijemePrijema(LocalDateTime.parse(vrijednosti[1],
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")))
        .postaviPosiljatelja(dohvatiOsobu(vrijednosti[2], Tvrtka.dohvatiInstancu().getOsobe()))
        .postaviPrimatelja(dohvatiOsobu(vrijednosti[3], Tvrtka.dohvatiInstancu().getOsobe()))
        .postaviVisinu(vrijednosti[5]).postaviSirinu(vrijednosti[6]).postaviDuzinu(vrijednosti[7])
        .postaviTezinu(vrijednosti[8]).postaviUsluguDostave(vrijednosti[9]).naplata(vrijednosti[10])
        .postaviStatusIsporuke("Zaprimljen").izgradi();
  }

  private Osoba dohvatiOsobu(String osoba_string, List<Osoba> osobe) {
    for (Osoba osoba : osobe) {
      if (osoba.getNaziv().compareTo(osoba_string) == 0) {
        return osoba;
      }
    }
    return null;
  }

  public List<Paket> dohvatiEvidencijuPaketa() {
    return this.paketi;
  }


}
