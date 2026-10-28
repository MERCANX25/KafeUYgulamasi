package kafe;

import java.math.BigDecimal;
import java.util.Objects;

/** Menüdeki bir ürün. Fiyatlar kuruş hatası olmasın diye BigDecimal tutulur. */
public record Urun(int no, String ad, BigDecimal fiyat) {

    public Urun {
        Objects.requireNonNull(ad, "ad");
        Objects.requireNonNull(fiyat, "fiyat");
        if (ad.isBlank()) {
            throw new IllegalArgumentException("Ürün adı boş olamaz.");
        }
        if (fiyat.signum() <= 0) {
            throw new IllegalArgumentException("Fiyat sıfırdan büyük olmalı.");
        }
    }

    public Urun(int no, String ad, String fiyat) {
        this(no, ad, new BigDecimal(fiyat));
    }
}
