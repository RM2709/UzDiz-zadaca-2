package rmilosevi_zadaca_2;

public class Ulica implements Komponent {

  private Integer id;
  private String naziv;
  private Double lat_1;
  private Double lon_1;
  private Double lat_2;
  private Double lon_2;
  private Integer najveci_kucni_broj;

  public Ulica(Integer id, String naziv, Double lat_1, Double lon_1, Double lat_2, Double lon_2,
      Integer najveci_kucni_broj) {
    super();
    this.id = id;
    this.naziv = naziv;
    this.lat_1 = lat_1;
    this.lon_1 = lon_1;
    this.lat_2 = lat_2;
    this.lon_2 = lon_2;
    this.najveci_kucni_broj = najveci_kucni_broj;
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

  public Double getLat_1() {
    return lat_1;
  }

  public void setLat_1(Double lat_1) {
    this.lat_1 = lat_1;
  }

  public Double getLon_1() {
    return lon_1;
  }

  public void setLon_1(Double lon_1) {
    this.lon_1 = lon_1;
  }

  public Double getLat_2() {
    return lat_2;
  }

  public void setLat_2(Double lat_2) {
    this.lat_2 = lat_2;
  }

  public Double getLon_2() {
    return lon_2;
  }

  public void setLon_2(Double lon_2) {
    this.lon_2 = lon_2;
  }

  public Integer getNajveci_kucni_broj() {
    return najveci_kucni_broj;
  }

  public void setNajveci_kucni_broj(Integer najveci_kucni_broj) {
    this.najveci_kucni_broj = najveci_kucni_broj;
  }


  @Override
  public void naziv(boolean svaDjeca) {
    System.out.println("        " + this.naziv);
  }


}
