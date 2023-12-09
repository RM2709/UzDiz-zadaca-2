package rmilosevi_zadaca_2;

public interface Handler {

  public void obradiZahtjev(String kljucParametra, String vrijednostParametra) throws Exception;

  public void postaviSljedeceg(Handler handler);

}
