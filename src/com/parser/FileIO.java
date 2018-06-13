package com.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFileChooser;

public class FileIO {
	List<String> inputData = new LinkedList<String>();
	List<String> rule = new LinkedList<String>();

	public void load() {

		JFileChooser myFileChooser = new JFileChooser();
		int intRet = myFileChooser.showOpenDialog(null);
		/*
		 * ��ȯ�� : JFileChooser.CANCEL_OPTION : ��ȭ���ڿ��� ���(Cancel)��ư�� �������
		 * JFileChooser.APPROVE_OPTION : ��ȭ���ڿ��� ����(yes,ok��) ��ư�� �������
		 * JFileChooser.ERROR_OPTION : � ������ �߻��� ��ȭ���ڰ� ��ҵȰ��
		 */

		// ���پ� �б����� String ��ü
		String strLine;

		if (intRet == JFileChooser.APPROVE_OPTION) {
			try {
				// FileChooser�� ���õ� ������ ���ϰ�ü�� ����
				java.io.File myFile = myFileChooser.getSelectedFile();

				// ���õ� ������ �����θ� �����Ͽ� BufferedReader ��ü�� �ۼ�
				BufferedReader myReader = new BufferedReader(new FileReader(myFile.getAbsolutePath()));

				// ���پ� �о���̸鼭 �����Ϳ� ����
				while ((strLine = myReader.readLine()) != null) {
					if (!strLine.isEmpty()) {
						this.inputData.add(strLine);
					}
				}

				// rule���� �����ϴ� list ����
				Iterator<String> iter = inputData.iterator();
				while (iter.hasNext()) {
					String data = iter.next();
					if ('R' != data.charAt(0)) {
						rule.add(data);
					}
				}

				// R0 �߰�
				rule.add(0, "S>" + rule.get(0).charAt(0));

				// Buffered Reader ��ü Ŭ����
				myReader.close();

			} catch (IOException ie) {
				System.out.println(ie + "=> ����¿���");
			}
		}
	}

	public void save(List<String> output) {
		JFileChooser myFileChooser = new JFileChooser();
		int intRet = myFileChooser.showSaveDialog(null);

		if (intRet == JFileChooser.APPROVE_OPTION) {
			try {
				// FileChooser�� ���õ� ������ ���ϰ�ü�� ����
				java.io.File myFile = myFileChooser.getSelectedFile();

				// ���õ� ������ �����θ� �����Ͽ� PrintWriter ��ü�� �ۼ�
				PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter(myFile.getAbsolutePath())));

				// linkedList�� �ϳ��� String���� �ٲ�
				ListIterator<String> it = (ListIterator<String>) this.inputData.listIterator();
				StringBuilder sb = new StringBuilder();
				while (it.hasNext()) {
					sb.append(it.next());
					sb.append("\r\n");
				}
				
				// ���Ͽ� ����
				myWriter.write(sb.toString());
				myWriter.close();

			} catch (IOException ie) {
				System.out.println(ie + "=> ����¿���");
			}
		}
	}
	
	public void save(LinkedHashSet<LinkedList<String>> C0) {
		JFileChooser myFileChooser = new JFileChooser();
		int intRet = myFileChooser.showSaveDialog(null);

		if (intRet == JFileChooser.APPROVE_OPTION) {
			try {
				// FileChooser�� ���õ� ������ ���ϰ�ü�� ����
				java.io.File myFile = myFileChooser.getSelectedFile();

				// ���õ� ������ �����θ� �����Ͽ� PrintWriter ��ü�� �ۼ�
				PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter(myFile.getAbsolutePath())));

				// linkedList�� �ϳ��� String���� �ٲ�
				Iterator<LinkedList<String>> it = C0.iterator();
				StringBuilder sb = new StringBuilder();
				int num = 0;
				while (it.hasNext()) {
					sb.append("I"+(num++)+"\r\n");
					
					sb.append(it.next());
					sb.append("\r\n");
				}

				// ���Ͽ� ����
				myWriter.write(sb.toString());
				myWriter.close();

			} catch (IOException ie) {
				System.out.println(ie + "=> ����¿���");
			}
		}
	}

	public List<String> getRule() {
		return this.rule;
	}
}
