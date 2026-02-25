import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class ProduktuKudeatzailea {
    
    private Connection konexioa;

    /* Datu-basearekin konexioa ezartzen duen eraikitzailea */
    public ProduktuKudeatzailea() {
        this.konexioa = Konexioa.getKonexioa(); 
    }

    /* Produktu berri bat datu-basean txertatzen du eta JSON fitxategiak eguneratzen ditu */
    public void gehituProduktua(Produktua p) {
        String sql = "INSERT INTO produktuak (izena, deskribapena, prezioa, stocka, kategoria, irudiak) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
            pstmt.setString(1, p.getIzena());
            pstmt.setString(2, p.getDeskribapena());
            pstmt.setDouble(3, p.getPrezioa());
            pstmt.setInt(4, p.getStocka());
            pstmt.setString(5, p.getKategoria());
            pstmt.setString(6, String.join(";", p.getIrudiak())); 
            pstmt.executeUpdate();
            
            esportatuJSON();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Produktuen zerrenda bat datu-basean gordetzen du (CSV fitxategi batetik irakurritakoak) */
    public void kargatuCSV(List<Produktua> produktuKatalogoa) {
        for (Produktua p : produktuKatalogoa) {
            String sql = "INSERT INTO produktuak (izena, deskribapena, prezioa, stocka, kategoria, irudiak) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
                pstmt.setString(1, p.getIzena());
                pstmt.setString(2, p.getDeskribapena());
                pstmt.setDouble(3, p.getPrezioa());
                pstmt.setInt(4, p.getStocka());
                pstmt.setString(5, p.getKategoria());
                pstmt.setString(6, String.join(";", p.getIrudiak()));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        esportatuJSON();
        System.out.println("✅ CSV datuak datu-basean kargatu dira eta JSON-ak eguneratu dira.");
    }

    /* Datu-basean dagoen produktu baten datuak eguneratzen ditu bere ID-a erabiliz */
    public void eguneratuProduktua(int id, Produktua pEguneratua) {
        String sql = "UPDATE produktuak SET izena=?, deskribapena=?, prezioa=?, stocka=?, kategoria=?, irudiak=? WHERE id=?";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
            pstmt.setString(1, pEguneratua.getIzena());
            pstmt.setString(2, pEguneratua.getDeskribapena());
            pstmt.setDouble(3, pEguneratua.getPrezioa());
            pstmt.setInt(4, pEguneratua.getStocka());
            pstmt.setString(5, pEguneratua.getKategoria());
            pstmt.setString(6, String.join(";", pEguneratua.getIrudiak()));
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
            
            esportatuJSON();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Produktu bat datu-basetik ezabatzen du bere ID-aren bidez */
    public void ezabatuProduktua(int id) {
        String sql = "DELETE FROM produktuak WHERE id=?";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            esportatuJSON();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Produktuak zerrendatzen ditu, kategoriaren arabera iragaziz edo prezio/stock-aren arabera ordenatuz */
    public void zerrendatuProduktuak(String kategoria, String ordenazioa) {
        String sql = "SELECT * FROM produktuak";
        
        if (kategoria != null && !kategoria.isEmpty()) {
            sql += " WHERE kategoria = ?";
        }
        
        if (ordenazioa != null) {
            if (ordenazioa.equalsIgnoreCase("prezioa")) {
                sql += " ORDER BY prezioa ASC";
            } else if (ordenazioa.equalsIgnoreCase("eskuragarritasuna") || ordenazioa.equalsIgnoreCase("stock")) {
                sql += " ORDER BY stocka DESC";
            }
        }

        try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
            if (kategoria != null && !kategoria.isEmpty()) {
                pstmt.setString(1, kategoria);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("izena") + 
                                   " | " + rs.getDouble("prezioa") + "€ | Stock: " + rs.getInt("stocka") +
                                   " | Kategoria: " + rs.getString("kategoria"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Izenaren edo deskribapenaren arabera produktuak bilatzen ditu */
    public void bilatuProduktua(String testua) {
        String sql = "SELECT * FROM produktuak WHERE izena LIKE ? OR deskribapena LIKE ?";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql)) {
            String bilaketa = "%" + testua + "%";
            pstmt.setString(1, bilaketa);
            pstmt.setString(2, bilaketa);
            ResultSet rs = pstmt.executeQuery();
            boolean aurkituta = false;
            while (rs.next()) {
                aurkituta = true;
                System.out.println("Aurkituta: [" + rs.getInt("id") + "] " + rs.getString("izena") + " - " + rs.getString("deskribapena"));
            }
            if (!aurkituta) {
                System.out.println("Ez da produkturik aurkitu '" + testua + "' testuarekin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Hiru JSON fitxategiak sortzeko metodo nagusia */
    public void esportatuJSON() {
        esportatuProduktuGuztiakJSON();
        esportatuEstatistikakJSON();
        esportatuSalduenakJSON();
    }

    /* Produktu guztien informazioa duen JSON fitxategia sortzen du */
    private void esportatuProduktuGuztiakJSON() {
        String sql = "SELECT * FROM produktuak";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
             FileWriter file = new FileWriter("produktu_guztiak.json")) {
            
            file.write("[\n");
            boolean lehenengoa = true;
            while (rs.next()) {
                if (!lehenengoa) file.write(",\n");
                file.write("  {\n");
                file.write("    \"id\": " + rs.getInt("id") + ",\n");
                file.write("    \"izena\": \"" + rs.getString("izena") + "\",\n");
                file.write("    \"deskribapena\": \"" + rs.getString("deskribapena") + "\",\n");
                file.write("    \"prezioa\": " + rs.getDouble("prezioa") + ",\n");
                file.write("    \"stocka\": " + rs.getInt("stocka") + ",\n");
                file.write("    \"kategoria\": \"" + rs.getString("kategoria") + "\",\n");
                file.write("    \"irudiak\": \"" + rs.getString("irudiak") + "\"\n");
                file.write("  }");
                lehenengoa = false;
            }
            file.write("\n]");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /* Produktuen kopurua eta bataz besteko prezioa JSON fitxategi batean gordetzen ditu */
    private void esportatuEstatistikakJSON() {
        String sql = "SELECT COUNT(*) as total, AVG(prezioa) as media FROM produktuak";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
             FileWriter file = new FileWriter("estatistikak.json")) {
            
            if (rs.next()) {
                file.write("{\n");
                file.write("  \"produktu_kopurua\": " + rs.getInt("total") + ",\n");
                file.write("  \"bataz_besteko_prezioa\": " + rs.getDouble("media") + "\n");
                file.write("}\n");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /* Stock gutxien duten 5 produktuak JSON batean esportatzen ditu */
    private void esportatuSalduenakJSON() {
        String sql = "SELECT * FROM produktuak ORDER BY stocka ASC LIMIT 5";
        try (PreparedStatement pstmt = konexioa.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
             FileWriter file = new FileWriter("salduenak.json")) {
            
            file.write("[\n");
            boolean lehenengoa = true;
            while (rs.next()) {
                if (!lehenengoa) file.write(",\n");
                file.write("  {\n");
                file.write("    \"id\": " + rs.getInt("id") + ",\n");
                file.write("    \"izena\": \"" + rs.getString("izena") + "\",\n");
                file.write("    \"stock_eskuragarri\": " + rs.getInt("stocka") + "\n");
                file.write("  }");
                lehenengoa = false;
            }
            file.write("\n]");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}