package rmilosevi_zadaca_2;

import java.util.List;

public class Mjesto extends Kompozit {

  private Integer id;
  private String naziv;

  public Mjesto(Integer id, String naziv, List<Komponent> ulice) {
    super();
    this.id = id;
    this.naziv = naziv;
    this.djeca = ulice;
  }

  @Override
  public void naziv(boolean svaDjeca) {
    if (svaDjeca) {
      System.out.println("    " + this.naziv);
      for (Komponent dijete : djeca) {
        dijete.naziv(svaDjeca);
      }
    } else {
      System.out.println("    " + this.naziv);
    }
  }

  public List<Komponent> dohvatiUlice() {
    List<Komponent> lista = djeca;
    return lista;
  }

  public void dodajUlicu(Komponent ulica) {
    djeca.add(ulica);
  }

  public void ukloniUlicu(Komponent ulica) {
    djeca.remove(ulica);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNaziv() {
    return naziv;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

}
