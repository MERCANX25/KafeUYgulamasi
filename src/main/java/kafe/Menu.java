package kafe;

import java.util.List;
import java.util.Optional;

/** Kafenin satışa sunduğu ürünler. */
public final class Menu {

    private final List<Urun> urunler;

    public Menu(List<Urun> urunler) {
        if (urunler.isEmpty()) {
            throw new IllegalArgumentException("Menü boş olamaz.");
        }
        this.urunler = List.copyOf(urunler);
    }

    public static Menu varsayilan() {
        return new Menu(List.of(
                new Urun(1, "Çay", "5"),
                new Urun(2, "Kahve", "10"),
                new Urun(3, "Tatlı", "20"),
                new Urun(4, "Türk Kahvesi", "15"),
                new Urun(5, "Su", "3")));
    }

    public List<Urun> urunler() {
        return urunler;
    }

    public Optional<Urun> bul(int no) {
        return urunler.stream().filter(u -> u.no() == no).findFirst();
    }
}
