import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.TextArea;
import java.awt.List;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Kiosk {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	
	public static ArrayList<Kullanici> kullaniciListe;


	public static ArrayList<Yayin> yayinListe;
	public static ArrayList<Islem> islemListe;
	public static ArrayList<Rezervasyon> rezervListe;

	public static Date currentDate;
	public static int IslemId=1; // 1 tane default var
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Kiosk window = new Kiosk(true);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		kullaniciListe = new ArrayList<>();
		yayinListe = new ArrayList<>();
		islemListe = new ArrayList<>();
		rezervListe = new ArrayList<>();

		Fill();

	}

	@SuppressWarnings("deprecation")
	static void Fill() {
		kullaniciListe.add(new Memur("aaa", "ahmet", "as", "asdfg", 1, 100));
		kullaniciListe.add(new Ogrenci("bbb", "memet", "as", "asdfg", 0, 100));
		kullaniciListe.add(new OgretimUyesi("ccc", "osman", "as", "asdfg", 0, 100));
		yayinListe.add(new Kitap("d", "hey", "asd", 1, false));
		yayinListe.add(new Dergi("f", "gadada", "asd", 1, true));
		yayinListe.add(new DersKitabi("g", "hihihi", "asd", 1, true));
		islemListe.add(new Islem("0", "aaa", "d", new Date(121, 2, 18), new Date(121, 3, 18)));
		rezervListe.add(new Rezervasyon("ccc","d"));

		LocalDateTime now = LocalDateTime.now();  
		currentDate=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;
		//currentDate = new Date(121, 4, 18);
	}

	private boolean inputType = false;
	private Kullanici currentKullanici;
	private Yayin currentYayin;

	@SuppressWarnings("deprecation")
	void Okuma(String id,boolean test) {
		if (inputType == false) {
			Yayin temp = searchYayin(id);
			if (temp != null) {
				/// GEÇERLÝ YAYIN OKUNDU
				currentYayin = temp;
				Output("Yayin " + currentYayin.getAdi(),test);
				inputType = true;
				Baslik("Kullanici ID:",test);
			} else {
				Output("Gecersiz Yayin!",test);
			}

		} else {
			Baslik("Kullanici ID:",test);
			Kullanici temp = searchKullanici(id);
			if (temp != null) {
				currentKullanici = temp;
				if (ElindekiKitapMi(currentKullanici, currentYayin) != null) {
					String islemidVal =ElindekiKitapMi(currentKullanici, currentYayin);
					int islemid= GetIndexOfIslemId(islemidVal);
					// ÝADE ÝÞLEMÝ
					if (SiniriGectiMi(currentKullanici, islemListe.get(islemid).getBitis())) {
						long ceza = getDifferenceDays(currentDate, islemListe.get(islemid).getBitis());
						int guncelBakiye = (int) (currentKullanici.getBakiye() - ceza);
						System.out.println("CEZA:"+ceza);
						//currentKullanici.setBakiye(guncelBakiye);
						searchKullanici(currentKullanici.getId()).setBakiye(guncelBakiye);
						
						Output("Ýade Tamamlandi Ceza Tutarý ("+ceza+") Bakiyeden Düþtü",test);
					} else {
						Output("Ýade Zamaninda Tamamlandi",test);
					}
					IadeIslemi(islemidVal); //Varsa rezervasyonlara mail gönderir ve iþlemi kaldýrýr

				} else if (MusaitMi(currentYayin)) {
					if(DersKitabýKontrol(currentKullanici, currentYayin)) {
						if (LimitCheck(currentKullanici)) {
							// Ýyi gidiyo
							if (!GetirmedigiVarMi(currentKullanici)) {
								// ALDI KÝTABI
								Date today,endDate;
								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
								LocalDateTime now = LocalDateTime.now();  
								today=new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()) ;
								endDate=GetEndDate(today);
								System.out.println(today);
								
								islemListe.add(new Islem(String.valueOf(IslemId),currentKullanici.getId(),currentYayin.getId(),today,endDate));
								IslemId++;
								Output("Kitabý alabilirsin",test);
								inputType = false;
								Baslik("Yayin ID:",test);
								currentYayin.setDurum(false);
							} else {
								Output("Aldýðýný getir",test);
							}
						} else {
							Output("Limit aþýldý",test);
						}
					}
					else {
						Output("Ders Kitabý Alma Yetkiniz Yok",test);
					}
				}else {
					// REZERVASYON
					Output("Yayin müsait deðil, rezervasyona eklendi",test);
					rezervListe.add(new Rezervasyon(currentKullanici.getId(), currentYayin.getId()));

				}

			} else {
				Output("Hata!",test);
			}
			inputType = false;
			Baslik("Yayin ID:",test);
		}
		if(test==false) {
			textField.setText("");
			RefreshTables();
		}
	}
	private void Output(String s,boolean test) {
		if(test==false) {
			output.setText(s);
		}
	}
	private void Baslik(String s,boolean test) {
		if(test==false) {
			baslik.setText(s);
		}
	}
	private int GetIndexOfIslemId(String islemId) {
		for (Islem islem : islemListe) {
			if(islem.getIslemId().equals(islemId)) {
				return islemListe.indexOf(islem);
			}
		}
		return -1;
	}
	private Date GetEndDate(Date today) {
		int month=today.getMonth(),year=today.getYear();
		if (currentKullanici instanceof OgretimUyesi) {
			if(month==10) {
				year+=1;
				month=-1;
			}else if(month==11) {
				year+=1;
				month=0;
			}else {
				month++;
			}
		}else {
			if(month==11) {
				year+=1;
				month=-1;
			}
		}
		//System.out.println(today.getDate());
		@SuppressWarnings("deprecation")
		Date endDate=new Date(year,month+1,today.getDate()) ;
		return endDate;
	}
	void IadeIslemi(String islemId) {
		ArrayList<Rezervasyon> list = new ArrayList<>();
		for (Rezervasyon rezervasyon : rezervListe) {
			if(rezervasyon.getYayinId().equals(currentYayin.getId())) {
				//Rezervasyon iþleniyor ( silinecek)
				list.add(rezervasyon);
			}
		}
		for (Rezervasyon rezervasyon : list) {
			rezervListe.remove(rezervasyon);
		}
		for (Islem e : islemListe) {
			if(e.getIslemId().equals(islemId)) {
				islemListe.remove(e);
				break;
			}
		}
		currentYayin.setDurum(true);

	}
	boolean DersKitabýKontrol(Kullanici k, Yayin y) { //öðretim üyesi alabiliyor sadece
		if(y instanceof DersKitabi) {
			if(k instanceof Memur || k instanceof Ogrenci) 
				return false;
		}
		return true;
	}
	String ElindekiKitapMi(Kullanici k, Yayin y) {
		for (Islem islem : islemListe) {
			if (islem.getKullaniciId().equals(k.getId()) && islem.getYayinId().equals(y.getId())) {
				return islem.getIslemId();
			}
		}
		return null;
	}

	boolean MusaitMi(Yayin yayin) {
		return yayin.isDurum();
	}

	boolean GetirmedigiVarMi(Kullanici kullanici) { // Sýnýr içinde getirmediði varsa
		for (Islem islem : islemListe) {
			if (islem.getKullaniciId().equals(kullanici.getId())
					&& SiniriGectiMi(kullanici, islem.getBitis())) {
				return true;
			}
		}
		return false;
	}

	boolean SiniriGectiMi(Kullanici k, Date bitis) {
		if (k instanceof Memur || k instanceof Ogrenci) {
			if (getDifferenceDays(currentDate, bitis) > 30) {
				// output.setText("Önce aldýðýný getir");
				return true;
			}
		} else { // Öðretim üyesi olmalý
			if (getDifferenceDays(currentDate, bitis) > 60) {
				// output.setText("Önce aldýðýný getir");
				return true;
			}
		}
		return false;
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d1.getTime() - d2.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	boolean LimitCheck(Kullanici kullanici) {
		if (kullanici instanceof Memur || kullanici instanceof Ogrenci) {
			System.out.println("MEMUURR OGRENCÝ+");
			if (kullanici.getLimit() < 3) {
				return true;
			} else {
				return false;
			}
		} else if (kullanici instanceof OgretimUyesi) {
			if (kullanici.getLimit() < 6) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Kullanici searchKullanici(String id) {
		for (Kullanici kullanici : kullaniciListe) {
			if (kullanici.getId().equals(id)) {
				return kullanici;
			}
		}
		return null;
	}

	private Yayin searchYayin(String id) {
		for (Yayin yayin : yayinListe) {
			if (yayin.getId().equals(id)) {
				return yayin;
			}
		}
		return null;
	}

	/**
	 * Create the application.
	 */
	public Kiosk(boolean b) {
		if(b) {
			initialize();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static JLabel baslik;
	JTextArea output;
	JTable table;
	JScrollPane scrollPane_1;
	JTable table2;
	private JScrollPane scrollPane_2;
	private JTable table3;
	private JScrollPane scrollPane_3;
	private JTable table4;
	private JScrollPane scrollPane;
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 741, 630);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(234, 36, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		baslik = new JLabel("Yayin ID:");
		baslik.setBounds(234, 11, 122, 14);
		frame.getContentPane().add(baslik);

		JButton btnNewButton = new JButton("Oku");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Okuma(textField.getText().toString(),false);
			}
		});
		btnNewButton.setBounds(330, 35, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		output = new JTextArea();
		output.setLineWrap(true);
		output.setEditable(false);
		output.setRows(5);
		output.setBounds(119, 77, 375, 72);
		frame.getContentPane().add(output);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 195, 265, 182);
		frame.getContentPane().add(scrollPane);
		//table = new JTable();
		//table.setEnabled(false);
		//table = new JTable(data,column);
		
		//table = new JTable()
		scrollPane.setViewportView(table);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(325, 195, 185, 182);
		frame.getContentPane().add(scrollPane_1);
		
		scrollPane_1.setViewportView(table2);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(26, 388, 666, 182);
		frame.getContentPane().add(scrollPane_2);
		
		scrollPane_2.setViewportView(table3);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(534, 195, 158, 182);
		frame.getContentPane().add(scrollPane_3);

		scrollPane_3.setViewportView(table4);
		RefreshTables();


	}
	private void RefreshTables() {
		System.out.print("REFRESHED");
		String data[][]=new String[kullaniciListe.size()][4];
		 String column[]={"ID","ADI","BAKÝYE","SIFAT"};    

		int i=0;
		for (Kullanici eleman : kullaniciListe) {
			data[i][0]=eleman.getId();
			data[i][1]=eleman.getIsim();
			data[i][2]=String.valueOf(eleman.getBakiye());
			data[i][3]=eleman.getClass().getName();

			i++;
		}
		table = new JTable(data,column);
		table.setEnabled(false);
		String data2[][]=new String[yayinListe.size()][3];
		String column2[]={"ID","YAYIN","DURUM"};    

		i=0;
		for (Yayin eleman : yayinListe) {
			data2[i][0]=eleman.getId();
			data2[i][1]=eleman.getAdi();
			data2[i][2]=String.valueOf(eleman.isDurum());

			i++;
		}
		table2 = new JTable(data2,column2);
		table2.setEnabled(false);
		String data3[][]=new String[islemListe.size()][5];
		String column3[]={"ID","KULLANICI ID","YAYIN ID","BASLANGIÇ","BÝTÝÞ"};    

		i=0;
		for (Islem eleman : islemListe) {
			data3[i][0]=eleman.getIslemId();
			data3[i][1]=eleman.getKullaniciId();
			data3[i][2]=eleman.getYayinId();
			data3[i][3]=String.valueOf(eleman.getBaslangic());
			data3[i][4]=String.valueOf(eleman.getBitis());

			i++;
		}
		table3 = new JTable(data3,column3);
		table3.setEnabled(false);
		String data4[][]=new String[rezervListe.size()][2];
		String column4[]={"KULLANICI ID","YAYIN ID"};    

		i=0;
		for (Rezervasyon eleman : rezervListe) {
			data4[i][0]=eleman.getKullaniciId();
			data4[i][1]=eleman.getYayinId();
			i++;
		}
		table4 = new JTable(data4,column4);
		table4.setEnabled(false);
		scrollPane.setViewportView(table);
		scrollPane_1.setViewportView(table2);
		scrollPane_2.setViewportView(table3);
		scrollPane_3.setViewportView(table4);

	}
}
