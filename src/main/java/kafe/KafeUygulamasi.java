package kafe;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/** Konsoldan sipariş alan akış. Giriş ve çıkış dışarıdan verildiği için testte de çalıştırılabilir. */
public final class KafeUygulamasi {

    private static final NumberFormat PARA = NumberFormat.getNumberInstance(Locale.of("tr", "TR"));

    static {
        PARA.setMinimumFractionDigits(2);
        PARA.setMaximumFractionDigits(2);
    }

    private final Menu menu;
    private final Scanner giris;
    private final PrintStream cikis;

    public KafeUygulamasi(Menu menu, Scanner giris, PrintStream cikis) {
        this.menu = menu;
        this.giris = giris;
        this.cikis = cikis;
    }

    /** Siparişi alır, fişi yazar ve oluşan siparişi döndürür. */
    public Siparis calistir() {
        Siparis siparis = new Siparis();
        cikis.println("☕ Kafeye hoş geldiniz!");
        menuyuYaz();

        while (true) {
            cikis.println();
            Integer no = sayiOku("Ürün numarası (bitirmek için 0): ");
            if (no == null || no == 0) {
                break;
            }
            var urun = menu.bul(no);
            if (urun.isEmpty()) {
                cikis.println("Geçersiz ürün numarası, menüdeki numaralardan birini girin.");
                menuyuYaz();
                continue;
            }
            Integer adet = sayiOku("Kaç adet? ");
            if (adet == null) {
                break;
            }
            try {
                siparis.ekle(urun.get(), adet);
                cikis.println(adet + " x " + urun.get().ad() + " sepete eklendi.");
            } catch (IllegalArgumentException e) {
                cikis.println(e.getMessage());
            }
        }

        fisiYaz(siparis);
        return siparis;
    }

    private void menuyuYaz() {
        cikis.println();
        cikis.println("Menü:");
        for (Urun urun : menu.urunler()) {
            cikis.printf("  %d - %-14s %8s TL%n", urun.no(), urun.ad(), para(urun.fiyat()));
        }
    }

    private void tutarSatiri(String etiket, String tutar) {
        cikis.printf("%-21s %8s TL%n", etiket, tutar);
    }

    /** Sayı okuyana kadar sorar. Girdi biterse null döner. */
    private Integer sayiOku(String soru) {
        while (true) {
            cikis.print(soru);
            if (!giris.hasNextLine()) {
                cikis.println();
                return null;
            }
            String satir = giris.nextLine().trim();
            try {
                return Integer.parseInt(satir);
            } catch (NumberFormatException e) {
                cikis.println("Lütfen bir sayı girin.");
            }
        }
    }

    private void fisiYaz(Siparis siparis) {
        cikis.println();
        if (siparis.bosMu()) {
            cikis.println("Sipariş verilmedi. İyi günler!");
            return;
        }
        cikis.println("-------------- Fiş ---------------");
        for (Map.Entry<Urun, Integer> kalem : siparis.kalemler().entrySet()) {
            BigDecimal tutar = kalem.getKey().fiyat().multiply(BigDecimal.valueOf(kalem.getValue()));
            cikis.printf("%3d x %-15s %8s TL%n", kalem.getValue(), kalem.getKey().ad(), para(tutar));
        }
        cikis.println("----------------------------------");
        tutarSatiri("Ara toplam", para(siparis.araToplam()));
        if (siparis.indirim().signum() > 0) {
            tutarSatiri("İndirim (%" + Siparis.INDIRIM_YUZDESI + ")", "-" + para(siparis.indirim()));
        }
        tutarSatiri("Toplam", para(siparis.toplam()));
        if (siparis.indirim().signum() == 0) {
            cikis.println("(" + para(Siparis.INDIRIM_ESIGI) + " TL üzeri siparişlerde %"
                    + Siparis.INDIRIM_YUZDESI + " indirim var.)");
        }
    }

    static String para(BigDecimal tutar) {
        return PARA.format(tutar);
    }
}
