
public class Kullanici {
	private String id,isim,soyisim,email;
	private int limit,bakiye;
	public Kullanici(String id, String isim, String soyisim, String email, int limit, int bakiye) {
		this.id = id;
		this.isim = isim;
		this.soyisim = soyisim;
		this.email = email;
		this.limit = limit;
		this.bakiye = bakiye;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsim() {
		return isim;
	}
	public void setIsim(String isim) {
		this.isim = isim;
	}
	public String getSoyisim() {
		return soyisim;
	}
	public void setSoyisim(String soyisim) {
		this.soyisim = soyisim;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getBakiye() {
		return bakiye;
	}
	public void setBakiye(int bakiye) {
		this.bakiye = bakiye;
	}
	
}
