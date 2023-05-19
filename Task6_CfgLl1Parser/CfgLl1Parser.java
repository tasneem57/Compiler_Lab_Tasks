//package csen1002.main.task7;

import java.util.ArrayList;


/**
 * @name Tasnem Nabil Elghobashy
 */

public class CfgLl1Parser {
	String input;
	String input_To_parse;
	
	public static ArrayList<Rule> formArrayOfRules(String[] rules){
		ArrayList<Rule> rules_arrlist = new ArrayList<Rule>();
		for (String rule:rules) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split(",");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);	
			Rule r = new Rule(lhs,rhs_arrlst);
			rules_arrlist.add(r);
		}
		return rules_arrlist;
	}
	
	public static void visualizeArrListOfRules(ArrayList<Rule> rules_arrlist) {
		for(Rule r:rules_arrlist)System.out.println(r.letter+" "+r.lexemes);	
		System.out.println("------");
	}
	
	public static String getRuleFromParseTable(ArrayList<ParseCell> parse_table,String var,String term) {
		String res = "not found";
		for(ParseCell pc: parse_table) {
			if (pc.var.equals(var) && pc.term.equals(term)) {res = pc.rule; break;}
				
		}
		return res;
	}

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG, the First sets of
	 *            each right-hand side, and the Follow sets of each variable. The
	 *            string representation follows the one in the task description
	 */
	public CfgLl1Parser(String input) {
		this.input = input;
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input_To_parse) {
		this.input_To_parse = input_To_parse;
		String[] input_arr = input.split("#");
		String[] variables = input_arr[0].split(";");
		String[] terminalsList = input_arr[1].split(";");
		String[] rules = input_arr[2].split(";");
		String[] firsts = input_arr[3].split(";");
		String[] follows = input_arr[4].split(";");
		
		//ArrayList of terminals to add $
		ArrayList<String> terminals = new ArrayList<String>();
		for(String term:terminalsList) terminals.add(term);
		terminals.add("$");
		
		// ArrayList <letter, arrayList<String>> for the rules, firsts and follows.
		ArrayList<Rule> rules_arrlist = formArrayOfRules(rules);
		ArrayList<Rule> firsts_arrlist = formArrayOfRules(firsts);
		ArrayList<Rule> follows_arrlist = new ArrayList<Rule>();
		for (String rule:follows) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split("(?!^)");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);	
			Rule r = new Rule(lhs,rhs_arrlst);
			follows_arrlist.add(r);
		}
		visualizeArrListOfRules(rules_arrlist);
		visualizeArrListOfRules(firsts_arrlist);
		visualizeArrListOfRules(follows_arrlist);
		
		boolean found = false;
		boolean flag = true;
		
		//ArrayList of ParseCells
		ArrayList<ParseCell> parse_table =new ArrayList<ParseCell>();	
		
		
		//Creating parse table
		for (Rule rule:rules_arrlist) { //loop over rules
		for(Rule first:firsts_arrlist) { //loop over firsts 
		if (rule.letter.equals(first.letter)) {  //S,S
			for(String first_terminal: first.lexemes) { //loop over firsts of same rule
				found =false;
				if(first_terminal.equals("e")) 
				{
					for(Rule follow:follows_arrlist) {
					if(follow.letter.equals(rule.letter)){
					for(String follow_terminal:follow.lexemes) {
						ParseCell cell = new ParseCell(rule.letter,follow_terminal,"e");
						parse_table.add(cell);
					}}}
					found = true;
				}else {
					boolean exist = false;
					for(String lexeme:rule.lexemes) {
						if (first_terminal.length()==1 && !first_terminal.equals("e") &&lexeme.startsWith(first_terminal)) {
							exist = true;
						}
					}
					for(String lexeme:rule.lexemes) { //loop over lexemes
						if (first_terminal.length()==1 && !first_terminal.equals("e") &&lexeme.startsWith(first_terminal))
						{
							//Normal Case
							ParseCell cell = new ParseCell(rule.letter,first_terminal,lexeme);
							parse_table.add(cell);
							found = true;
						}
						else if(!exist && lexeme.charAt(0)>='A'&&lexeme.charAt(0)<='Z') {
								//Start with variable Case
								String var_in_lexem = ""+lexeme.charAt(0);
								for(Rule fir:firsts_arrlist) {
								if(fir.letter.equals(var_in_lexem)){//loop over firsts of start variable
								for(String f:fir.lexemes) {
								if(!f.equals("e")){
							    if(!first_terminal.contains(f)) {
										flag = false;									
							    }}}break;}}
								if (flag) {
									for(int i=0;i<first_terminal.length();i++) {
										if(!(""+first_terminal.charAt(i)).equals("e")) {
										ParseCell cell = new ParseCell(rule.letter,first_terminal.charAt(i)+"",lexeme);
										parse_table.add(cell);
										}else {
											for(Rule follow:follows_arrlist) {
												if(follow.letter.equals(rule.letter)){
												for(String follow_terminal:follow.lexemes) {
													ParseCell cell = new ParseCell(rule.letter,follow_terminal,"e");
													parse_table.add(cell);
													found = true;
												}}}			
										}
									}
								}else flag=true;
						}
						if (found) break;
						
					}
				}
			}
					
		}}} 
		
		//Fill empty cells in parse table with "ERROR"
		for(String var:variables) {
			for(String term:terminals) {
				if (getRuleFromParseTable(parse_table,var,term).equals("not found")){
					ParseCell cell = new ParseCell(var,term,"ERROR");
					parse_table.add(cell);
				}
			}
		}
		//Visualize Parse table 
		for(ParseCell pc: parse_table) System.out.println(pc.var+" "+pc.term+ " "+pc.rule);
		
		// ------------------------Now we have Parse Table parse_table---------------------------------------
		// parse_table is arrayList of ParseCell(var,term,rule)
		
		input_To_parse+="$";
		String result = "S";
		ArrayList<String> stack = new ArrayList<String>();
		stack.add("$");
		stack.add("S");
		String top_of_stack;
		String previous = result;
		for(int i =0; i<input_To_parse.length();) {//loop over String input
			String currunt = input_To_parse.charAt(i)+"";
			if(currunt.equals("$") && stack.get(stack.size()-1).equals("$")){//Stopping condition
				break;
			}else {
				top_of_stack = stack.get(stack.size()-1);
				//check if variable
				if(top_of_stack.charAt(0)>='A'&&top_of_stack.charAt(0)<='Z') {
					String rule = getRuleFromParseTable(parse_table,top_of_stack,currunt);
					if (rule.equals("e")) {
						stack.remove(stack.size()-1);
						previous = previous.replaceFirst("(?:"+top_of_stack+")+", "");
						result+=";"+previous;
					}
					else if(!rule.equals("ERROR")) {
						previous = previous.replaceFirst("(?:"+top_of_stack+")+", rule);
						result+=";"+previous;
						stack.remove(stack.size()-1);
						for(int j = rule.length()-1;j>=0;j--) {
							stack.add(rule.charAt(j)+"");	
						}	
					}else {
						//if rule is Error --> ? 
						result+=";ERROR";
						break;
					}
				}
				//check if terminal
				else if(top_of_stack.charAt(0)>='a'&&top_of_stack.charAt(0)<='z') {
					if (top_of_stack.equals(currunt)) {
						stack.remove(stack.size()-1);
						i++;
					}else {
						result+=";ERROR";
						break;
					}
					
				}//check if $
				else if (top_of_stack.equals("$")) {
					if (currunt.equals("$")) {
						break;
					}else {
						result+=";ERROR";
						break;
					}
				}		
				
			}			
		}
		return result;
	}

}

//class ParceCell
class ParseCell {
	String var;
	String term;
	String rule;
	public ParseCell (String var,String term,String rule) {
		this.var = var;
		this.term = term;
		this.rule = rule;
	}
}

//Class Rule
class Rule {
	String letter;
	ArrayList<String> lexemes;
	
	public Rule (String letter,ArrayList<String> lexemes) {
		this.letter = letter;
		this.lexemes = lexemes;
	}
	
	public ArrayList<String> getNum1() {return this.lexemes;}
	public String getLetter(){return this.letter;}
}
