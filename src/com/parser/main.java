package com.parser;

import java.util.LinkedList;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileIO file = new FileIO();
		file.load();
		
		// ���Ͽ��� �о�� rule�� �̿��Ͽ� parser�� �����. 
		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
		Parser parser = new Parser(CFG);
			
		// <NonTerminal, nonterminal�� �����ϴ� Grammer List>  �� ���� Hash table ���� ClOSURE���� �̿�
		
		
		LinkedList<String> rule = new LinkedList<String>();
		
		parser.GOTO();
		System.out.println(parser.getC0());
	}

}
