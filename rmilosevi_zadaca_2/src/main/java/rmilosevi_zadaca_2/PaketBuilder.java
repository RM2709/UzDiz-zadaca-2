package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.List;

public interface PaketBuilder {

  public PaketBuilder resetiraj();

  public PaketBuilder postaviOznaku(String oznaka);

  public PaketBuilder postaviVrijemePrijema(LocalDateTime vrijeme_prijema);

  public PaketBuilder postaviVrijemePreuzimanja(LocalDateTime vrijeme_preuzimanja);

  public PaketBuilder postaviPosiljatelja(Osoba posiljatelj);

  public PaketBuilder postaviPrimatelja(Osoba primatelj);

  public PaketBuilder postaviVrstuPaketa(String vrsta_paketa, List<VrstaPaketa> vrste_paketa);

  public PaketBuilder postaviVisinu(String visina);

  public PaketBuilder postaviDuzinu(String duzina);

  public PaketBuilder postaviSirinu(String sirina);

  public PaketBuilder postaviTezinu(String tezina);

  public PaketBuilder postaviUsluguDostave(String usluga_dostave);

  public PaketBuilder postaviStatusIsporuke(String status_isporuke);

  public PaketBuilder naplata(String iznos);

  public Paket izgradi();

}
