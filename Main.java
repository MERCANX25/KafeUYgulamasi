import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int cay = 5;
        int kahve = 10;
        int tatli = 20;
        double toplamTutar = 0;

        System.out.println("Ürün numaraları:");
        System.out.println("1 - Çay (5 TL)");
        System.out.println("2 - Kahve (10 TL)");
        System.out.println("3 - Tatlı (20 TL)");
        System.out.print("Lütfen ürün numarasını giriniz: ");
        int urunNo = input.nextInt();

        System.out.print("Kaç adet istersiniz? ");
        int adet = input.nextInt();

        if (urunNo == 1) {
            toplamTutar = cay * adet;
        } else if (urunNo == 2) {
            toplamTutar = kahve * adet;
        } else if (urunNo == 3) {
            toplamTutar = tatli * adet;
        } else {
            System.out.println("Geçersiz ürün numarası!");
            return;
        }

        if (toplamTutar > 50) {
            System.out.println("50 TL üzeri alışverişlerde %10 indirim uygulanıyor.");
            toplamTutar = toplamTutar * 0.9;
        }

        System.out.println("Toplam tutar: " + toplamTutar + " TL");
    }
}
