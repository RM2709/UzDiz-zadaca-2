package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.List;

public interface Status {

  public List<Voznja> zapocniVoznju(List<Voznja> voznje, Vozilo vozilo,
      LocalDateTime vrijeme_pocetka_voznje, List<Paket> paketi_za_dostavu);

  public void rad(LocalDateTime pocetak, LocalDateTime kraj, List<Paket> evidencija_paketa,
      Vozilo vozilo, List<Voznja> voznje, List<Paket> paketi_za_dostavu);

}
