package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Stream;

import interpreter.CuteInterpreter;
import parser.ast.*;

public class ParserMain {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String input_string = scanner.nextLine();
			if (input_string.equals("")) {
				break; 
			} else {
				System.out.print("...");
				CuteParser cuteParser = new CuteParser(input_string);
				Node parseTree = cuteParser.parseExpr();
				CuteInterpreter i = new CuteInterpreter();
				Node resultNode = i.runExpr(parseTree);
				NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
				System.out.println("");
			}
		}
	}
}
