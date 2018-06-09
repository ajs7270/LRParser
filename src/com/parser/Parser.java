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
		
		//<NonTerminal, nonterminal로 시작하는 Grammer List>를 담은 Hash table 생성 ClOSURE에서 이용
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();
		Iterator<String> iter = CFG.iterator();
		
		// CFG를 Hash Table에 담음
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
		String cfg = null; // CFG의 룰 하나를 의미
		char markSymbol;
		while(iter.hasNext()) {
			queue.offer(iter.next());
			while(!queue.isEmpty())
				cfg = queue.poll();
				
				
				// '.' 이 없는 경우 '.'을 추가해줌 
				if(!cfg.contains(".")) {
					StringBuffer cfgPlusDotSymbol = new StringBuffer();
					cfgPlusDotSymbol.append(cfg);	
					cfgPlusDotSymbol.insert(cfg.indexOf('>') + 1,".");
					cfg = cfgPlusDotSymbol.toString();
					
					//그리고 result에  넣어줌
					result.add(cfg);
					
					// 만약  mark symbol 이 Nonterminal일 경우 Nontermianl들에 해당되는 rule을 큐에 넣어줌
					markSymbol = cfg.charAt(cfg.indexOf('.') + 1);
					if(Character.isUpperCase(markSymbol)) {
						LinkedList<String> temp = CFG.get(markSymbol);
						Iterator<String> tempIter = temp.iterator();
						
						while(tempIter.hasNext()) {
							queue.add(tempIter.next());
						}	
					}
					
				// '.'이 있는경우 계속 .을 증가시켜 주면서 큐에 넣는다.
				}else {			
					
					//'.'을 한칸 뒤로 보냄
					
					
					//결과로 넣어줌
					result.add(cfg);
					
					//REDUCE '.'이 마지막에 찍혀있는 경우엔 큐에 넣지 않고 while문 반복
					if(cfg.charAt('.') == cfg.length()-1 ) {
						continue;
					}
					
					// 만약 대문자로 시작한다면 대문자로 시작하는애 다 queue에 넣어줌
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

		// '.'의 위치 확인
		int head = rule.indexOf('.');

		// '.'이 맨 마지막에 위치하지 않은 경우
		if (head + 1 < rule.length()) {
			// '.'다음이 대문자이면 non-terminal 이기 때문에 false return
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
