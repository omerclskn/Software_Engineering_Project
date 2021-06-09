
public class Yayin {
	private String id,adi,yazar;
	private int sene;
	private boolean durum;
	public Yayin(String id, String adi, String yazar, int sene, boolean durum) {
		super();
		this.id = id;
		this.adi = adi;
		this.yazar = yazar;
		this.sene = sene;
		this.durum = durum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAdi() {
		return adi;
	}
	public void setAdi(String adi) {
		this.adi = adi;
	}
	public String getYazar() {
		return yazar;
	}
	public void setYazar(String yazar) {
		this.yazar = yazar;
	}
	public int getSene() {
		return sene;
	}
	public void setSene(int sene) {
		this.sene = sene;
	}
	public boolean isDurum() {
		return durum;
	}
	public void setDurum(boolean durum) {
		this.durum = durum;
	}
	
	

}
