import java.util.List;

public class Produktua {
    
    private String izena;
    private String deskribapena;
    private double prezioa;
    private int stocka;
    private String kategoria;
    private List<String> irudiak;

    public Produktua() {
    }

    public Produktua(String izena, String deskribapena, double prezioa, int stocka, String kategoria, List<String> irudiak) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.prezioa = prezioa;
        this.stocka = stocka;
        this.kategoria = kategoria;
        this.irudiak = irudiak;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public double getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(double prezioa) {
        this.prezioa = prezioa;
    }

    public int getStocka() {
        return stocka;
    }

    public void setStocka(int stocka) {
        this.stocka = stocka;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public List<String> getIrudiak() {
        return irudiak;
    }

    public void setIrudiak(List<String> irudiak) {
        this.irudiak = irudiak;
    }

    @Override
    public String toString() {
        return "Produktua{" +
                "izena='" + izena + '\'' +
                ", deskribapena='" + deskribapena + '\'' +
                ", prezioa=" + prezioa +
                ", stocka=" + stocka +
                ", kategoria='" + kategoria + '\'' +
                ", irudiak=" + irudiak +
                '}';
    }
}