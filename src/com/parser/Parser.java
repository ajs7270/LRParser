package com.parser;

import java.util.LinkedList;
import java.util.List;

public class Parser {

	public static void main(String[] args) {

		LinkedList<LinkedList<String>> C0 = new LinkedList<LinkedList<String>>();

		FileIO file = new FileIO();
		file.load();

		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
	}

	public List<String> CLOSURE(List<String> rule) {
		List<String> result = new LinkedList<String>();

		return rule;
	}

	public Boolean isTerminal(String rule) {

		// '.'�� ��ġ Ȯ��
		int head = rule.indexOf('.');

		// '.'�� �� �������� ��ġ���� ���� ���
		if (head + 1 < rule.length()) {
			// '.'������ �빮���̸� non-terminal �̱� ������ false return
			if (Character.isUpperCase(rule.charAt(head + 1))) {
				return false;
			} else {
				return true;
			}		
		} else {
			return true;
		}
	}
	
	public 

}
