package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	public static MenjacnicaGUI mg;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIKontroler.mg = new MenjacnicaGUI();
					GUIKontroler.mg.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(mg.contentPane,
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	public static void prikaziAboutProzor(){
		JOptionPane.showMessageDialog(mg.contentPane,
				"Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(mg.contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				mg.sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg.contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(mg.contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				mg.sistem.ucitajIzFajla(file.getAbsolutePath());
				GUIKontroler.prikaziSveValute();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg.contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel)(mg.table.getModel());
		model.staviSveValuteUModel(mg.sistem.vratiKursnuListu());

	}
	
	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(mg.contentPane);
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI() {
		
		if (mg.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(mg.table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(
					model.vratiValutu(mg.table.getSelectedRow()));
			prozor.setLocationRelativeTo(mg.contentPane);
			prozor.setVisible(true);
		}
	}
	
	public static void prikaziIzvrsiZamenuGUI() {
		if (mg.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(mg.table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(
					model.vratiValutu(mg.table.getSelectedRow()));
			prozor.setLocationRelativeTo(mg.contentPane);
			prozor.setVisible(true);
		}
	}
	public static void unesiKurs(String naziv,String skraceniNaziv,int sifra,double prodajni,double kupovni,double srednji) {
		try {
			Valuta valuta = new Valuta();
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajni);
			valuta.setKupovni(kupovni);
			valuta.setSrednji(srednji);
			
			// Dodavanje valute u kursnu listu
			GUIKontroler.mg.sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			GUIKontroler.prikaziSveValute();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg.contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try{
			mg.sistem.obrisiValutu(valuta);
			
			GUIKontroler.prikaziSveValute();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg.contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static double izvrsiZamenu(Valuta valuta,boolean prodaja,double iznos){
		try{
			double konacniIznos = 
					mg.sistem.izvrsiTransakciju(valuta, prodaja, iznos);
			return konacniIznos;
		} catch (Exception e1) {
		JOptionPane.showMessageDialog(mg.contentPane, e1.getMessage(),
				"Greska", JOptionPane.ERROR_MESSAGE);
	}
		return 1;
	}
}
