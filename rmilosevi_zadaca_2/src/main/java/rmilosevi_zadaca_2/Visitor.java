package rmilosevi_zadaca_2;

public interface Visitor {

  public void posjetiVozilo(Vozilo vozilo);

  public void posjetiVoznju(Voznja voznja);

  public void posjetiSegment(SegmentVoznje segment);

}
