package rmilosevi_zadaca_2;

import java.time.LocalDateTime;

public class SegmentVoznje implements VisitedElement {

  private double od_lat;
  private double od_lon;
  private double do_lat;
  private double do_lon;
  private double udaljenost;
  private LocalDateTime vrijeme_pocetka;
  private LocalDateTime vrijeme_kraja;
  private double trajanje_voznje;
  private double trajanje_isporuke;
  private Paket paket;

  public void inicijaliziraj(Vozilo vozilo) {
    double lat = Math.abs(do_lat - od_lat);
    double lon = Math.abs(do_lon - od_lon);
    udaljenost = Math.sqrt((lat) * (lat) + (lon) * (lon)) * 100;
    trajanje_voznje = udaljenost / vozilo.getProsjecna_brzina();
    if (paket != null) {
      trajanje_isporuke = ((double) Tvrtka.dohvatiInstancu().getVrijeme_isporuke()) / 60;
    } else {
      trajanje_isporuke = 0;
    }
  }

  public long dohvatiTrajanjeSekundeLong() {
    double trajanje = (trajanje_voznje + trajanje_isporuke) * 3600;
    return (long) trajanje;
  }

  public double getOd_lat() {
    return od_lat;
  }

  public void setOd_lat(double od_lat) {
    this.od_lat = od_lat;
  }

  public double getOd_lon() {
    return od_lon;
  }

  public void setOd_lon(double od_lon) {
    this.od_lon = od_lon;
  }

  public double getDo_lat() {
    return do_lat;
  }

  public void setDo_lat(double do_lat) {
    this.do_lat = do_lat;
  }

  public double getDo_lon() {
    return do_lon;
  }

  public void setDo_lon(double do_lon) {
    this.do_lon = do_lon;
  }

  public double getUdaljenost() {
    return udaljenost;
  }

  public void setUdaljenost(double udaljenost) {
    this.udaljenost = udaljenost;
  }

  public LocalDateTime getVrijeme_pocetka() {
    return vrijeme_pocetka;
  }

  public void setVrijeme_pocetka(LocalDateTime vrijeme_pocetka) {
    this.vrijeme_pocetka = vrijeme_pocetka;
  }

  public LocalDateTime getVrijeme_kraja() {
    return vrijeme_kraja;
  }

  public void setVrijeme_kraja(LocalDateTime vrijeme_kraja) {
    this.vrijeme_kraja = vrijeme_kraja;
  }

  public double getTrajanje_voznje() {
    return trajanje_voznje;
  }

  public void setTrajanje_voznje(double trajanje_voznje) {
    this.trajanje_voznje = trajanje_voznje;
  }

  public double getTrajanje_isporuke() {
    return trajanje_isporuke;
  }

  public void setTrajanje_isporuke(double trajanje_isporuke) {
    this.trajanje_isporuke = trajanje_isporuke;
  }

  public Paket getPaket() {
    return paket;
  }

  public void setPaket(Paket paket) {
    this.paket = paket;
  }

  @Override
  public void prihvatiPosjet(Visitor visitor) {
    visitor.posjetiSegment(this);
  }



}
