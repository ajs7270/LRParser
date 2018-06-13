package com.parser;

import java.util.LinkedList;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileIO file = new FileIO();
		file.load();
		
		// 파일에서 읽어온 rule을 이용하여 parser을 만든다. 
		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
		Parser parser = new Parser(CFG);
			
		// <NonTerminal, nonterminal로 시작하는 Grammer List>  를 담은 Hash table 생성 ClOSURE에서 이용
		
		
		LinkedList<String> rule = new LinkedList<String>();
		
		parser.GOTO();
		System.out.println(parser.getC0());
	}

}
