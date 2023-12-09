package rmilosevi_zadaca_2;

import java.time.LocalDateTime;

public class Paket {

  private String oznaka;
  private LocalDateTime vrijeme_prijema;
  private LocalDateTime vrijeme_preuzimanja;
  private Osoba posiljatelj;
  private Osoba primatelj;
  private VrstaPaketa vrsta;
  private double visina;
  private double sirina;
  private double duzina;
  private double tezina;
  private String usluga_dostave;
  private double iznos_pouzeca;
  private double iznos_dostave;
  private String status_isporuke;
  PaketObavijestavanje paketObavijestavanje;

  public Paket() {
    super();
    paketObavijestavanje = new PaketObavijestavanje();
  }

  public String getOznaka() {
    return oznaka;
  }

  public void setOznaka(String oznaka) {
    this.oznaka = oznaka;
  }

  public LocalDateTime getVrijeme_prijema() {
    return vrijeme_prijema;
  }

  public void setVrijeme_prijema(LocalDateTime vrijeme_prijema) {
    this.vrijeme_prijema = vrijeme_prijema;
  }

  public Osoba getPosiljatelj() {
    return posiljatelj;
  }

  public void setPosiljatelj(Osoba posiljatelj) {
    this.posiljatelj = posiljatelj;
  }

  public Osoba getPrimatelj() {
    return primatelj;
  }

  public void setPrimatelj(Osoba primatelj) {
    this.primatelj = primatelj;
  }

  public VrstaPaketa getVrsta() {
    return vrsta;
  }

  public void setVrsta(VrstaPaketa vrsta) {
    this.vrsta = vrsta;
  }

  public double getVisina() {
    return visina;
  }

  public void setVisina(double visina) {
    this.visina = visina;
  }

  public double getSirina() {
    return sirina;
  }

  public void setSirina(double sirina) {
    this.sirina = sirina;
  }

  public double getDuzina() {
    return duzina;
  }

  public void setDuzina(double duzina) {
    this.duzina = duzina;
  }

  public double getTezina() {
    return tezina;
  }

  public void setTezina(double tezina) {
    this.tezina = tezina;
  }

  public String getUsluga_dostave() {
    return usluga_dostave;
  }

  public void setUsluga_dostave(String usluga_dostave) {
    this.usluga_dostave = usluga_dostave;
  }

  public double getIznos_pouzeca() {
    return iznos_pouzeca;
  }

  public void setIznos_pouzeca(double iznos_pouzeca) {
    this.iznos_pouzeca = iznos_pouzeca;
  }

  public LocalDateTime getVrijeme_preuzimanja() {
    return vrijeme_preuzimanja;
  }

  public void setVrijeme_preuzimanja(LocalDateTime vrijeme_preuzimanja) {
    this.vrijeme_preuzimanja = vrijeme_preuzimanja;
  }

  public String getStatus_isporuke() {
    return status_isporuke;
  }

  public void setStatus_isporuke(String status_isporuke) {
    this.status_isporuke = status_isporuke;
  }

  public double getIznos_dostave() {
    return iznos_dostave;
  }

  public void setIznos_dostave(double iznos_dostave) {
    this.iznos_dostave = iznos_dostave;
  }

}
