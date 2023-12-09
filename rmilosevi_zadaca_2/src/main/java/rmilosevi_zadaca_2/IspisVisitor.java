package rmilosevi_zadaca_2;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class IspisVisitor implements Visitor {

  @Override
  public void posjetiVozilo(Vozilo vozilo) {
    String poravnajLijevo =
        "| %-12s | %-20s | %-15s | %-13s | %-15s | %-15s | %-17s | %-10s | %-10s | %-12s |%n";
    System.out.format(poravnajLijevo, vozilo.getRegistracija(), vozilo.getOpis(),
        vozilo.getStatus().getClass().getSimpleName(),
        String.format("%.2f", (vozilo.getOdvozeni_km())),
        Integer.toString(vozilo.brojHitnihPaketa()), Integer.toString(vozilo.brojObicnihPaketa()),
        Integer.toString(vozilo.brojIsporucenihPaketa()),
        String.format("%.2f", (vozilo.trenutnaTezina() / vozilo.getKapacitet_tezine()) * 100),
        String.format("%.2f", (vozilo.trenutniProstor() / vozilo.getKapacitet_prostora()) * 100),
        Integer.toString(vozilo.brojVoznji()));
  }

  @Override
  public void posjetiVoznju(Voznja voznja) {
    String poravnajLijevo =
        "| %-20s | %-20s | %-15s | %-13s | %-15s | %-15s | %-17s | %-10s | %-10s |%n";
    System.out.format(poravnajLijevo,
        voznja.getVrijeme_polaska().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
        voznja.getVrijeme_povratka().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
        Duration.between(voznja.getVrijeme_polaska(), voznja.getVrijeme_povratka()).toMinutes(),
        String.format("%.2f", (voznja.getOdvozeni_km())),
        Integer.toString(voznja.brojHitnihPaketa()), Integer.toString(voznja.brojObicnihPaketa()),
        Integer.toString(voznja.brojIsporucenihPaketa()),
        String.format("%.2f", voznja.getPostotakTezine()),
        String.format("%.2f", voznja.getPostotakProstora()));

  }


  @Override
  public void posjetiSegment(SegmentVoznje segment) {
    String poravnajLijevo = "| %-20s | %-20s | %-15s | %-13s | %-15s |%n";
    System.out.format(poravnajLijevo,
        segment.getVrijeme_pocetka().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
        segment.getVrijeme_kraja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")),
        Duration.between(segment.getVrijeme_pocetka(), segment.getVrijeme_kraja()).toMinutes(),
        String.format("%.2f", (segment.getUdaljenost())),
        (segment.getPaket() != null) ? segment.getPaket().getOznaka() : "Povratak u ured"

    );
  }

}
