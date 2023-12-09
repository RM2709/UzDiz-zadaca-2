package rmilosevi_zadaca_2;

import java.util.List;

public class Kompozit implements Komponent {

  protected List<Komponent> djeca;

  public List<Komponent> getDjeca() {
    return djeca;
  }

  public void setDjeca(List<Komponent> djeca) {
    this.djeca = djeca;
  }

  @Override
  public void naziv(boolean svaDjeca) {}

}
