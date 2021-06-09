import java.sql.Date;

public class Islem {
	private String islemId,kullaniciId,yayinId;
	private Date baslangic,bitis;
	public Islem(String islemId, String kullaniciId, String yayinId, Date baslangic, Date bitis) {
		super();
		this.islemId = islemId;
		this.kullaniciId = kullaniciId;
		this.yayinId = yayinId;
		this.baslangic = baslangic;
		this.bitis = bitis;
	}
	public String getIslemId() {
		return islemId;
	}
	public void setIslemId(String islemId) {
		this.islemId = islemId;
	}
	public String getKullaniciId() {
		return kullaniciId;
	}
	public void setKullaniciId(String kullaniciId) {
		this.kullaniciId = kullaniciId;
	}
	public String getYayinId() {
		return yayinId;
	}
	public void setYayinId(String yayinId) {
		this.yayinId = yayinId;
	}
	public Date getBaslangic() {
		return baslangic;
	}
	public void setBaslangic(Date baslangic) {
		this.baslangic = baslangic;
	}
	public Date getBitis() {
		return bitis;
	}
	public void setBitis(Date bitis) {
		this.bitis = bitis;
	}
	
}
