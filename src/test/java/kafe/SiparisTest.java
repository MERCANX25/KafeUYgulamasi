package kafe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SiparisTest {

    private final Menu menu = Menu.varsayilan();
    private final Urun cay = menu.bul(1).orElseThrow();
    private final Urun kahve = menu.bul(2).orElseThrow();
    private final Urun tatli = menu.bul(3).orElseThrow();

    @Test
    void bosSiparisinToplamiSifirdir() {
        Siparis siparis = new Siparis();
        assertTrue(siparis.bosMu());
        assertEquals(0, siparis.toplam().compareTo(BigDecimal.ZERO));
    }

    @Test
    void araToplamUrunlerinFiyatiCarpiAdettir() {
        Siparis siparis = new Siparis();
        siparis.ekle(cay, 2);
        siparis.ekle(kahve, 1);
        assertEquals(new BigDecimal("20"), siparis.araToplam());
    }

    @Test
    void ayniUrunTekrarEklenirseAdediArtar() {
        Siparis siparis = new Siparis();
        siparis.ekle(cay, 2);
        siparis.ekle(cay, 3);
        assertEquals(1, siparis.kalemler().size());
        assertEquals(5, siparis.kalemler().get(cay));
    }

    @Test
    void elliTlVeAltindaIndirimYoktur() {
        Siparis siparis = new Siparis();
        siparis.ekle(kahve, 5); // tam 50 TL
        assertEquals(BigDecimal.ZERO, siparis.indirim());
        assertEquals(new BigDecimal("50"), siparis.toplam());
    }

    @Test
    void elliTlUzerindeYuzdeOnIndirimUygulanir() {
        Siparis siparis = new Siparis();
        siparis.ekle(tatli, 3); // 60 TL
        assertEquals(new BigDecimal("6.00"), siparis.indirim());
        assertEquals(0, siparis.toplam().compareTo(new BigDecimal("54")));
    }

    @Test
    void indirimKurusaYuvarlanir() {
        Siparis siparis = new Siparis();
        siparis.ekle(new Urun(9, "Kurabiye", "12.35"), 5); // 61,75 TL -> indirim 6,175 -> 6,18
        assertEquals(new BigDecimal("6.18"), siparis.indirim());
        assertEquals(new BigDecimal("55.57"), siparis.toplam());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Siparis.EN_FAZLA_ADET + 1})
    void gecersizAdetReddedilir(int adet) {
        Siparis siparis = new Siparis();
        assertThrows(IllegalArgumentException.class, () -> siparis.ekle(cay, adet));
        assertTrue(siparis.bosMu());
    }

    @Test
    void fiyatSifirOlamaz() {
        assertThrows(IllegalArgumentException.class, () -> new Urun(1, "Bedava", "0"));
    }
}
