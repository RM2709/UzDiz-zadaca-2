package rmilosevi_zadaca_2;

public class Osoba implements SlusacObavijesti {

  private String naziv;
  private Mjesto grad;
  private Ulica ulica;
  private Integer kucni_broj;

  public Osoba(String naziv, Integer kucni_broj) {
    super();
    this.naziv = naziv;
    this.kucni_broj = kucni_broj;
  }

  public String getNaziv() {
    return naziv;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

  public Mjesto getGrad() {
    return grad;
  }

  public void setGrad(Mjesto grad) {
    this.grad = grad;
  }

  public Ulica getUlica() {
    return ulica;
  }

  public void setUlica(Ulica ulica) {
    this.ulica = ulica;
  }

  public Integer getKucni_broj() {
    return kucni_broj;
  }

  public void setKucni_broj(Integer kucni_broj) {
    this.kucni_broj = kucni_broj;
  }

  @Override
  public void primiObavijest(Paket paket, String obavijest) {
    if (obavijest.compareTo("prijem") == 0) {
      System.out.println(naziv + " je obaviješten(a) o prijemu paketa " + paket.getOznaka());
    } else if (obavijest.compareTo("ukrcaj") == 0) {
      System.out.println(naziv + " je obaviješten(a) o ukrcaju paketa " + paket.getOznaka());
    } else if (obavijest.compareTo("preuzimanje") == 0) {
      System.out.println(naziv + " je obaviješten(a) o preuzimanju paketa " + paket.getOznaka());
    }

  }



}
