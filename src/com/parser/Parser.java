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

	public static void main(String[] args) {

		LinkedList<LinkedList<String>> C0 = new LinkedList<LinkedList<String>>();

		FileIO file = new FileIO();
		file.load();

		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
		
		UniqueQueue<String> jisu = new UniqueQueue<String>();
		
		// <NonTerminal, nonterminal로 시작하는 Grammer List>  를 담은 Hash table 생성 ClOSURE에서 이용
		HashMap<Character, LinkedList<String>> CFG_HashMap = Parser.to_HashMap(CFG);
		
		LinkedList<String> rule = new LinkedList<String>();
		System.out.println(Parser.CLOSURE(rule, CFG_HashMap));
	}
	
	public static HashMap<Character, LinkedList<String>> to_HashMap(LinkedList<String> CFG){
		Iterator<String> iter = CFG.iterator();
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();

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
		return CFG_HashMap; 

	}
	
	/*
	 * [S>E, E>E+T, E>T, T>T*F, T>F, F>(E), F>x]라는 CFG를 가질 때,
	 * 
	 * [S>E]가 들어오면
	 * [S->.E, E->.E+T, E->.T, T->.T*F, T->.F, F->.(E), F->.x]인 Linked list로 반환해줌
	 */
	public static List<String> CLOSURE(LinkedList<String> rule, HashMap<Character, LinkedList<String>> CFG) {
		Iterator<String> iter = rule.iterator();
		LinkedList<String> result = new LinkedList<String>();
		Queue<String> queue = new UniqueQueue<String>();
		String cfg = null; // CFG의 룰 하나를 의미
		char markSymbol;
		while(iter.hasNext()) {
			queue.add((String)iter.next());
	
			while(!queue.isEmpty()) {
				
				cfg = queue.poll();
				
				// '.' 이 없는 경우 '.'을 추가해줌 
				if(!cfg.contains(".")) {					
					cfg = cfg.replace(">", "->.");
	
					//그리고 result에  넣어줌
					result.add(cfg);
					
					// 만약  mark symbol 이 Nonterminal일 경우 Nonterminal들에 해당되는 rule을 큐에 넣어줌
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
					
					//REDUCE '.'이 마지막에 찍혀있는 경우엔 큐에 넣지 않고 while문 반복
					// 즉 '.'이 마지막이면 큐에 들어가지 않는다.
					if(cfg.charAt('.') == cfg.length()-1 ) {
						result.add(cfg);
						continue;
					}
					
					//'.'을 한칸 뒤로 보냄
					int index = cfg.indexOf(".");
					StringBuilder tempSub = new StringBuilder(cfg.substring(index, index+1));					
					cfg = cfg.replace(tempSub.toString(),tempSub.reverse().toString());
					
					//결과로 넣어줌
					result.add(cfg);
					
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
		}	
		return result;
	}

	public static void GOTO() {
		
	}

}
