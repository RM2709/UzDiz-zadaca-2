package rmilosevi_zadaca_2;

public class ObradaKomandeDnevnik implements ServisObradeKomande {

  ObradaKomande servisObradeKomande = new ObradaKomande();

  @Override
  public void obradiKomandu(String komanda) {
    if (Tvrtka.dohvatiInstancu().pisiDnevnik() == true) {
      Tvrtka.dohvatiInstancu().zapisiDnevnik(komanda);
    }
    servisObradeKomande.obradiKomandu(komanda);
  }

}
