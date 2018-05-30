package com.parser;

import java.util.LinkedList;
import java.util.List;

public class Parser {

	public static void main(String[] args) {

		LinkedList<LinkedList<String>> C0 = new LinkedList<LinkedList<String>>();

		FileIO file = new FileIO();
		file.load();

		LinkedList<String> CFG = (LinkedList<String>) file.getRule();
	}

	public List<String> CLOSURE(List<String> rule) {
		List<String> result = new LinkedList<String>();

		return rule;
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
	
	public 

}
