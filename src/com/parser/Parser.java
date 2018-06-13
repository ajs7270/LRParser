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
	
		//I0�� �־��ش�.
		C0.add(this.CFG);
	}
	
	public LinkedHashSet<LinkedList<String>> getC0(){
		return C0;
	}
	/*
	 * [S>E, E>E+T, E>T, T>T*F, T>F, F>(E), F>x]��� CFG�� ���� ��
	 * [S->.E, E->.E+T, E->.T, T->.T*F, T->.F, F->.(E), F->.x] �� �ٲ��� 
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
	
	// .�� �������� �ֳ� Ȯ��
	public static boolean canGoNext(String rule) {
		if(rule.indexOf('.') == rule.length()-1 ) {
			return false;
		}else {
			return true;
		}
	}
	
	// .�� ��ĭ �ڷ� ����
	public static LinkedList<String> goNext(LinkedList<String> CFG) {
		
		LinkedList<String> result = new LinkedList<String>();
		Iterator<String> iter = CFG.iterator();
		
		while(iter.hasNext()) {
			
			String rule = iter.next();
			
			int index = rule.indexOf(".");
			
			//���� �������� ������ �ٲ���
			if(canGoNext(rule)) {
				StringBuilder tempSub = new StringBuilder(rule.substring(index, index+2));							
				result.add(rule.replace(tempSub.toString(),tempSub.reverse().toString()));
			}else {
				result.add(rule);				
			}		
		}
		return result;
	}
	
	// markSymbol�� terminal���� �˷���
	public static boolean markisTermianl(String rule) {
		char markSymbol;
		// .�� �� ���� ������� �������� terminal�� ����
		if(!canGoNext(rule))
			return true;
		
		markSymbol = getMark(rule);
		if(Character.isUpperCase(markSymbol))
			return false;
		else
			return true;	
	}
	
	// CFG rule�� �ϳ� �־��ָ� markSymbol�� �������ش�.
	public static char getMark(String rule) {
		return rule.charAt(rule.indexOf('.') + 1);
	}
	
	//CFG�� ������ HashMap���� ������ش�. 
	public HashMap<Character, LinkedList<String>> setHashMap(LinkedList<String> CFG){
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
	
	
	// CLOSURE�� �˰��־���ϴ� ���� 
	// CFG, markSymbol �̰� �ΰ��� �˰��־ �Ҽ� ���� �׷� �� CFG�� �˰� �����ϱ� markSymbol�� �˼��ְ� �Ѱ��ָ� �ǰڱ�.
	public static LinkedList<String> CLOSURE(LinkedList<String> rule) {
		// CLOSURE�� ã�� rule�� �־��ش�.
		Iterator<String> iter = rule.iterator();
		LinkedList<String> result = new LinkedList<String>();
		Queue<String> queue = new UniqueQueue<String>();
		Queue<String> resultQueue = new UniqueQueue<String>();
		String cfg = null; // CFG�� �� �ϳ��� �ǹ�
		char markSymbol;
		
		// �ϴ� ����� ť�� �ִ´�
		while(iter.hasNext()) {
			queue.add((String)iter.next());
		}
		
		// ť�� ������ ����.
		while(!queue.isEmpty()) {	
			cfg = queue.poll();	
			resultQueue.add(cfg);
			//markSymbol�� non terminal �� �� ť�� �־��ش�. 
			if(!markisTermianl(cfg)) {
				markSymbol = getMark(cfg);
				LinkedList<String> temp = CFG_HashMap.get(markSymbol);
				Iterator<String> tempIter = temp.iterator();
				
				//markSymbol��� ���۵Ǵ� �� �� ť�� �־��ش�. 
				while(tempIter.hasNext()) {
					queue.add(tempIter.next());
				}	
			}		
		}	
		
		//resultť�� ���� ��������� linkedList�� �ٲ��ش�.
		while(!resultQueue.isEmpty()) {
			result.add(resultQueue.poll());
		}
		
		return result;
	}

	
	
	public static void GOTO() {
		// GOTO�� �ؾ����� 
		// ���� ��ĭ �Ű��ش�.
		// CLOSURE�� ��ĭ �Ű��� ����� �־��ش�. 
		// ��ĭ �Ű��� ����� markSymbol�� ���� ���ĵǾ��ִ� �����̴�.
		
		// �׷� ��ũ Symbol�� ���� ���ĵǾ��ִ� Hashmap�� ����� �Լ��� ������ �־�� �Ѵ�.
		
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
		HashMap<Character, LinkedList<String>> Temp_CFG_HashMap;
		Iterator<Character> HashIter;
		
		// �ϴ� �츮�� �ٷ� CFG���� C0�� ����ִ�.
		// C0�� ����ִ� CFG�� ������ ���� ��ĭ�� �Ű��ش�.  
		// markSymbol�� ���� Hashmap���� �������
		// �� HashMap�� markSymbol�� ���� �ϳ��� �������� CLOSURE�� ���ָ�
		// �� ��������� C0�� �����Ѵ�. 
	
		int i = 0; 
		while(CIter.hasNext()) {
			// C0�� �ִ� CFG�� �ϳ� ������.
			LinkedList<String> curCFG = CIter.next();
			System.out.println(C0);
			// ���� ��ĭ �Ű��ش�.
			// ���� ���� �������� ������ �����?
			// �ΰ��� ���.
			// 1. ���� �������� �ִ� curCFG�� ���� ������ش�. 
			// �׷� ���� �������� �ִ� CFG�� HashMap�� ���鶧 �����ָ� �ǰڴ�. 
			// 2. ���� �������� ������� ���� ó�����ش�. (v) ����
			
			//�ϴ� ���� mark Symbol�� ����  Hash map�� ������ְ� 
			Temp_CFG_HashMap = Mark_Hash(curCFG);
			HashIter = Temp_CFG_HashMap.keySet().iterator();
			curCFG = goNext(curCFG);
			

			while(HashIter.hasNext()) {
				//��ĭ �ڷ� �̵�
				curCFG = goNext(Temp_CFG_HashMap.get(HashIter.next()));
				LinkedList<String> result = Parser.CLOSURE(curCFG);
				C0.add(result);
				System.out.println(C0);
			}
			
			
			//���ͷ����Ͱ� ����� �ȵ��Ƽ� �߸ŷ� �ٲ�
			i++;
			CIter = C0.iterator();
			for(int j = 0;j < i; j++) {
				CIter.next();
			}
		}

	}
	
	// markSymbol�� ���� ���ĵǾ��ִ� Hashmap�� ����� �Լ�
	public static HashMap<Character, LinkedList<String>> Mark_Hash(LinkedList<String> CFG) {
		Iterator<String> iter = CFG.iterator();
		HashMap<Character, LinkedList<String>> CFG_HashMap = new HashMap<Character, LinkedList<String>>();
		
		// CFG�� markSymbol�� key�� �Ͽ� Hash Table�� ����
		while(iter.hasNext()) {
			
			// �ϴ� �ϳ��� ������ mark Symbol�� ã�´�.
			String rule = iter.next();
			
			// ���� �������� �ִٸ� markSymbol�� ������ �� ���� ������ 
			// ����ó���� �Ͽ� .�� �������� �ִ��� Ȯ���Ѵ�.
			// �� ���� �������� ���� rule�鸸 hashMap�� ���� �ȴ�.
			if(canGoNext(rule)) {
				char markSymbol = getMark(rule);
				LinkedList<String> GrammerList;
				
				//���� markSymbol�� Key�� ������ ���� ��� ������ ����Ʈ�� �̹� rule�� �߰�
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
