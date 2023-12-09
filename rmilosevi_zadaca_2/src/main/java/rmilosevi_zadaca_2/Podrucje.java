package rmilosevi_zadaca_2;

import java.util.ArrayList;
import java.util.List;

public class Podrucje extends Kompozit {

  private Integer id;

  public Podrucje(Integer id) {
    super();
    this.id = id;
    this.djeca = new ArrayList<>();
  }

  @Override
  public void naziv(boolean svaDjeca) {
    System.out.println("Područje " + this.id);
    for (Komponent dijete : djeca) {
      if (djeca.indexOf(dijete) + 1 < djeca.size()) {
        if (dijete.getClass() == Mjesto.class
            && djeca.get(djeca.indexOf(dijete) + 1).getClass() != Ulica.class) {
          dijete.naziv(true);
        } else {
          dijete.naziv(false);
        }
      } else if (djeca.indexOf(dijete) + 1 == djeca.size()) {
        if (dijete.getClass() == Mjesto.class) {
          dijete.naziv(true);
        } else {
          dijete.naziv(false);
        }
      } else {
        dijete.naziv(false);
      }
    }
  }

  public List<Ulica> dohvatiSveUlice() {
    List<Ulica> ulicePodrucja = new ArrayList<>();
    for (Komponent dijete : djeca) {
      if (djeca.indexOf(dijete) + 1 < djeca.size()) {
        if (dijete.getClass() == Mjesto.class
            && djeca.get(djeca.indexOf(dijete) + 1).getClass() != Ulica.class) {
          for (Komponent komponent : ((Mjesto) dijete).dohvatiUlice()) {
            ulicePodrucja.add((Ulica) komponent);
          }
        } else if (dijete.getClass() != Mjesto.class) {
          ulicePodrucja.add((Ulica) dijete);
        }
      } else if (djeca.indexOf(dijete) + 1 >= djeca.size()) {
        if (dijete.getClass() == Mjesto.class) {
          for (Komponent komponent : ((Mjesto) dijete).dohvatiUlice()) {
            ulicePodrucja.add((Ulica) komponent);
          }
        } else {
          ulicePodrucja.add((Ulica) dijete);
        }
      }
    }
    return ulicePodrucja;
  }


  public void dodajDijete(Komponent dijete) {
    if (!djeca.contains(dijete)) {
      djeca.add(dijete);
    }
  }

  public void ukloniDijete(Komponent dijete) {
    if (djeca.contains(dijete)) {
      djeca.remove(dijete);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

}
