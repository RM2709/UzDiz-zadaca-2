package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.List;

public class NijeAktivno implements Status {

  @Override
  public List<Voznja> zapocniVoznju(List<Voznja> voznje, Vozilo vozilo,
      LocalDateTime vrijeme_pocetka_voznje, List<Paket> paketi_za_dostavu) {
    return voznje;
  }

  @Override
  public void rad(LocalDateTime pocetak, LocalDateTime kraj, List<Paket> evidencija_paketa,
      Vozilo vozilo, List<Voznja> voznje, List<Paket> paketi_za_dostavu) {

  }

}
