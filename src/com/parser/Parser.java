package com.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction;

public class Parser {

	public static void main(String[] args) {

		LinkedList<LinkedList<String>> C0 = new LinkedList<LinkedList<String>>();

		FileIO file = new FileIO();
		file.load();

		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
		
		//<NonTerminal, nonterminal�� �����ϴ� Grammer List>�� ���� Hash table ���� ClOSURE���� �̿�
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();
		Iterator<String> iter = CFG.iterator();
		
		// CFG�� Hash Table�� ����
		while(iter.hasNext()) {
			String R = iter.next();
			char Nonterminal = R.charAt(0);
			LinkedList<String> GrammerList;
			
			if(CFG_HashMap.containsKey(Nonterminal)) {
				GrammerList = CFG_HashMap.get(Nonterminal);
			}else {
				GrammerList = new LinkedList<String>();
			}
			
			GrammerList.add(R);
			CFG_HashMap.put(Nonterminal, GrammerList);
		}
		
		
	}

	public List<String> CLOSURE(List<String> rule, HashMap<Character, LinkedList<String>> CFG) {
		Iterator<String> iter = rule.iterator();
		List<String> result = new LinkedList<String>();
		Queue<String> queue = new UniqueQueue<String>();
		String cfg = null; // CFG�� �� �ϳ��� �ǹ�
		char markSymbol;
		while(iter.hasNext()) {
			queue.offer(iter.next());
			while(!queue.isEmpty())
				cfg = queue.poll();
				
				
				// '.' �� ���� ��� '.'�� �߰����� 
				if(!cfg.contains(".")) {
					StringBuffer cfgPlusDotSymbol = new StringBuffer();
					cfgPlusDotSymbol.append(cfg);	
					cfgPlusDotSymbol.insert(cfg.indexOf('>') + 1,".");
					cfg = cfgPlusDotSymbol.toString();
					
					//�׸��� result��  �־���
					result.add(cfg);
					
					// ����  mark symbol �� Nonterminal�� ��� Nontermianl�鿡 �ش�Ǵ� rule�� ť�� �־���
					markSymbol = cfg.charAt(cfg.indexOf('.') + 1);
					if(Character.isUpperCase(markSymbol)) {
						LinkedList<String> temp = CFG.get(markSymbol);
						Iterator<String> tempIter = temp.iterator();
						
						while(tempIter.hasNext()) {
							queue.add(tempIter.next());
						}	
					}
					
				// '.'�� �ִ°�� ��� .�� �������� �ָ鼭 ť�� �ִ´�.
				}else {			
					
					//'.'�� ��ĭ �ڷ� ����
					
					
					//����� �־���
					result.add(cfg);
					
					//REDUCE '.'�� �������� �����ִ� ��쿣 ť�� ���� �ʰ� while�� �ݺ�
					if(cfg.charAt('.') == cfg.length()-1 ) {
						continue;
					}
					
					// ���� �빮�ڷ� �����Ѵٸ� �빮�ڷ� �����ϴ¾� �� queue�� �־���
					markSymbol = cfg.charAt(cfg.indexOf('.') + 1);
					if(Character.isUpperCase(markSymbol)) {
												
						LinkedList<String> temp = CFG.get(markSymbol);
						Iterator<String> tempIter = temp.iterator();
						
						while(tempIter.hasNext()) {
							queue.add(tempIter.next());
						}	
					}
					
					

					
					
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
