# Kafe Uygulaması

[![Testler](https://github.com/MERCANX25/KafeUYgulamasi/actions/workflows/test.yml/badge.svg)](https://github.com/MERCANX25/KafeUYgulamasi/actions/workflows/test.yml)

Konsoldan sipariş alan küçük bir Java kafe uygulaması. Menüden istediğin kadar ürün seçersin, sonunda fiş yazdırılır. 50 TL üzeri siparişlerde %10 indirim uygulanır.

```
☕ Kafeye hoş geldiniz!

Menü:
  1 - Çay                5,00 TL
  2 - Kahve             10,00 TL
  3 - Tatlı             20,00 TL
  4 - Türk Kahvesi      15,00 TL
  5 - Su                 3,00 TL

Ürün numarası (bitirmek için 0): 1
Kaç adet? 2
2 x Çay sepete eklendi.

Ürün numarası (bitirmek için 0): 3
Kaç adet? 3
3 x Tatlı sepete eklendi.

Ürün numarası (bitirmek için 0): 0

-------------- Fiş ---------------
  2 x Çay                10,00 TL
  3 x Tatlı              60,00 TL
----------------------------------
Ara toplam               70,00 TL
İndirim (%10)            -7,00 TL
Toplam                   63,00 TL
```

## Çalıştırma

Gereksinim: Java 21 ve Maven.

```bash
mvn package
java -jar target/kafe-uygulamasi-1.0.0.jar
```

Testler:

```bash
mvn test
```

## Yapı

```
src/main/java/kafe/
  Urun.java             Ürün (no, ad, fiyat)
  Menu.java             Satıştaki ürünler ve numaraya göre arama
  Siparis.java          Sepet: adet toplama, ara toplam, indirim, toplam
  KafeUygulamasi.java   Konsol akışı ve fiş
  Main.java             Giriş noktası
src/test/java/kafe/     JUnit 5 testleri
```

Hesaplama `Siparis` sınıfında, konsol akışı `KafeUygulamasi` sınıfında. Akış girişi ve çıkışı dışarıdan aldığı için testlerde hazır girdiyle baştan sona çalıştırılabiliyor.

## İlk sürüme göre neler değişti

İlk sürüm tek dosyalık bir programdı: bir ürün seçilip fiyat hesaplanıyordu. Yeniden düzenlerken:

- **Birden fazla ürün:** Sepete istenen kadar ürün eklenebiliyor, aynı ürün tekrar seçilince adedi artıyor.
- **Hatalı girişler:** Sayı yerine harf girilince program çöküyordu, artık tekrar soruyor. Negatif ya da sıfır adet kabul edilmiyor.
- **Para hesabı:** `double` yerine `BigDecimal` kullanılıyor, indirim kuruşa yuvarlanıyor.
- **Fiş:** Ürün ürün tutar, ara toplam, indirim ve toplam yazdırılıyor.
- **Maven projesi ve testler:** 16 JUnit testi, GitHub Actions ile her gönderimde çalışıyor.
