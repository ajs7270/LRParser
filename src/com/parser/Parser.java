package com.parser;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Parser {

	public static void main(String[] args) {

		LinkedList<LinkedList<String>> C0 = new LinkedList<LinkedList<String>>();
		
		FileIO file = new FileIO();
		file.load();

		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
		
	}

	public List<String> CLOSURE(List<String> rule) {
		Iterator<String> iter = rule.iterator();
		List<String> result = new LinkedList<String>();
		Queue<String> queue = new LinkedList<String>();
		String cfg = null;
		while(iter.hasNext()) {
			queue.offer(iter.next());
			while(!queue.isEmpty())
				cfg = queue.poll();
				// '.' �� ���� ��� '.'�� �߰��Ͽ� result�� queue�� �־���
				if(!cfg.contains(".")) {
					StringBuffer cfgPlusDotSymbol = new StringBuffer();
					cfgPlusDotSymbol.append(cfg);
					cfgPlusDotSymbol.insert(cfg.indexOf('>') + 1,".");
					System.out.println(cfgPlusDotSymbol);
					queue.offer(cfgPlusDotSymbol.toString());
					result.add(cfgPlusDotSymbol.toString());
				}else {
					
				}
		}
		return result;
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

}
