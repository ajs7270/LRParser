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

		// <NonTerminal, nonterminal로 시작하는 Grammer List>  를 담은 Hash table 생성 ClOSURE에서 이용
		CFG_HashMap = Parser.to_HashMap(CFG);
		
		LinkedList<String> rule = new LinkedList<String>();
		
		//I0을 만들기 위해 첫번째 식을 넣어준다.
		rule.add(CFG.peek());
		//I0을 넣어준다.
		C0.add(Parser.CLOSURE(rule, CFG_HashMap));
		//I1, I2, ...를만들어준다.
		Parser.GOTO();
		System.out.println(C0);
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
	public static LinkedList<String> CLOSURE(LinkedList<String> rule, HashMap<Character, LinkedList<String>> CFG) {
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
					if(cfg.indexOf('.') == cfg.length()-1 ) {
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
		//저장해 놓는다 뭘? I1, I2, I3 를구하기 위해선 I1의 CFG를 보고 Hash Map을 만들어야 한다.
		// 이 Hash Map은 어차피 다음 I를 구하기 위해 필요한 것이고 언제든지 필요할때 다시 만들 수 있으니까 temp로 선언해주자.
		// 그리고  그 Hash Map은 Non terminal 을 key로 가지고 있고, key를 한번 쭉 순회해줄 hashIter가 있어야 겠네
		// Non terminal의 key를 이용해 빼온 LinkedList를 CLOUSER함수에 넣어서 I값을 구함 만약 이때 I가 C0을 확인하여 중복되어있는지 확인하고 
		// C0가 중복되어있는지 확인해야하니까 그냥 LinkedList 대신에 LinkedHashSet사용하자.
		// 큐를 만들어야 하나? 아냐 굳이 큐를 만들 필요는 없겠다. 일단 I0을 이용해서 I0의  iter가 끝날때 까지 C0에 넣다가  iter가 끝나면 다음 C0을 구한다.
		// 그럼 또 Iiter가 있어야겠네!
		// 정리해보자 필요한건 temp_hashMap, HashIter, C0, CIter 그럼 이렇게 4개만 있으면 되겠다!!!
		// 그중에서 C0은 이미 선언되어 있는거니까 
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
