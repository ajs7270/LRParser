package com.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction;

import org.omg.CosNaming.IstringHelper;

import com.sun.javafx.geom.transform.GeneralTransform3D;

public class Parser {

	static LinkedHashSet<LinkedList<String>> C0 = new LinkedHashSet<LinkedList<String>>();
	LinkedList<String> CFG;
	static HashMap<Character, LinkedList<String>> CFG_HashMap;
	
	public Parser(LinkedList<String> CFG) {
		// TODO Auto-generated constructor stub
		this.CFG = CFG;
		setStart();
		
		this.CFG_HashMap = setHashMap(this.CFG);
	
		//I0을 넣어준다.
		C0.add(this.CFG);
	}
	
	public LinkedHashSet<LinkedList<String>> getC0(){
		return C0;
	}
	/*
	 * [S>E, E>E+T, E>T, T>T*F, T>F, F>(E), F>x]라는 CFG를 가질 때
	 * [S->.E, E->.E+T, E->.T, T->.T*F, T->.F, F->.(E), F->.x] 로 바꿔줌 
	 */
	public void setStart() {
		LinkedList<String> startCFG = new LinkedList<String>();
		Iterator<String> iter = this.CFG.iterator();
		String rule = "";
		
		while(iter.hasNext()) {
			rule = iter.next();
			rule = rule.replace(">", "->.");
			startCFG.add(rule);
		}
		
		this.CFG = startCFG;
	}
	
	// .이 마지막에 있나 확인
	public static boolean canGoNext(String rule) {
		if(rule.indexOf('.') == rule.length()-1 ) {
			return false;
		}else {
			return true;
		}
	}
	
	// .을 한칸 뒤로 보냄
	public static LinkedList<String> goNext(LinkedList<String> CFG) {
		
		LinkedList<String> result = new LinkedList<String>();
		Iterator<String> iter = CFG.iterator();
		
		while(iter.hasNext()) {
			
			String rule = iter.next();
			
			int index = rule.indexOf(".");
			
			//점이 마지막에 없을때 바꿔줌
			if(canGoNext(rule)) {
				StringBuilder tempSub = new StringBuilder(rule.substring(index, index+2));							
				result.add(rule.replace(tempSub.toString(),tempSub.reverse().toString()));
			}else {
				result.add(rule);				
			}		
		}
		return result;
	}
	
	// markSymbol이 terminal인지 알려줌
	public static boolean markisTermianl(String rule) {
		char markSymbol;
		// .이 맨 끝에 있을경우 끝남으로 terminal을 리턴
		if(!canGoNext(rule))
			return true;
		
		markSymbol = getMark(rule);
		if(Character.isUpperCase(markSymbol))
			return false;
		else
			return true;	
	}
	
	// CFG rule을 하나 넣어주면 markSymbol을 리턴해준다.
	public static char getMark(String rule) {
		return rule.charAt(rule.indexOf('.') + 1);
	}
	
	//CFG를 받으면 HashMap으로 만들어준다. 
	public HashMap<Character, LinkedList<String>> setHashMap(LinkedList<String> CFG){
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
	
	
	// CLOSURE가 알고있어야하는 내용 
	// CFG, markSymbol 이거 두개만 알고있어도 할수 잇음 그럼 난 CFG는 알고 잇으니까 markSymbol만 알수있게 넘겨주면 되겠군.
	public static LinkedList<String> CLOSURE(LinkedList<String> rule) {
		// CLOSURE을 찾을 rule을 넣어준다.
		Iterator<String> iter = rule.iterator();
		LinkedList<String> result = new LinkedList<String>();
		Queue<String> queue = new UniqueQueue<String>();
		Queue<String> resultQueue = new UniqueQueue<String>();
		String cfg = null; // CFG의 룰 하나를 의미
		char markSymbol;
		
		// 일단 룰들을 큐에 넣는다
		while(iter.hasNext()) {
			queue.add((String)iter.next());
		}
		
		// 큐를 돌리기 시작.
		while(!queue.isEmpty()) {	
			cfg = queue.poll();	
			resultQueue.add(cfg);
			//markSymbol이 non terminal 일 때 큐에 넣어준다. 
			if(!markisTermianl(cfg)) {
				markSymbol = getMark(cfg);
				LinkedList<String> temp = CFG_HashMap.get(markSymbol);
				Iterator<String> tempIter = temp.iterator();
				
				//markSymbol들로 시작되는 걸 다 큐에 넣어준다. 
				while(tempIter.hasNext()) {
					queue.add(tempIter.next());
				}	
			}		
		}	
		
		//result큐에 쌓인 결과값들은 linkedList로 바꿔준다.
		while(!resultQueue.isEmpty()) {
			result.add(resultQueue.poll());
		}
		
		return result;
	}

	
	
	public static void GOTO() {
		// GOTO가 해야할일 
		// 점을 한칸 옮겨준다.
		// CLOSURE에 한칸 옮겨준 룰들을 넣어준다. 
		// 한칸 옮겨준 룰들은 markSymbol에 따라 정렬되어있는 값들이다.
		
		// 그럼 마크 Symbol에 따라 정렬되어있는 Hashmap을 만드는 함수를 가지고 있어야 한다.
		
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
		HashMap<Character, LinkedList<String>> Temp_CFG_HashMap;
		Iterator<Character> HashIter;
		
		// 일단 우리가 다룰 CFG들은 C0에 들어있다.
		// C0에 들어있는 CFG를 꺼내서 점을 한칸씩 옮겨준다.  
		// markSymbol에 따라 Hashmap으로 만든다음
		// 그 HashMap을 markSymbol에 따라 하나씩 꺼내가며 CLOSURE를 해주며
		// 그 결과값들은 C0에 저장한다. 
	
		int i = 0; 
		while(CIter.hasNext()) {
			// C0에 있는 CFG를 하나 꺼낸다.
			LinkedList<String> curCFG = CIter.next();
			System.out.println(C0);
			// 점을 한칸 옮겨준다.
			// 만약 점이 마지막에 있으면 어떡하지?
			// 두가지 경우.
			// 1. 점이 마지막에 있는 curCFG를 없게 만들어준다. 
			// 그럼 점이 마지막에 있는 CFG를 HashMap을 만들때 없애주면 되겠다. 
			// 2. 점이 마지막에 있을경우 따로 처리해준다. (v) 선택
			
			//일단 현재 mark Symbol을 바탕  Hash map을 만들어주고 
			Temp_CFG_HashMap = Mark_Hash(curCFG);
			HashIter = Temp_CFG_HashMap.keySet().iterator();
			curCFG = goNext(curCFG);
			

			while(HashIter.hasNext()) {
				//한칸 뒤로 이동
				curCFG = goNext(Temp_CFG_HashMap.get(HashIter.next()));
				LinkedList<String> result = Parser.CLOSURE(curCFG);
				C0.add(result);
				System.out.println(C0);
			}
			
			
			//이터레이터가 제대로 안돌아서 야매로 바꿈
			i++;
			CIter = C0.iterator();
			for(int j = 0;j < i; j++) {
				CIter.next();
			}
		}

	}
	
	// markSymbol에 따라 정렬되어있는 Hashmap을 만드는 함수
	public static HashMap<Character, LinkedList<String>> Mark_Hash(LinkedList<String> CFG) {
		Iterator<String> iter = CFG.iterator();
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();
		
		// CFG를 markSymbol을 key로 하여 Hash Table에 담음
		while(iter.hasNext()) {
			
			// 일단 하나를 꺼내서 mark Symbol을 찾는다.
			String rule = iter.next();
			
			// 점이 마지막에 있다면 markSymbol을 가져올 수 없기 때문에 
			// 예외처리를 하여 .이 마지막에 있는지 확인한다.
			// 즉 점이 마지막에 없는 rule들만 hashMap에 들어가게 된다.
			if(canGoNext(rule)) {
				char markSymbol = getMark(rule);
				LinkedList<String> GrammerList;
				
				//만약 markSymbol을 Key로 가지고 있을 경우 기존의 리스트에 이번 rule을 추가
				if(CFG_HashMap.containsKey(markSymbol)) {
					GrammerList = CFG_HashMap.get(markSymbol);
				}else {
					GrammerList = new LinkedList<String>();
				}
				GrammerList.add(rule);
				CFG_HashMap.put(markSymbol, GrammerList);	
			}	
		}
		
		return CFG_HashMap;
	}

}
