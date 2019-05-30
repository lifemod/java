package com.lifemod.myedit;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FileWindow extends JFrame implements ActionListener, Runnable {
	
	private static final long serialVersionUID = -899299121832419106L;
	
	Thread compiler = null;
	Thread run_prom = null;
	boolean bn = true;
	CardLayout mycard;
	File file_saved = null;
	JButton button_input_txt,
			button_compiler_text,
			button_compiler,
			button_run_prom,
			button_see_doswin;
	
	JPanel p = new JPanel();
	JTextArea input_text = new JTextArea();
	JTextArea compiler_text = new JTextArea();
	JTextArea dos_out_text = new JTextArea();
	
	JTextField input_file_name_text = new JTextField();
	JTextField run_file_name_text = new JTextField();
	
	public FileWindow() {
		super("java编辑器");
		mycard = new CardLayout();
		compiler = new Thread();
		run_prom = new Thread();
		button_input_txt = new JButton("代码区");
		button_compiler_text = new JButton("编译区");
		button_see_doswin = new JButton("运行区");
		button_compiler = new JButton("编译");
		button_run_prom = new JButton("运行");
		
		p.setLayout(mycard);
		input_text.setFont(new Font("宋体", Font.BOLD, 24));
		compiler_text.setFont(new Font("宋体", Font.BOLD, 24));
		dos_out_text.setFont(new Font("宋体", Font.BOLD, 24));
		p.add("input", input_text);
		p.add("compiler", compiler_text);
		p.add("dos", dos_out_text);
		add(p, "Center");
		
		compiler_text.setBackground(Color.pink);
		dos_out_text.setBackground(Color.cyan);
		JPanel p1 = new JPanel();
		
		p1.setLayout(new GridLayout());
		p1.add(button_input_txt);
		p1.add(button_compiler_text);
		p1.add(button_see_doswin);
		p1.add(new JLabel("源代码文件名(.java)"));
		p1.add(input_file_name_text);
		p1.add(button_compiler);
		p1.add(new JLabel("主类名"));
		p1.add(run_file_name_text);
		p1.add(button_run_prom);
		add(p1, "North");
		
		button_input_txt.addActionListener(this);
		button_compiler_text.addActionListener(this);
		button_compiler.addActionListener(this);
		button_run_prom.addActionListener(this);
		button_see_doswin.addActionListener(this);
	}
	
	@Override
	public void run() {
		//编译
		if(Thread.currentThread() == compiler) {
			compiler_text.setText(null);
			String temp = input_text.getText().trim();
			byte[] buffer = temp.getBytes();
			int b = buffer.length;
			String file_name = null;
			file_name = input_file_name_text.getText().trim();
			
			try {
				file_saved = new File(file_name);
				FileOutputStream writeFile = null;
				writeFile = new FileOutputStream(file_saved);
				writeFile.write(buffer, 0, b);
				writeFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Runtime rt = Runtime.getRuntime();
				InputStream in = rt.exec("javac " + file_name).getErrorStream();
				BufferedInputStream buFin = new BufferedInputStream(in);
				byte[] shuzu = new byte[100];
				int n = 0;
				boolean flag = true;
				
				while((n = buFin.read(shuzu, 0, shuzu.length)) != -1) {
					String s = null;
					s = new String(shuzu, 0, n);
					compiler_text.append(s);
					if(s != null) {
						flag = false;
					}
				}
				if(flag) {
					compiler_text.append("Compile Succeed!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//运行
		else if(Thread.currentThread() == run_prom) {
			dos_out_text.setText(null);
			try {
				//获取
				Runtime rt = Runtime.getRuntime();
				String path = run_file_name_text.getText().trim();
				Process stream = rt.exec("java " + path);
				
				InputStream in = stream.getInputStream();
				BufferedInputStream bisErr = new BufferedInputStream(stream.getErrorStream());
				BufferedInputStream bisIn = new BufferedInputStream(in);
				byte[] buf = new byte[150];
				byte[] err_buf = new byte[150];
				
				@SuppressWarnings("unused")
				int m = 0;
				@SuppressWarnings("unused")
				int i = 0;
				String s = null;
				String err = null;
				while((m = bisIn.read(buf, 0, 150)) != -1) {
					s = new String(buf, 0, 150);
					dos_out_text.append(s);
				}
				while((i = bisErr.read(err_buf, 0, 150)) != -1) {
					err = new String(err_buf, 0, 150);
					dos_out_text.append(err);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == button_input_txt) {
			mycard.show(p, "input");
		}
		else if(e.getSource() == button_compiler_text) {
			mycard.show(p, "compiler");
		}
		else if(e.getSource() == button_see_doswin) {
			mycard.show(p, "dos");
		}
		else if(e.getSource() == button_compiler) {
			if(!(compiler.isAlive())) {
				compiler = new Thread(this);
			}
			try {
				compiler.start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			mycard.show(p, "compiler");
		}
		else if(e.getSource() == button_run_prom) {
			if(!(run_prom.isAlive())) {
				run_prom = new Thread(this);
			}
			try {
				run_prom.start();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			mycard.show(p, "dos");
		}
	}

}
