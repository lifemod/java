package com.lifemod.myedit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author lifemod
 * 
 */
public class Main {

	public static void main(String[] args) {
		
		FileWindow fw = new FileWindow();
		fw.pack();
		fw.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		fw.setBounds(560, 240, 800, 600);
		fw.setVisible(true);
	}

}
