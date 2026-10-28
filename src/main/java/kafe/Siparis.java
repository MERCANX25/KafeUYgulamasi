package kafe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bir müşterinin sepeti. Aynı ürün tekrar eklenirse adedi artar.
 * Ara toplam {@link #INDIRIM_ESIGI} TL'yi geçerse %{@link #INDIRIM_YUZDESI} indirim uygulanır.
 */
public final class Siparis {

    public static final BigDecimal INDIRIM_ESIGI = new BigDecimal("50");
    public static final int INDIRIM_YUZDESI = 10;
    public static final int EN_FAZLA_ADET = 100;

    private final Map<Urun, Integer> kalemler = new LinkedHashMap<>();

    public void ekle(Urun urun, int adet) {
        if (adet < 1 || adet > EN_FAZLA_ADET) {
            throw new IllegalArgumentException("Adet 1 ile " + EN_FAZLA_ADET + " arasında olmalı.");
        }
        kalemler.merge(urun, adet, Integer::sum);
    }

    public Map<Urun, Integer> kalemler() {
        return Collections.unmodifiableMap(kalemler);
    }

    public boolean bosMu() {
        return kalemler.isEmpty();
    }

    public BigDecimal araToplam() {
        return kalemler.entrySet().stream()
                .map(k -> k.getKey().fiyat().multiply(BigDecimal.valueOf(k.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal indirim() {
        BigDecimal araToplam = araToplam();
        if (araToplam.compareTo(INDIRIM_ESIGI) <= 0) {
            return BigDecimal.ZERO;
        }
        return araToplam.multiply(BigDecimal.valueOf(INDIRIM_YUZDESI))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal toplam() {
        return araToplam().subtract(indirim());
    }
}
