package it.unisa.bugforecast.test;

import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * aleso
 */
public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String workspace = "C:\\Users\\aleso\\Desktop\\runtime-EclipseApplication\\";
		File fInput = new File(workspace);
		String a[] = fInput.list(); 
		System.out.println("stampo la lista dei files contenuti nella directory:");
		int j = 0;
		for (int i = 0; i < a.length; i++) {
			if (Character.isUpperCase(a[i].charAt(0))) {
				j = j + 1;
				System.out.println(j + ". " + a[i]);
			}
		}
		JFrame win;
		win = new JFrame("PrimaÃfinestra");
		Container c = win.getContentPane();
		c.add(new JLabel("BuonaÃLezione"));
		win.setSize(200,200);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
	}

}
