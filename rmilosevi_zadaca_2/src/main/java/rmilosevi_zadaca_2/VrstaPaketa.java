package rmilosevi_zadaca_2;

public record VrstaPaketa(String oznaka, String opis, double visina, double sirina, double duzina,
    double maksimalna_tezina, double cijena, double cijena_hitno, double cijenaP, double cijenaT) {

  public String oznaka() {
    return oznaka;
  }

  public String opis() {
    return opis;
  }

  public double visina() {
    return visina;
  }

  public double duzina() {
    return duzina;
  }

  public double sirina() {
    return sirina;
  }

  public double maksimalna_tezina() {
    return maksimalna_tezina;
  }

  public double cijena() {
    return cijena;
  }

  public double cijena_hitno() {
    return cijena_hitno;
  }

  public double cijenaP() {
    return cijenaP;
  }

  public double cijenaT() {
    return cijenaT;
  }


}
