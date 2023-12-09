package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UredZaDostavu {

  private List<Vozilo> vozila;
  private List<Podrucje> podrucja;

  public UredZaDostavu(List<Vozilo> vozila, List<Podrucje> podrucja) {
    this.vozila = vozila;
    this.podrucja = podrucja;
  }

  public List<Vozilo> getVozila() {
    return vozila;
  }

  public void setVozila(List<Vozilo> vozila) {
    this.vozila = vozila;
  }



  public void rad(LocalDateTime pocetak, LocalDateTime kraj, List<Paket> paketi,
      boolean unutarRadnogVremena) {
    for (Vozilo vozilo : vozila) {
      vozilo.rad(pocetak, kraj, paketi);
    }
    if (kraj.getMinute() == 0 && kraj.getSecond() == 0 && unutarRadnogVremena) {
      ukrcajPakete(dohvatiOdredenePakete(paketi, true), kraj);
      ukrcajPakete(dohvatiOdredenePakete(paketi, false), kraj);
      posaljiVozila(kraj);
    } else if (kraj.getHour() > pocetak.getHour() && unutarRadnogVremena) {
      LocalDateTime puni_sat = pocetak.withMinute(0).withSecond(0);
      ukrcajPakete(dohvatiOdredenePakete(paketi, true), puni_sat);
      ukrcajPakete(dohvatiOdredenePakete(paketi, false), puni_sat);
      posaljiVozila(puni_sat);
    }
  }

  private void posaljiVozila(LocalDateTime puni_sat) {
    for (Vozilo vozilo : vozila) {
      if (!vozilo.dohvatiPakete().isEmpty() && !vozilo.naVoznjiJe(puni_sat)) {
        vozilo.zapocniVoznju(puni_sat);
      }
    }
  }

  private List<Paket> dohvatiOdredenePakete(List<Paket> paketi, boolean hitni) {
    List<Paket> odabrani_paketi = new ArrayList<>();
    for (Paket paket : paketi) {
      if (hitni) {
        if (paket.getUsluga_dostave().compareTo("H") == 0) {
          odabrani_paketi.add(paket);
        }
      } else {
        if (paket.getUsluga_dostave().compareTo("H") != 0) {
          odabrani_paketi.add(paket);
        }
      }
    }
    return odabrani_paketi;
  }

  private void ukrcajPakete(List<Paket> paketi, LocalDateTime vrijeme) {
    Podrucje podrucje_paketa = null;
    for (Paket paket : paketi) {
      if (paket.getStatus_isporuke().trim().compareTo("Zaprimljen") == 0) {
        for (Podrucje podrucje : podrucja) {
          if (podrucje.dohvatiSveUlice().contains(paket.getPrimatelj().getUlica())) {
            podrucje_paketa = podrucje;
          }
        }
        List<Vozilo> vozila_za_podrucje = vozilaZaPodrucje(vozila, podrucje_paketa, vrijeme);
        Vozilo odabrano_vozilo = null;
        if (!vozila_za_podrucje.isEmpty()) {
          for (Vozilo vozilo : vozila_za_podrucje) {
            if (vozilo.imaLiMjesta(paket) && !vozilo.dohvatiPakete().isEmpty()
                && podrucje_paketa.dohvatiSveUlice()
                    .contains(vozilo.dohvatiPakete().get(0).getPrimatelj().getUlica())) {
              odabrano_vozilo = vozilo;
            }
          }
          if (odabrano_vozilo == null) {
            odabrano_vozilo =
                najveceRangiranoVoziloZaPodrucje(vozila_za_podrucje, podrucje_paketa, paket);
          }
        }
        if (odabrano_vozilo != null) {
          ukrcajPaket(odabrano_vozilo, paket, vrijeme);
        }
      }
    }
  }

  private void ukrcajPaket(Vozilo odabrano_vozilo, Paket paket, LocalDateTime vrijeme) {
    System.out
        .println("Paket " + paket.getOznaka() + " je ukrcan u vozilo " + odabrano_vozilo.getOpis()
            + " u " + vrijeme.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
    paket.paketObavijestavanje.obavijestiSlusace(paket, "ukrcaj");
    paket.setStatus_isporuke("Ukrcan");
    odabrano_vozilo.dodajPaket(paket);
  }

  private List<Vozilo> vozilaZaPodrucje(List<Vozilo> vozila, Podrucje podrucje,
      LocalDateTime vrijeme) {
    List<Vozilo> vozila_za_podrucje = new ArrayList<>();
    for (Vozilo vozilo : vozila) {
      if (vozilo.getPodrucja_po_rangu().contains(podrucje)
          && vozilo.getStatus().getClass() == Aktivno.class && !vozilo.naVoznjiJe(vrijeme)) {
        vozila_za_podrucje.add(vozilo);
      }
    }
    return vozila_za_podrucje;
  }

  private Vozilo najveceRangiranoVoziloZaPodrucje(List<Vozilo> vozila, Podrucje podrucje_paketa,
      Paket paket) {
    Vozilo odabrano_vozilo = null;
    Integer najmanja_vrijednost = 999999999;
    for (Vozilo vozilo : vozila) {
      if (vozilo.getPodrucja_po_rangu().contains(podrucje_paketa)) {
        int rang = vozilo.getPodrucja_po_rangu().indexOf(podrucje_paketa);
        if (rang < najmanja_vrijednost && vozilo.imaLiMjesta(paket)
            && vozilo.dohvatiPakete().isEmpty()) {
          odabrano_vozilo = vozilo;
          najmanja_vrijednost = rang;
        }
      }
    }
    return odabrano_vozilo;
  }

}
