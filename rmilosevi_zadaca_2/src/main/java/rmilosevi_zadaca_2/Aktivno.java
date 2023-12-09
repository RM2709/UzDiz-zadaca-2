package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Aktivno implements Status {

  @Override
  public List<Voznja> zapocniVoznju(List<Voznja> voznje, Vozilo vozilo,
      LocalDateTime vrijeme_pocetka_voznje, List<Paket> paketi_za_dostavu) {
    Voznja voznja = new Voznja();
    double trenutni_lat = Tvrtka.dohvatiInstancu().getGps_lat();
    double trenutni_lon = Tvrtka.dohvatiInstancu().getGps_lon();
    LocalDateTime pocetak = vrijeme_pocetka_voznje;
    if (Tvrtka.dohvatiInstancu().getIsporuka() == 2) {
      paketi_za_dostavu = poredajPaketePoNajblizojDostavi(paketi_za_dostavu);
    }
    for (Paket paket : paketi_za_dostavu) {
      String[] koordinatePaketa = izracunajKoordinatePaketa(paket).split(" ");
      SegmentVoznje segmentVoznje = new SegmentVoznje();
      segmentVoznje.setPaket(paket);
      segmentVoznje.setOd_lat(trenutni_lat);
      segmentVoznje.setOd_lon(trenutni_lon);
      trenutni_lat = Double.parseDouble(koordinatePaketa[0]);
      trenutni_lon = Double.parseDouble(koordinatePaketa[1]);
      segmentVoznje.setDo_lat(trenutni_lat);
      segmentVoznje.setDo_lon(trenutni_lon);
      segmentVoznje.inicijaliziraj(vozilo);
      segmentVoznje.setVrijeme_pocetka(pocetak);
      pocetak = pocetak.plusSeconds(segmentVoznje.dohvatiTrajanjeSekundeLong());
      segmentVoznje.setVrijeme_kraja(pocetak);
      voznja.dodajSegment(segmentVoznje);
    }
    voznja = vratiVoziloUredu(voznja, vozilo, trenutni_lat, trenutni_lon, pocetak);
    voznje.add(voznja);
    vozilo.postaviPakete(paketi_za_dostavu);
    return voznje;
  }

  private Voznja vratiVoziloUredu(Voznja voznja, Vozilo vozilo, double trenutni_lat,
      double trenutni_lon, LocalDateTime pocetak) {
    SegmentVoznje segmentVoznje = new SegmentVoznje();
    segmentVoznje.setPaket(null);
    segmentVoznje.setOd_lat(trenutni_lat);
    segmentVoznje.setOd_lon(trenutni_lon);
    segmentVoznje.setDo_lat(Tvrtka.dohvatiInstancu().getGps_lat());
    segmentVoznje.setDo_lon(Tvrtka.dohvatiInstancu().getGps_lon());
    segmentVoznje.inicijaliziraj(vozilo);
    segmentVoznje.setVrijeme_pocetka(pocetak);
    pocetak = pocetak.plusSeconds(segmentVoznje.dohvatiTrajanjeSekundeLong());
    segmentVoznje.setVrijeme_kraja(pocetak);
    voznja.dodajSegment(segmentVoznje);
    return voznja;
  }

  private List<Paket> poredajPaketePoNajblizojDostavi(List<Paket> paketi) {
    List<Paket> paketi_temp = paketi;
    List<Paket> poredani_paketi = new ArrayList<>();
    double trenutni_lat = Tvrtka.dohvatiInstancu().getGps_lat();
    double trenutni_lon = Tvrtka.dohvatiInstancu().getGps_lon();
    Paket odabrani_paket;

    while (!paketi_temp.isEmpty()) {
      odabrani_paket = izracunajNajbliziPaket(paketi_temp, trenutni_lat, trenutni_lon);
      if (odabrani_paket == null) {
        return poredani_paketi;
      }
      paketi_temp.remove(odabrani_paket);
      poredani_paketi.add(odabrani_paket);
      String[] koordinate = izracunajKoordinatePaketa(odabrani_paket).trim().split(" ");
      trenutni_lat = Double.parseDouble(koordinate[0]);
      trenutni_lon = Double.parseDouble(koordinate[1]);
    }
    return poredani_paketi;
  }

  private Paket izracunajNajbliziPaket(List<Paket> paketi, double trenutni_lat,
      double trenutni_lon) {
    double udaljenost;
    double paket_lat;
    double paket_lon;
    double lat;
    double lon;
    double najmanja_udaljenost = 9999999;
    if (paketi.size() == 1) {
      return paketi.get(0);
    }
    Paket odabrani_paket = null;
    for (Paket paket : paketi) {
      String[] koordinate = izracunajKoordinatePaketa(paket).trim().split(" ");
      paket_lat = Double.parseDouble(koordinate[0]);
      paket_lon = Double.parseDouble(koordinate[1]);
      lat = Math.abs(paket_lat - trenutni_lat);
      lon = Math.abs(paket_lon - trenutni_lon);
      udaljenost = Math.sqrt((lat) * (lat) + (lon) * (lon));
      if (udaljenost < najmanja_udaljenost) {
        najmanja_udaljenost = udaljenost;
        odabrani_paket = paket;
      }
    }
    return odabrani_paket;
  }

  private String izracunajKoordinatePaketa(Paket paket) {
    Ulica ulica = paket.getPrimatelj().getUlica();
    double lat = Math.abs(ulica.getLat_2() - ulica.getLat_1());
    double lon = Math.abs(ulica.getLon_2() - ulica.getLon_1());
    double udaljenost = Math.sqrt((lat) * (lat) + (lon) * (lon));
    double postotak =
        ((double) paket.getPrimatelj().getKucni_broj()) / ulica.getNajveci_kucni_broj();
    double udaljenost2 = udaljenost * postotak;
    double sin_alfa = lat / udaljenost;
    double cos_alfa = lon / udaljenost;
    double lat_paket = sin_alfa * udaljenost2;
    double lon_paket = cos_alfa * udaljenost2;
    lat_paket = ulica.getLat_1() + lat_paket;
    lon_paket = ulica.getLon_1() + lon_paket;
    return "" + lat_paket + " " + lon_paket + "";
  }

  @Override
  public void rad(LocalDateTime pocetak, LocalDateTime kraj, List<Paket> evidencija_paketa,
      Vozilo vozilo, List<Voznja> voznje, List<Paket> paketi_za_dostavu) {
    if (!voznje.isEmpty() && voznje != null) {
      Voznja voznja = voznje.get(voznje.size() - 1);
      for (SegmentVoznje segment : voznja.getSegmentiVoznje()) {
        if (segment.getVrijeme_kraja().minusSeconds(1).isBefore(kraj)
            && segment.getVrijeme_kraja().plusSeconds(1).isAfter(pocetak)) {
          if (segment.getPaket() == null) {
            System.out.print("Vozilo " + vozilo.getOpis() + " se vratilo u ured.");
            if (vozilo.getPrihod() > 0) {
              System.out.println(" Sa sobom je donijelo " + vozilo.getPrihod() + "€");
            } else {
              System.out.println("");
            }
            Tvrtka.dohvatiInstancu().azurirajPrihod(vozilo.getPrihod());
            vozilo.setPrihod(0);
            vozilo.ocistiPakete();
          } else if (segment.getPaket().getStatus_isporuke().compareTo("Ukrcan") == 0
              && !paketi_za_dostavu.isEmpty()) {
            Paket paket = paketi_za_dostavu.get(paketi_za_dostavu.indexOf(segment.getPaket()));
            paket.setStatus_isporuke("Isporučen");
            paket.setVrijeme_preuzimanja(segment.getVrijeme_kraja());
            Paket ev_paket = evidencija_paketa.get(evidencija_paketa.indexOf(segment.getPaket()));
            ev_paket.setVrijeme_preuzimanja(segment.getVrijeme_kraja());
            ev_paket.setStatus_isporuke("Isporučen");
            System.out.print("Paket " + paket.getOznaka() + " je dostavljen na adresu '"
                + paket.getPrimatelj().getUlica().getNaziv().trim() + " "
                + paket.getPrimatelj().getKucni_broj() + ", "
                + paket.getPrimatelj().getGrad().getNaziv().trim() + "' u "
                + paket.getVrijeme_preuzimanja()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
            if (paket.getUsluga_dostave().compareTo("P") == 0) {
              vozilo.setPrihod(vozilo.getPrihod() + paket.getIznos_pouzeca());
              System.out.println(". Naplaćena je dostava u iznosu " + paket.getIznos_pouzeca());
            } else {
              System.out.println("");
            }
            ev_paket.paketObavijestavanje.obavijestiSlusace(paket, "preuzimanje");
          }
          vozilo.setOdvozeni_km(vozilo.getOdvozeni_km() + segment.getUdaljenost());
        }
      }
    }

  }

}
