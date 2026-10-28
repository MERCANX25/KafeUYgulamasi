package kafe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class KafeUygulamasiTest {

    private String cikti;

    private Siparis calistir(String girdi) {
        var tampon = new ByteArrayOutputStream();
        var cikis = new PrintStream(tampon, true, StandardCharsets.UTF_8);
        Siparis siparis = new KafeUygulamasi(Menu.varsayilan(), new Scanner(girdi), cikis).calistir();
        cikti = tampon.toString(StandardCharsets.UTF_8);
        return siparis;
    }

    @Test
    void birdenFazlaUrunSiparisEdilebilir() {
        Siparis siparis = calistir("1\n2\n3\n3\n0\n"); // 2 çay + 3 tatlı = 70 TL
        assertEquals(2, siparis.kalemler().size());
        assertEquals(0, siparis.toplam().compareTo(new BigDecimal("63")));
        assertTrue(cikti.contains("İndirim"));
        assertTrue(cikti.contains("63,00"));
    }

    @Test
    void harfGirilinceCokmezTekrarSorar() {
        Siparis siparis = calistir("abc\n2\niki\n2\n0\n");
        assertTrue(cikti.contains("Lütfen bir sayı girin."));
        assertEquals(new BigDecimal("20"), siparis.toplam());
    }

    @Test
    void gecersizUrunNumarasiReddedilir() {
        Siparis siparis = calistir("9\n0\n");
        assertTrue(cikti.contains("Geçersiz ürün numarası"));
        assertTrue(siparis.bosMu());
    }

    @Test
    void negatifAdetSepeteEklenmez() {
        Siparis siparis = calistir("1\n-4\n0\n");
        assertTrue(cikti.contains("Adet 1 ile"));
        assertTrue(siparis.bosMu());
    }

    @Test
    void girdiBitinceSiparisKapanir() {
        Siparis siparis = calistir("2\n1\n");
        assertEquals(new BigDecimal("10"), siparis.toplam());
        assertTrue(cikti.contains("Fiş"));
    }

    @Test
    void bosSiparisteFisYazilmaz() {
        calistir("0\n");
        assertTrue(cikti.contains("Sipariş verilmedi"));
    }
}
