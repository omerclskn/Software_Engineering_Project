import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testCase {
	Kiosk kiosk;
	 @BeforeEach                                         
	    public void setUp() throws Exception {
		 kiosk = new Kiosk(false);
	    }
	@Test
	void test() {
		//Ders kitabýný sadece öðretim görevlisi alýr
		Kullanici m=new Memur("aaa", "ahmet", "as", "asdfg", 1, 100);
		Yayin k=new DersKitabi("d", "bok", "asd", 1, false);
		assertEquals(false, kiosk.DersKitabýKontrol(m,k));
	}
	@Test
	void test2() {
		//Getirmediði kitap varsa true. Memur olduðu için 30 gün
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.islemListe.add(new Islem("0", "aaa", "d", new Date(121, 2, 10), new Date(121, 3, 10)));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;

		assertEquals(true, kiosk.GetirmedigiVarMi(kiosk.kullaniciListe.get(0)));
	}
	@Test
	void test3() {
		//Getirmediði kitap varsa true. Öðretim üyesi olduðu için 90 gün
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.kullaniciListe.add(new OgretimUyesi("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.islemListe.add(new Islem("0", "aaa", "d", new Date(121, 3, 10), new Date(121, 4, 18)));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;

		assertEquals(false, kiosk.GetirmedigiVarMi(kiosk.kullaniciListe.get(0)));
	}
	@Test
	void test4() {
		//Elindeki kitapsa iþlem ID döndürür
		kiosk.islemListe = new ArrayList<>();
		Kullanici k=new OgretimUyesi("aaa", "ahmet", "as", "asdfg", 1, 100);
		Yayin y=new DersKitabi("d", "hey", "asd", 1, false);
		kiosk.islemListe.add(new Islem("0", "aaa", "d", new Date(121, 3, 10), new Date(121, 4, 18)));

		assertEquals("0", kiosk.ElindekiKitapMi(k,y));
	}
	@Test
	void test5() {
		//Limit kontrolü 3 ve 6
		kiosk.islemListe = new ArrayList<>();
		Kullanici k=new OgretimUyesi("aaa", "ahmet", "as", "asdfg", 5, 100);
		Kullanici k2=new Memur("aaa", "ahmet", "as", "asdfg", 2, 100);
		Kullanici k3=new Ogrenci("aaa", "ahmet", "as", "asdfg", 3, 100);

		assertEquals(true, kiosk.LimitCheck(k));
		assertEquals(true, kiosk.LimitCheck(k2));
		assertEquals(false, kiosk.LimitCheck(k3));

	}
	@Test
	void test6() {
		//Kitabý geri verdik ve müsait hale geldi
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.rezervListe = new ArrayList<>();
		kiosk.yayinListe = new ArrayList<>();
		
		kiosk.yayinListe.add(new Kitap("d", "hey", "asd", 1, false));
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.islemListe.add(new Islem("0", "aaa", "d", new Date(121, 3, 10), new Date(121, 4, 18)));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;

		kiosk.Okuma("d",true);
		kiosk.Okuma("aaa",true);

		assertEquals(true, kiosk.MusaitMi(kiosk.yayinListe.get(0)));
	}
	@Test
	void test7() {
		//Kitabý geri verdik ve baþkasý aldý müsait mi
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.rezervListe = new ArrayList<>();
		kiosk.yayinListe = new ArrayList<>();
		
		kiosk.yayinListe.add(new Kitap("d", "hey", "asd", 1, false));
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.kullaniciListe.add(new Ogrenci("bbb", "memet", "as", "asdfg", 0, 100));

		kiosk.islemListe.add(new Islem("0", "aaa", "d", new Date(121, 3, 10), new Date(121, 4, 18)));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;

		kiosk.Okuma("d",true);
		kiosk.Okuma("aaa",true);
		kiosk.Okuma("d",true);
		kiosk.Okuma("bbb",true);
		assertEquals(false, kiosk.MusaitMi(kiosk.yayinListe.get(0)));
	}
	@Test
	void test8() {
		//Kitabý vaktinde getirmeyen biri
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		Islem i = new Islem("0", "aaa", "d", new Date(121, 0, 10), new Date(121, 2, 18));
		kiosk.islemListe.add(i);
		assertEquals(true, kiosk.SiniriGectiMi(kiosk.kullaniciListe.get(0),i.getBitis()));
	}
	@Test
	void test9() {
		//Kitabý vaktinde getirmeyen biri
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.yayinListe = new ArrayList<>();
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.yayinListe.add(new Kitap("d", "hey", "asd", 1, false));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;
		Islem i = new Islem("0", "aaa", "d", new Date(121, 0, 10), new Date(121, 4, 18));
		kiosk.islemListe.add(i);
		kiosk.Okuma("d", true);
		kiosk.Okuma("aaa", true);
		kiosk.Okuma("d", true);
		kiosk.Okuma("aaa", true);
		assertEquals(false, kiosk.SiniriGectiMi(kiosk.kullaniciListe.get(0),i.getBitis()));
	}
	@Test
	void test10() {
		//Kitabý vaktinde getirmeyen biri
		kiosk.kullaniciListe = new ArrayList<>();
		kiosk.islemListe = new ArrayList<>();
		kiosk.yayinListe = new ArrayList<>();
		kiosk.rezervListe = new ArrayList<>();
		kiosk.kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kiosk.yayinListe.add(new Kitap("d", "hey", "asd", 1, false));
		LocalDateTime now = LocalDateTime.now();  
		kiosk.currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;
		Islem i = new Islem("0", "aaa", "d", new Date(121, 1, 10), new Date(121, 2, 10));
		kiosk.islemListe.add(i);
		kiosk.Okuma("d", true);
		kiosk.Okuma("aaa", true);
		assertEquals(true,kiosk.kullaniciListe.get(0).getBakiye()<50);
	}
}
