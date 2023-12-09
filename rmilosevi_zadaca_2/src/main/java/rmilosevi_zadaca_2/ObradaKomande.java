package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ObradaKomande implements ServisObradeKomande {

  Tvrtka tvrtka = Tvrtka.dohvatiInstancu();

  @Override
  public void obradiKomandu(String komanda) {
    Pattern uzorakVR = Pattern.compile("^VR [1-9]{1}[0-9]{0,1}$");
    Pattern uzorakPS = Pattern.compile("^PS [A-Ž]{2}[0-9]{3,4}[A-Z]{2} (A|NI|NA)$");
    Pattern uzorakDNP = Pattern.compile("^DNP (D|N)$");
    Pattern uzorakPO = Pattern.compile("^PO '[ -ž]{1,}' [0-Ž]{9} (D|N)$");
    Pattern uzorakVV = Pattern.compile("^VV [A-Ž]{2}[0-9]{3,4}[A-Z]{2}$");
    Pattern uzorakVS = Pattern.compile("^VS [A-Ž]{2}[0-9]{3,4}[A-Z]{2} [0-9]{1,}$");
    if (komanda.compareTo("IP") == 0) {
      obradiIP();
    } else if (uzorakVR.matcher(komanda).find()) {
      obradiVR(komanda);
    } else if (komanda.compareTo("PP") == 0) {
      obradiPP();
    } else if (komanda.compareTo("SV") == 0) {
      obradiSV();
    } else if (komanda.compareTo("DN") == 0) {
      obradiDN();
    } else if (uzorakDNP.matcher(komanda).find()) {
      obradiDNP(komanda);
    } else if (uzorakVV.matcher(komanda).find()) {
      obradiVV(komanda);
    } else if (uzorakVS.matcher(komanda).find()) {
      obradiVS(komanda);
    } else if (uzorakPS.matcher(komanda).find()) {
      obradiPS(komanda);
    } else if (uzorakPO.matcher(komanda).find()) {
      obradiPO(komanda);
    } else {
      if (komanda.compareTo("Q") != 0) {
        System.out.println("Krivi unos komande");
      } else {
        System.out.println("Prekid rada programa");
      }
    }
  }

  private void obradiDNP(String komanda) {
    String da_ne = komanda.trim().split(" ")[1].trim();
    if (da_ne.compareTo("D") == 0) {
      if (tvrtka.pisiDnevnik() == true) {
        System.out.println("Pisanje u dnevnik je već uključeno");
      } else {
        tvrtka.postaviPisanjeDnevnika(true);
        System.out.println("Pisanje u dnevnik uključeno");
      }
    } else {
      if (tvrtka.pisiDnevnik() == false) {
        System.out.println("Pisanje u dnevnik je već isključeno");
      } else {
        tvrtka.postaviPisanjeDnevnika(false);
        System.out.println("Pisanje u dnevnik isključeno");
      }
    }

  }

  private void obradiDN() {
    String poravnajLijevo = "| %-20s | %-70s |%n";
    if (!tvrtka.getDnevnik().isEmpty()) {
      System.out.format(poravnajLijevo, "Vrijeme", "Komanda");
      for (SimpleEntry<LocalDateTime, String> zapis : tvrtka.getDnevnik()) {
        System.out.format(poravnajLijevo,
            zapis.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
            zapis.getValue());
      }
    } else {
      System.out.format("Dnevnik je prazan");
    }
  }

  private void obradiVS(String komanda) {
    String[] komanda_split = komanda.split(" ");
    Integer broj_voznje = Integer.parseInt(komanda_split[2].trim());
    boolean postoji = false;
    String poravnajLijevo = "| %-20s | %-20s | %-15s | %-13s | %-15s |%n";
    for (Vozilo vozilo : tvrtka.getUredZaDostavu().getVozila()) {
      if (vozilo.getRegistracija().compareTo(komanda_split[1].trim()) == 0) {
        postoji = true;
        if (vozilo.getStatus().getClass().getSimpleName().compareTo("Aktivno") == 0) {
          if (vozilo.brojVoznji() >= broj_voznje && broj_voznje > 0) {
            System.out.format(poravnajLijevo, "Vrijeme početka", "Vrijeme kraja", "Trajanje (min)",
                "Odvoženo km", "Paket");
            for (SegmentVoznje segmentVoznje : vozilo.getVoznje().get(broj_voznje - 1)
                .getSegmentiVoznje()) {
              tvrtka.getIspisVisitor().posjetiSegment(segmentVoznje);
            }
          } else {
            System.out.println("Vožnja s brojem '" + broj_voznje + "' ne postoji");
          }
        } else if (vozilo.getStatus().getClass().getSimpleName().compareTo("NijeAktivno") == 0) {
          System.out.println(
              "Vozilo " + vozilo.getRegistracija() + " " + vozilo.getOpis() + " je neaktivno!");
        } else {
          System.out.println(
              "Vozilo " + vozilo.getRegistracija() + " " + vozilo.getOpis() + " je neispravno!");
        }
      }
    }
    if (!postoji) {
      System.out.println("Vozilo s oznakom '" + komanda_split[1].trim() + "' ne postoji");
    }
  }

  private void obradiVV(String komanda) {
    String poravnajLijevo =
        "| %-20s | %-20s | %-15s | %-13s | %-15s | %-15s | %-17s | %-10s | %-10s |%n";
    boolean postoji = false;

    for (Vozilo vozilo : tvrtka.getUredZaDostavu().getVozila()) {
      if (vozilo.getRegistracija().compareTo(komanda.split(" ")[1].trim()) == 0) {
        postoji = true;
        if (vozilo.getStatus().getClass().getSimpleName().compareTo("Aktivno") == 0) {
          System.out.format(poravnajLijevo, "Vrijeme početka", "Vrijeme povratka", "Trajanje (min)",
              "Odvoženo km", "Hitni paketi", "Obični paketi", "Isporučeni paketi", "% težine",
              "% prostora");
          for (Voznja voznja : vozilo.getVoznje()) {
            voznja.izracunajPostotakProstora(vozilo);
            voznja.izracunajPostotakTezine(vozilo);
            tvrtka.getIspisVisitor().posjetiVoznju(voznja);
          }
        } else if (vozilo.getStatus().getClass().getSimpleName().compareTo("NijeAktivno") == 0) {
          System.out.println(
              "Vozilo " + vozilo.getRegistracija() + " " + vozilo.getOpis() + " je neaktivno!");
        } else {
          System.out.println(
              "Vozilo " + vozilo.getRegistracija() + " " + vozilo.getOpis() + " je neispravno!");
        }
      }
    }
    if (!postoji) {
      System.out.println("Vozilo s oznakom '" + komanda.split(" ")[1].trim() + "' ne postoji");
    }
  }

  private void obradiSV() {
    String poravnajLijevo =
        "| %-12s | %-20s | %-15s | %-13s | %-15s | %-15s | %-17s | %-10s | %-10s | %-12s |%n";
    System.out.format(poravnajLijevo, "Oznaka", "Naziv", "Status", "Odvoženi km", "Hitni paketi",
        "Obični paketi", "Isporučeni paketi", "% težine", "% prostora", "Broj vožnji");
    for (Vozilo vozilo : tvrtka.getUredZaDostavu().getVozila()) {
      tvrtka.getIspisVisitor().posjetiVozilo(vozilo);
    }
  }

  private void obradiPO(String komanda) {
    String[] komanda_dio = komanda.split("'");
    String string_osoba = komanda_dio[1].trim();
    String string_paket = komanda_dio[2].trim().split(" ")[0].trim();
    Osoba odabrana_osoba = null;
    Paket odabrani_paket = null;
    for (Osoba osoba : tvrtka.getOsobe()) {
      if (osoba.getNaziv().compareTo(string_osoba) == 0) {
        odabrana_osoba = osoba;
      }
    }
    if (odabrana_osoba == null) {
      System.out.println("Odabrana osoba ne postoji");
      return;
    }
    for (Paket paket : tvrtka.getUredZaPrijem().dohvatiEvidencijuPaketa()) {
      if (paket.getOznaka().compareTo(string_paket) == 0) {
        odabrani_paket = paket;
      }
    }
    if (odabrani_paket == null) {
      if (!postojiNezaprimljeniPaket(string_paket)) {
        System.out.println("Odabrani paket ne postoji");
      } else {
        prijaviOdjaviSlusanjeNezaprimljenogPaketa(komanda_dio[2].trim().split(" ")[1], string_osoba,
            string_paket);
      }
      return;
    }
    if (odabrana_osoba != odabrani_paket.getPosiljatelj()
        && odabrana_osoba != odabrani_paket.getPrimatelj()) {
      System.out.println("Odabrana osoba nije primatelj niti pošiljatelj odabranog paketa");
      return;
    }
    prijaviOdjaviSlusanje(komanda_dio, odabrana_osoba, odabrani_paket);
  }

  private void prijaviOdjaviSlusanjeNezaprimljenogPaketa(String d_n, String string_osoba,
      String string_paket) {
    if (d_n.compareTo("D") == 0) {
      prijaviSlusanjeNezaprimljenogPaketa(string_paket, string_osoba);
    } else if (d_n.compareTo("N") == 0) {
      odjaviSlusanjeNezaprimljenogPaketa(string_paket, string_osoba);
    }
  }

  private void odjaviSlusanjeNezaprimljenogPaketa(String string_paket, String string_osoba) {
    for (SimpleEntry<LocalDateTime, String> podatak : tvrtka.getPodaci_paketi()) {
      if (podatak.getValue().split(";")[0].trim().compareTo(string_paket) == 0) {
        if (podatak.getValue().split(";")[2].trim().compareTo(string_osoba) == 0) {
          if (tvrtka.getOdjave_slusanja()
              .contains(new SimpleEntry<String, String>(string_paket, string_osoba))) {
            System.out.println("Pošiljatelj '" + string_osoba
                + "' već ne prima obavijesti o paketu " + string_paket);
          } else {
            tvrtka
                .dodajOdjavu_slusanja(new SimpleEntry<String, String>(string_paket, string_osoba));
            System.out.println("Odjavljeno slušanje paketa " + string_paket + " za pošiljatelja '"
                + string_osoba + "'");
          }
          return;
        } else if (podatak.getValue().split(";")[3].trim().compareTo(string_osoba) == 0) {
          if (tvrtka.getOdjave_slusanja()
              .contains(new SimpleEntry<String, String>(string_paket, string_osoba))) {
            System.out.println("Primatelj '" + string_osoba + "' već ne prima obavijesti o paketu "
                + string_paket);
          } else {
            tvrtka
                .dodajOdjavu_slusanja(new SimpleEntry<String, String>(string_paket, string_osoba));
            System.out.println("Odjavljeno slušanje paketa " + string_paket + " za primatelja '"
                + string_osoba + "'");
          }
          return;
        }
      }
    }
    System.out.println("Odabrana osoba nije primatelj niti pošiljatelj odabranog paketa");

  }

  private void prijaviSlusanjeNezaprimljenogPaketa(String string_paket, String string_osoba) {
    for (SimpleEntry<LocalDateTime, String> podatak : tvrtka.getPodaci_paketi()) {
      if (podatak.getValue().split(";")[0].trim().compareTo(string_paket) == 0) {
        if (podatak.getValue().split(";")[2].trim().compareTo(string_osoba) == 0) {
          if (!tvrtka.getOdjave_slusanja()
              .contains(new SimpleEntry<String, String>(string_paket, string_osoba))) {
            System.out.println(
                "Pošiljatelj '" + string_osoba + "' već prima obavijesti o paketu " + string_paket);
          } else {
            tvrtka
                .ukloniOdjavu_slusanja(new SimpleEntry<String, String>(string_paket, string_osoba));
            System.out.println("Prijavljeno slušanje paketa " + string_paket + " za pošiljatelja '"
                + string_osoba + "'");
          }
          return;
        } else if (podatak.getValue().split(";")[3].trim().compareTo(string_osoba) == 0) {
          if (!tvrtka.getOdjave_slusanja()
              .contains(new SimpleEntry<String, String>(string_paket, string_osoba))) {
            System.out.println(
                "Primatelj '" + string_osoba + "' već prima obavijesti o paketu " + string_paket);
          } else {
            tvrtka
                .ukloniOdjavu_slusanja(new SimpleEntry<String, String>(string_paket, string_osoba));
            System.out.println("Prijavljeno slušanje paketa " + string_paket + " za primatelja '"
                + string_osoba + "'");
          }
          return;
        }
      }
    }
    System.out.println("Odabrana osoba nije primatelj niti pošiljatelj odabranog paketa");
  }

  private boolean postojiNezaprimljeniPaket(String string_paket) {
    for (SimpleEntry<LocalDateTime, String> podatak : tvrtka.getPodaci_paketi()) {
      if (podatak.getValue().split(";")[0].trim().compareTo(string_paket) == 0) {
        return true;
      }
    }
    return false;
  }

  private void prijaviOdjaviSlusanje(String[] komanda_dio, Osoba odabrana_osoba,
      Paket odabrani_paket) {
    if (komanda_dio[2].trim().split(" ")[1].compareTo("D") == 0) {
      odabrani_paket.paketObavijestavanje.prijaviSlusanje(odabrana_osoba);
      System.out.println("Prijavljeno slušanje paketa " + odabrani_paket.getOznaka() + " za "
          + ((odabrana_osoba == odabrani_paket.getPrimatelj() ? "primatelja" : "pošiljatelja"))
          + " '" + odabrana_osoba.getNaziv() + "'");
    } else if (komanda_dio[2].trim().split(" ")[1].compareTo("N") == 0) {
      odabrani_paket.paketObavijestavanje.odjaviSlusanje(odabrana_osoba);
      System.out.println("Odjavljeno slušanje paketa " + odabrani_paket.getOznaka() + " za "
          + ((odabrana_osoba == odabrani_paket.getPrimatelj() ? "primatelja" : "pošiljatelja"))
          + " '" + odabrana_osoba.getNaziv() + "'");
    }
  }

  private void obradiPS(String komanda) {
    String[] komanda_dio = komanda.split(" ");
    boolean postoji = false;
    for (Vozilo vozilo : tvrtka.getVozila()) {
      if (vozilo.getRegistracija().compareTo(komanda_dio[1]) == 0) {
        postoji = true;
        if ((vozilo.getStatus().getClass().getSimpleName().compareTo("Aktivno") == 0
            && komanda_dio[2].compareTo("A") == 0)
            || (vozilo.getStatus().getClass().getSimpleName().compareTo("NijeAktivno") == 0
                && komanda_dio[2].compareTo("NA") == 0)
            || (vozilo.getStatus().getClass().getSimpleName().compareTo("NijeIspravno") == 0
                && komanda_dio[2].compareTo("NI") == 0)) {
          System.out.println("Status vozila '" + komanda_dio[1] + "' je već jednak unesenom");
          continue;
        }
        switch (komanda_dio[2]) {
          case "A": {
            vozilo.setStatus(new Aktivno());
            break;
          }
          case "NA": {
            vozilo.setStatus(new NijeAktivno());
            break;
          }
          case "NI": {
            vozilo.setStatus(new NijeIspravno());
            break;
          }
        }
        System.out.println("Status vozila " + vozilo.getRegistracija().trim() + " postavljen na '"
            + vozilo.getStatus().getClass().getSimpleName() + "'");
      }
    }
    if (!postoji) {
      System.out.println("Vozilo s registarskom oznakom '" + komanda_dio[1] + "' nije pronađeno");
    }
  }

  private void obradiPP() {
    for (Podrucje podrucje : tvrtka.getPodrucja()) {
      podrucje.naziv(false);
    }

  }


  private void obradiVR(String komanda) {
    int broj_sati = Integer.parseInt(komanda.split(" ")[1]);
    LocalDateTime ciljano_vrijeme = tvrtka.getVirtualni_sat().plusHours(broj_sati);
    LocalDateTime pocetak;
    LocalDateTime kraj;
    boolean kraj_smjene = false;
    while (tvrtka.getVirtualni_sat().isBefore(ciljano_vrijeme)) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      pocetak = tvrtka.getVirtualni_sat().plusSeconds(0);
      if (tvrtka.getVirtualni_sat().plusSeconds(tvrtka.getMnozitelj_sekundi())
          .isAfter(ciljano_vrijeme)) {
        tvrtka.setVirtualni_sat(ciljano_vrijeme.plusSeconds(0));
      } else {
        tvrtka
            .setVirtualni_sat(tvrtka.getVirtualni_sat().plusSeconds(tvrtka.getMnozitelj_sekundi()));
      }
      kraj = tvrtka.getVirtualni_sat().plusSeconds(0);
      if (!pocetak.toLocalTime().plusSeconds(1).isAfter(tvrtka.getPocetak_rada())
          && kraj.toLocalTime().minusSeconds(1).isAfter(tvrtka.getPocetak_rada())) {
        pocetak = tvrtka.getPocetak_rada().atDate(pocetak.toLocalDate());
      }
      if (!kraj.toLocalTime().minusSeconds(1).isBefore(tvrtka.getKraj_rada())
          && pocetak.toLocalTime().minusSeconds(1).isBefore(tvrtka.getKraj_rada())) {
        kraj = tvrtka.getKraj_rada().atDate(pocetak.toLocalDate());
        kraj_smjene = true;
      }
      if (pocetak.plusSeconds(1).toLocalTime().isAfter(tvrtka.getPocetak_rada())
          && kraj.minusSeconds(1).toLocalTime().isBefore(tvrtka.getKraj_rada())) {
        tvrtka.getUredZaPrijem().rad(pocetak, kraj);
        tvrtka.getUredZaDostavu().rad(pocetak, kraj,
            tvrtka.getUredZaPrijem().dohvatiEvidencijuPaketa(), true);
      } else if (pocetak.plusSeconds(1).toLocalTime().isAfter(tvrtka.getPocetak_rada())
          && !kraj.minusSeconds(1).toLocalTime().isBefore(tvrtka.getKraj_rada())) {
        tvrtka.getUredZaDostavu().rad(pocetak, kraj,
            tvrtka.getUredZaPrijem().dohvatiEvidencijuPaketa(), false);
      }
      System.out.println(
          tvrtka.getVirtualni_sat().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
    }
    System.out
        .println("Do kraja rada je došlo zbog " + ((!kraj_smjene) ? "isteka vremena izvršavanja"
            : "kraja radnog vremena u " + tvrtka.getKraj_rada()));
  }

  private void obradiIP() {
    System.out.println("Virtualni sat: "
        + tvrtka.getVirtualni_sat().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
    System.out.println("Prikupljeno je " + tvrtka.prijaviPrihod() + "€");
    String poravnajLijevo = "| %-15s | %-30s | %-15s | %-15s | %-15s | %-30s | %-15s | %-15s|%n";
    System.out.format(poravnajLijevo, "Oznaka", "Vrijeme prijema", "Vrsta paketa", "Usluga dostave",
        "Status isporuke", "Vrijeme preuzimanja", "Iznos dostave", "Iznos pouzeća");

    for (Paket paket : tvrtka.getUredZaPrijem().dohvatiEvidencijuPaketa()) {
      if (paket.getStatus_isporuke().compareTo("Nezaprimljen") != 0) {
        System.out.format(poravnajLijevo, paket.getOznaka(),
            paket.getVrijeme_prijema().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
            paket.getVrsta().oznaka(), paket.getUsluga_dostave(), paket.getStatus_isporuke(),
            (paket.getVrijeme_preuzimanja() != null) ? paket.getVrijeme_preuzimanja()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")) : "Nije dostavljen",
            String.format("%.2f", (paket.getIznos_dostave())),
            String.format("%.2f", (paket.getIznos_pouzeca())));
      }
    }
  }

}
