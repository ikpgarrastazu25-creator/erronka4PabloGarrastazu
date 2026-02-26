import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /* Kudeatzailea instanciatzean, automatikoki datu-basearekin konexioa 
           ezartzen da Konexioa klasearen bidez.
        */
        ProduktuKudeatzailea kudeatzailea = new ProduktuKudeatzailea();
        int aukera;

        /* Erabiltzaileari menua erakusten dion begizta, 
           7. aukera (Irten) sakatu arte errepikatuko da.
        */
        do {
            System.out.println("\n=== ONLINE DENDA - BACK-END KUDEAKETA ===");
            System.out.println("1. Produktua gehitu");
            System.out.println("2. CSV fitxategia igo");
            System.out.println("3. Produktua eguneratu");
            System.out.println("4. Produktua ezabatu");
            System.out.println("5. Produktuak zerrendatu");
            System.out.println("6. Produktuak bilatu");
            System.out.println("7. Irten");
            System.out.print("Aukeratu eragiketa bat (1-7): ");
            
            aukera = scanner.nextInt();
            scanner.nextLine(); 

            switch (aukera) {
                case 1: 
                    /* PRODUKTUA GEHITU: 
                       Erabiltzaileari produktu berri baten datuak eskatzen dizkio banan-banan,
                       Produktua objektu bat sortzen du eta datu-basean gordetzen du.
                    */
                    System.out.print("Izena: ");
                    String izena = scanner.nextLine();
                    System.out.print("Deskribapena: ");
                    String desk = scanner.nextLine();
                    System.out.print("Prezioa: ");
                    double prezioa = scanner.nextDouble();
                    System.out.print("Stocka: ");
                    int stocka = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Kategoria: ");
                    String kategoria = scanner.nextLine();
                    System.out.print("Irudiak (banatu ';' erabiliz): ");
                    List<String> irudiak = Arrays.asList(scanner.nextLine().split(";"));

                    Produktua p = new Produktua(izena, desk, prezioa, stocka, kategoria, irudiak);
                    kudeatzailea.gehituProduktua(p);
                    System.out.println("✅ Produktua ondo gehitu da datu-basean, eta JSON fitxategiak eguneratu dira!");
                    break;

                case 2: 
                    /* CSV KARGATU: 
                       CSV fitxategi baten ibilbidea eskatzen du, bertako lerroak irakurri,
                       komaz (,) banatu eta produktuak masiboki datu-basean kargatzen ditu.
                    */
                    System.out.print("Sartu fitxategiaren ibilbidea (adib: MAHAIGAINEKO_APLIKAZIOA/dataset_online_arropa_denda_salmentak_erronka4.csv): ");
                    String fitxategia = scanner.nextLine();
                    List<Produktua> produktuKatalogoa = new ArrayList<>();
                    
                    try (BufferedReader br = new BufferedReader(new FileReader(fitxategia))) {
                        br.readLine(); /* Lehenengo lerroa (goiburua) saltatzen du */
                        String lerroa;
                        while ((lerroa = br.readLine()) != null) {
                            String[] datuak = lerroa.split(",");
                            produktuKatalogoa.add(new Produktua(datuak[0], datuak[1], Double.parseDouble(datuak[2]), Integer.parseInt(datuak[3]), datuak[4], Arrays.asList(datuak[5].split(";"))));
                        }
                        kudeatzailea.kargatuCSV(produktuKatalogoa);
                    } catch (Exception e) {
                        System.out.println("❌ Errorea CSV fitxategia irakurtzean.");
                        /* e.printStackTrace() gehitu dugu errore zehatza kontsolan ikusteko */
                        e.printStackTrace(); 
                    }
                    break;

                case 3: 
                    /* EGUNERATU: 
                       Datu-basean dagoen produktu baten ID-a eskatzen du, 
                       eta ondoren eremu guztiak berriro eskatzen ditu hura gainidazteko.
                    */
                    System.out.print("Sartu eguneratu nahi den produktuaren ID-a datu-basean: ");
                    int idEguneratu = scanner.nextInt();
                    scanner.nextLine();
                    
                    System.out.println("Sartu datu berriak:");
                    System.out.print("Izen berria: ");
                    String iz = scanner.nextLine();
                    System.out.print("Deskribapen berria: ");
                    String des = scanner.nextLine();
                    System.out.print("Prezio berria: ");
                    double pre = scanner.nextDouble();
                    System.out.print("Stock berria: ");
                    int sto = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Kategoria berria: ");
                    String kat = scanner.nextLine();
                    System.out.print("Irudi berriak (;): ");
                    List<String> iru = Arrays.asList(scanner.nextLine().split(";"));
                    
                    kudeatzailea.eguneratuProduktua(idEguneratu, new Produktua(iz, des, pre, sto, kat, iru));
                    System.out.println("✅ Produktua eguneratu da!");
                    break;

                case 4: 
                    /* EZABATU: 
                       Sartutako ID-a duen produktua datu-basetik ezabatzen du.
                    */
                    System.out.print("Sartu ezabatu nahi den produktuaren ID-a: ");
                    int idEzabatu = scanner.nextInt();
                    kudeatzailea.ezabatuProduktua(idEzabatu);
                    System.out.println("✅ Produktua ezabatu da eta JSON-ak eguneratu dira!");
                    break;

                case 5: 
                    /* ZERRENDATU: 
                       Produktuak datu-basetik ateratzen ditu eta pantailaratzen ditu. 
                       Erabiltzaileari prezioaren edo stockaren arabera ordenatzeko aukera ematen dio.
                    */
                    System.out.print("Nola ordenatu nahi dituzu? (idatzi 'prezioa', 'stock' edo sakatu Enter ez ordenatzeko): ");
                    String ord = scanner.nextLine();
                    if (ord.isBlank()) ord = null;
                    System.out.println("\n--- PRODUKTUEN ZERRENDA ---");
                    kudeatzailea.zerrendatuProduktuak(null, ord);
                    break;

                case 6: 
                    /* BILATU: 
                       Sartutako testua produktuaren izenean edo deskribapenean
                       ba ote dagoen bilatzen du datu-basean.
                    */
                    System.out.print("Sartu bilatzeko hitza (izena edo deskribapena): ");
                    String testua = scanner.nextLine();
                    System.out.println("\n--- BILAKETAREN EMAITZA ---");
                    kudeatzailea.bilatuProduktua(testua);
                    break;

                case 7: 
                    /* IRTEN: Programa modu egokian amaitzen du */
                    System.out.println("Programa amaitzen... Agur!");
                    break;

                default:
                    System.out.println("❌ Aukera okerra. Mesedez, aukeratu 1 eta 7 arteko zenbaki bat.");
            }

        } while (aukera != 7);

        scanner.close();
    }
}