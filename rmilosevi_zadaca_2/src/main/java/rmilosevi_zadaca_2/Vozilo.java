package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vozilo implements VisitedElement {

  private String registracija;
  private String opis;
  private Double kapacitet_tezine;
  private Double kapacitet_prostora;
  private Integer redoslijed;
  private Integer prosjecna_brzina;
  private List<Podrucje> podrucja_po_rangu;
  private Status status;
  private List<Paket> paketi_za_dostavu;
  private List<Voznja> voznje;
  private double odvozeni_km;
  private double prihod;


  public Vozilo(String registracija, String opis, Double kapacitet_tezine,
      Double kapacitet_prostora, Integer redoslijed, Integer prosjecna_brzina,
      List<Podrucje> podrucja, Status status) {
    super();
    this.registracija = registracija;
    this.opis = opis;
    this.kapacitet_tezine = kapacitet_tezine;
    this.kapacitet_prostora = kapacitet_prostora;
    this.redoslijed = redoslijed;
    this.prosjecna_brzina = prosjecna_brzina;
    this.podrucja_po_rangu = podrucja;
    this.status = status;
    this.paketi_za_dostavu = new ArrayList<>();
    this.voznje = new ArrayList<>();
    this.odvozeni_km = 0;
    this.prihod = 0;
  }

  public void rad(LocalDateTime pocetak, LocalDateTime kraj, List<Paket> evidencija_paketa) {
    status.rad(pocetak, kraj, evidencija_paketa, this, voznje, paketi_za_dostavu);
  }

  public boolean naVoznjiJe(LocalDateTime vrijeme) {
    if (voznje.size() > 0) {
      Voznja posljednja_voznja = voznje.get(voznje.size() - 1);
      if (posljednja_voznja.getVrijeme_povratka().isBefore(vrijeme)) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public double getPrihod() {
    return prihod;
  }

  public void setPrihod(double prihod) {
    this.prihod = prihod;
  }

  public void zapocniVoznju(LocalDateTime vrijeme_pocetka_voznje) {
    voznje = status.zapocniVoznju(voznje, this, vrijeme_pocetka_voznje, paketi_za_dostavu);
  }

  public boolean imaLiMjesta(Paket paket) {
    if ((trenutnaTezina() + paket.getTezina() >= kapacitet_tezine) || (trenutniProstor()
        + (paket.getVisina() * paket.getDuzina() * paket.getSirina()) >= kapacitet_prostora)) {
      return false;
    } else {
      return true;
    }
  }

  public int brojHitnihPaketa() {
    int broj = 0;
    for (Paket paket : paketi_za_dostavu) {
      if (paket.getUsluga_dostave().compareTo("H") == 0) {
        broj++;
      }
    }
    return broj;
  }

  public int brojObicnihPaketa() {
    int broj = 0;
    for (Paket paket : paketi_za_dostavu) {
      if (paket.getUsluga_dostave().compareTo("H") != 0) {
        broj++;
      }
    }
    return broj;
  }

  public int brojVoznji() {
    return voznje.size();
  }

  public int brojIsporucenihPaketa() {
    int broj = 0;
    for (Paket paket : paketi_za_dostavu) {
      if (paket.getStatus_isporuke().compareTo("Isporučen") == 0) {
        broj++;
      }
    }
    return broj;
  }

  public double getOdvozeni_km() {
    return odvozeni_km;
  }

  public void setOdvozeni_km(double odvozeni_km) {
    this.odvozeni_km = odvozeni_km;
  }

  public void dodajPaket(Paket paket) {
    paketi_za_dostavu.add(paket);
  }

  public void ocistiPakete() {
    paketi_za_dostavu.clear();
  }

  public List<Paket> dohvatiPakete() {
    return paketi_za_dostavu;
  }

  public void postaviPakete(List<Paket> paketi) {
    this.paketi_za_dostavu = paketi;
  }

  public double trenutnaTezina() {
    double tezina = 0;
    for (Paket paket : paketi_za_dostavu) {
      tezina += paket.getTezina();
    }
    return tezina;
  }

  public boolean imaLiHitniPaket() {
    for (Paket paket : paketi_za_dostavu) {
      if (paket.getUsluga_dostave().compareTo("H") == 0
          && paket.getStatus_isporuke().compareTo("Ukrcan") == 0) {
        return true;
      }
    }
    return false;
  }

  public double trenutniProstor() {
    double prostor = 0;
    for (Paket paket : paketi_za_dostavu) {
      prostor += (paket.getVisina() * paket.getDuzina() * paket.getSirina());
    }
    return prostor;
  }

  public List<Voznja> getVoznje() {
    return voznje;
  }

  public void setVoznje(List<Voznja> voznje) {
    this.voznje = voznje;
  }

  public String getRegistracija() {
    return registracija;
  }

  public void setRegistracija(String registracija) {
    this.registracija = registracija;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public Double getKapacitet_tezine() {
    return kapacitet_tezine;
  }

  public void setKapacitet_tezine(Double kapacitet_tezine) {
    this.kapacitet_tezine = kapacitet_tezine;
  }

  public Double getKapacitet_prostora() {
    return kapacitet_prostora;
  }

  public void setKapacitet_prostora(Double kapacitet_prostora) {
    this.kapacitet_prostora = kapacitet_prostora;
  }

  public Integer getRedoslijed() {
    return redoslijed;
  }

  public void setRedoslijed(Integer redoslijed) {
    this.redoslijed = redoslijed;
  }

  public Integer getProsjecna_brzina() {
    return prosjecna_brzina;
  }

  public void setProsjecna_brzina(Integer prosjecna_brzina) {
    this.prosjecna_brzina = prosjecna_brzina;
  }

  public List<Podrucje> getPodrucja_po_rangu() {
    return podrucja_po_rangu;
  }

  public void setPodrucja_po_rangu(List<Podrucje> podrucja_po_rangu) {
    this.podrucja_po_rangu = podrucja_po_rangu;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public void prihvatiPosjet(Visitor visitor) {
    visitor.posjetiVozilo(this);

  }

}
