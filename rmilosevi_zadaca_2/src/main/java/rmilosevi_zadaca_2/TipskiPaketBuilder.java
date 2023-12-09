package rmilosevi_zadaca_2;

import java.time.LocalDateTime;
import java.util.List;

public class TipskiPaketBuilder implements PaketBuilder {

  private Paket paket = new Paket();

  @Override
  public PaketBuilder resetiraj() {
    paket = new Paket();
    return this;
  }

  @Override
  public Paket izgradi() {
    return paket;
  }

  @Override
  public PaketBuilder postaviOznaku(String oznaka) {
    paket.setOznaka(oznaka);
    return this;
  }

  @Override
  public PaketBuilder postaviVrijemePrijema(LocalDateTime vrijeme_prijema) {
    paket.setVrijeme_prijema(vrijeme_prijema);
    return this;
  }

  @Override
  public PaketBuilder postaviVrijemePreuzimanja(LocalDateTime vrijeme_preuzimanja) {
    paket.setVrijeme_preuzimanja(vrijeme_preuzimanja);
    return null;
  }

  @Override
  public PaketBuilder postaviPosiljatelja(Osoba posiljatelj) {
    paket.setPosiljatelj(posiljatelj);
    return this;
  }

  @Override
  public PaketBuilder postaviPrimatelja(Osoba primatelj) {
    paket.setPrimatelj(primatelj);
    return this;
  }

  @Override
  public PaketBuilder postaviVrstuPaketa(String vrsta_paketa, List<VrstaPaketa> vrste_paketa) {
    for (VrstaPaketa vrstaPaketa : vrste_paketa) {
      if (vrstaPaketa.oznaka().compareTo(vrsta_paketa) == 0) {
        paket.setVrsta(vrstaPaketa);
      }
    }
    return this;
  }

  @Override
  public PaketBuilder postaviVisinu(String visina) {
    paket.setVisina(paket.getVrsta().visina());
    return this;
  }

  @Override
  public PaketBuilder postaviDuzinu(String duzina) {
    paket.setDuzina(paket.getVrsta().duzina());
    return this;
  }

  @Override
  public PaketBuilder postaviSirinu(String sirina) {
    paket.setSirina(paket.getVrsta().sirina());
    return this;
  }

  @Override
  public PaketBuilder postaviTezinu(String tezina) {
    paket.setTezina(Double.parseDouble(tezina.replace(',', '.')));
    return this;
  }

  @Override
  public PaketBuilder postaviUsluguDostave(String usluga_dostave) {
    paket.setUsluga_dostave(usluga_dostave);
    return this;
  }

  @Override
  public PaketBuilder naplata(String iznos) {
    if (paket.getUsluga_dostave().compareTo("P") == 0) {
      paket.setIznos_pouzeca(Double.parseDouble(iznos.replace(",", ".")));
      paket.setIznos_dostave(0);
    } else {
      double osnovna_cijena;
      if (paket.getUsluga_dostave().compareTo("H") == 0) {
        osnovna_cijena = paket.getVrsta().cijena_hitno();
      } else {
        osnovna_cijena = paket.getVrsta().cijena();
      }
      paket.setIznos_pouzeca(0);
      paket.setIznos_dostave(osnovna_cijena);
    }
    return this;
  }

  @Override
  public PaketBuilder postaviStatusIsporuke(String status_isporuke) {
    paket.setStatus_isporuke(status_isporuke);
    return this;
  }

}
