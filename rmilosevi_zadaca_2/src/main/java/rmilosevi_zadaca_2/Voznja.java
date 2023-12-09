package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Voznja implements VisitedElement {

  private List<SegmentVoznje> segmentiVoznje;
  private LocalDateTime vrijeme_polaska;
  private LocalDateTime vrijeme_povratka;
  private double postotakTezine;
  private double postotakProstora;

  public Voznja() {
    this.segmentiVoznje = new ArrayList<>();
  }

  public void dodajSegment(SegmentVoznje segment) {
    if (segment.getOd_lat() == Tvrtka.dohvatiInstancu().getGps_lat()
        && segment.getOd_lon() == Tvrtka.dohvatiInstancu().getGps_lon()) {
      vrijeme_polaska = segment.getVrijeme_pocetka();
    } else if (segment.getDo_lat() == Tvrtka.dohvatiInstancu().getGps_lat()
        && segment.getDo_lon() == Tvrtka.dohvatiInstancu().getGps_lon()) {
      vrijeme_povratka = segment.getVrijeme_kraja();
    }
    segmentiVoznje.add(segment);
  }

  public double getOdvozeni_km() {
    double odvozeni_km = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      odvozeni_km += segmentVoznje.getUdaljenost();
    }
    return odvozeni_km;
  }

  public int brojHitnihPaketa() {
    int broj = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      if (segmentVoznje.getPaket() != null) {
        if (segmentVoznje.getPaket().getUsluga_dostave().compareTo("H") == 0) {
          broj++;
        }
      }
    }
    return broj;
  }

  public int brojObicnihPaketa() {
    int broj = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      if (segmentVoznje.getPaket() != null) {
        if (segmentVoznje.getPaket().getUsluga_dostave().compareTo("H") != 0) {
          broj++;
        }
      }
    }
    return broj;
  }

  public int brojIsporucenihPaketa() {
    int broj = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      if (segmentVoznje.getPaket() != null) {
        if (segmentVoznje.getPaket().getStatus_isporuke().compareTo("Isporučen") == 0) {
          broj++;
        }
      }
    }
    return broj;
  }

  public void izracunajPostotakTezine(Vozilo vozilo) {
    double ukupna_tezina = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      if (segmentVoznje.getPaket() != null) {
        ukupna_tezina += segmentVoznje.getPaket().getTezina();
      }
    }
    postotakTezine = (ukupna_tezina / vozilo.getKapacitet_tezine()) * 100;
  }

  public void izracunajPostotakProstora(Vozilo vozilo) {
    double ukupan_prostor = 0;
    for (SegmentVoznje segmentVoznje : segmentiVoznje) {
      if (segmentVoznje.getPaket() != null) {
        ukupan_prostor += (segmentVoznje.getPaket().getDuzina()
            * segmentVoznje.getPaket().getSirina() * segmentVoznje.getPaket().getVisina());
      }
    }
    postotakProstora = (ukupan_prostor / vozilo.getKapacitet_prostora()) * 100;
  }



  public double getPostotakTezine() {
    return postotakTezine;
  }

  public void setPostotakTezine(double postotakTezine) {
    this.postotakTezine = postotakTezine;
  }

  public double getPostotakProstora() {
    return postotakProstora;
  }

  public void setPostotakProstora(double postotakProstora) {
    this.postotakProstora = postotakProstora;
  }

  public List<SegmentVoznje> getSegmentiVoznje() {
    return segmentiVoznje;
  }

  public LocalDateTime getVrijeme_polaska() {
    return vrijeme_polaska;
  }

  public void setVrijeme_polaska(LocalDateTime vrijeme_polaska) {
    this.vrijeme_polaska = vrijeme_polaska;
  }

  public LocalDateTime getVrijeme_povratka() {
    return vrijeme_povratka;
  }

  public void setVrijeme_povratka(LocalDateTime vrijeme_povratka) {
    this.vrijeme_povratka = vrijeme_povratka;
  }

  @Override
  public void prihvatiPosjet(Visitor visitor) {
    visitor.posjetiVoznju(this);
  }


}
