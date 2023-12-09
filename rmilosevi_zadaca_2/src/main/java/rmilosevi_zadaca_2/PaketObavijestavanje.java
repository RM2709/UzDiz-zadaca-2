package rmilosevi_zadaca_2;

import java.util.ArrayList;
import java.util.List;

public class PaketObavijestavanje {

  private List<SlusacObavijesti> slusaci = new ArrayList<>();

  public void prijaviSlusanje(SlusacObavijesti slusac) {
    if (!slusaci.contains(slusac)) {
      slusaci.add(slusac);
    }
  }

  public void odjaviSlusanje(SlusacObavijesti slusac) {
    if (slusaci.contains(slusac)) {
      slusaci.remove(slusac);
    }
  }

  public void obavijestiSlusace(Paket paket, String obavijest) {
    for (SlusacObavijesti slusac : slusaci) {
      slusac.primiObavijest(paket, obavijest);
    }
  }

}
