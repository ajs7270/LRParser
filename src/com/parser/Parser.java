package com.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction;

import org.omg.CosNaming.IstringHelper;

public class Parser {

	static LinkedHashSet<LinkedList<String>> C0 = new LinkedHashSet<LinkedList<String>>();
	static LinkedList<String> CFG;
	static HashMap<Character, LinkedList<String>> CFG_HashMap;
	public static void main(String[] args) {
		
		FileIO file = new FileIO();
		file.load();

		CFG = (LinkedList<String>) file.getRule();

		// <NonTerminal, nonterminal�� �����ϴ� Grammer List>  �� ���� Hash table ���� ClOSURE���� �̿�
		CFG_HashMap = Parser.to_HashMap(CFG);
		
		LinkedList<String> rule = new LinkedList<String>();
		
		//I0�� ����� ���� ù��° ���� �־��ش�.
		rule.add(CFG.peek());
		//I0�� �־��ش�.
		C0.add(Parser.CLOSURE(rule, CFG_HashMap));
		//I1, I2, ...��������ش�.
		Parser.GOTO();
		System.out.println(C0);
	}
	
	
	
	public static HashMap<Character, LinkedList<String>> to_HashMap(LinkedList<String> CFG){
		Iterator<String> iter = CFG.iterator();
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();

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
	
		return CFG_HashMap; 
	}
	
	/*
	 * [S>E, E>E+T, E>T, T>T*F, T>F, F>(E), F>x]��� CFG�� ���� ��,
	 * 
	 * [S>E]�� ������
	 * [S->.E, E->.E+T, E->.T, T->.T*F, T->.F, F->.(E), F->.x]�� Linked list�� ��ȯ����
	 */
	public static LinkedList<String> CLOSURE(LinkedList<String> rule, HashMap<Character, LinkedList<String>> CFG) {
		Iterator<String> iter = rule.iterator();
		LinkedList<String> result = new LinkedList<String>();
		Queue<String> queue = new UniqueQueue<String>();
		String cfg = null; // CFG�� �� �ϳ��� �ǹ�
		char markSymbol;
		while(iter.hasNext()) {
			queue.add((String)iter.next());
	
			while(!queue.isEmpty()) {
				
				cfg = queue.poll();
				
				// '.' �� ���� ��� '.'�� �߰����� 
				if(!cfg.contains(".")) {					
					cfg = cfg.replace(">", "->.");
	
					//�׸��� result��  �־���
					result.add(cfg);
					
					// ����  mark symbol �� Nonterminal�� ��� Nonterminal�鿡 �ش�Ǵ� rule�� ť�� �־���
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
					
					//REDUCE '.'�� �������� �����ִ� ��쿣 ť�� ���� �ʰ� while�� �ݺ�
					// �� '.'�� �������̸� ť�� ���� �ʴ´�.
					if(cfg.indexOf('.') == cfg.length()-1 ) {
						result.add(cfg);
						continue;
					}
					
					//'.'�� ��ĭ �ڷ� ����
					int index = cfg.indexOf(".");
					StringBuilder tempSub = new StringBuilder(cfg.substring(index, index+1));					
					cfg = cfg.replace(tempSub.toString(),tempSub.reverse().toString());
					
					//����� �־���
					result.add(cfg);
					
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
		}	
		return result;
	}

	public static void GOTO() {
		//������ ���´� ��? I1, I2, I3 �����ϱ� ���ؼ� I1�� CFG�� ���� Hash Map�� ������ �Ѵ�.
		// �� Hash Map�� ������ ���� I�� ���ϱ� ���� �ʿ��� ���̰� �������� �ʿ��Ҷ� �ٽ� ���� �� �����ϱ� temp�� ����������.
		// �׸���  �� Hash Map�� Non terminal �� key�� ������ �ְ�, key�� �ѹ� �� ��ȸ���� hashIter�� �־�� �ڳ�
		// Non terminal�� key�� �̿��� ���� LinkedList�� CLOUSER�Լ��� �־ I���� ���� ���� �̶� I�� C0�� Ȯ���Ͽ� �ߺ��Ǿ��ִ��� Ȯ���ϰ� 
		// C0�� �ߺ��Ǿ��ִ��� Ȯ���ؾ��ϴϱ� �׳� LinkedList ��ſ� LinkedHashSet�������.
		// ť�� ������ �ϳ�? �Ƴ� ���� ť�� ���� �ʿ�� ���ڴ�. �ϴ� I0�� �̿��ؼ� I0��  iter�� ������ ���� C0�� �ִٰ�  iter�� ������ ���� C0�� ���Ѵ�.
		// �׷� �� Iiter�� �־�߰ڳ�!
		// �����غ��� �ʿ��Ѱ� temp_hashMap, HashIter, C0, CIter �׷� �̷��� 4���� ������ �ǰڴ�!!!
		// ���߿��� C0�� �̹� ����Ǿ� �ִ°Ŵϱ� 
		Iterator<LinkedList<String>> CIter = C0.iterator();
		Iterator<Character> HashIter;
		HashMap<Character, LinkedList<String>> Temp_CFG_HashMap;
		 new HashMap<Character, LinkedList<String>>();
		while(CIter.hasNext()) {
			Temp_CFG_HashMap = Parser.to_HashMap(CIter.next());
			HashIter = Temp_CFG_HashMap.keySet().iterator();
			while(HashIter.hasNext()) {
				LinkedList<String> result = Parser.CLOSURE(Temp_CFG_HashMap.get(HashIter.next()), CFG_HashMap);
				C0.add(result);
			}
		}

	}

}
