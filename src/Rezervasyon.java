
public class Rezervasyon {
	private String kullaniciId,yayinId;

	public Rezervasyon(String kullaniciId, String yayinId) {
		this.kullaniciId = kullaniciId;
		this.yayinId = yayinId;
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
	
}
