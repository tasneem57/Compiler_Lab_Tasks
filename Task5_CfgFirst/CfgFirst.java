//package csen1002.main.task6;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @name Tasneem Nabil Elghobashy
 */

public class CfgFirst{
	String cfg;

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	public CfgFirstFollow(String cfg) {
		// TODO Auto-generated constructor stub
		this.cfg = cfg;
	}

	/**
	 * Calculates the First Set of each variable in the CFG.
	 * 
	 * @return A string representation of the First of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String first() {
		// TODO Auto-generated method stub
		//Splitting the input
		String[] input_arr = cfg.split("#");
		//String[] variables = input_arr[0].split(";");
		//String[] terminals = input_arr[1].split(";");
		String[] rules = input_arr[2].split(";");	
		
		// ArrayList of firsts <letter, arrayList<String>> which are the firsts of the Variable
		//and ArrayList of follows <letter, arrayList<String>> which are the firsts of the Variable
		ArrayList<Rule> first_arrlist = new ArrayList<Rule>();
		ArrayList<Rule> follow_arrlist = new ArrayList<Rule>();

		//ArrayList of Rules to contain the rules <letter,arrayList<String>)
		ArrayList<Rule> rules_arrlist = new ArrayList<Rule>();
		for (String rule:rules) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split(",");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);
			Rule r = new Rule(lhs,rhs_arrlst);
			rules_arrlist.add(r);
			Rule r2 = new Rule(lhs,new ArrayList<String>());
			first_arrlist.add(r2);
			follow_arrlist.add(r2);
	   }
		Rule rule;
		boolean change = true;
		while (change) {
			change = false;
			//loop over rules
			for(int i=0; i<rules_arrlist.size();i++) {
				rule = rules_arrlist.get(i);
				//loop over lexemes of current rule
				for (int m=0; m<rule.lexemes.size(); m++) {
					String	s = rule.lexemes.get(m);
					for(int k=0; k<first_arrlist.size(); k++) {
						Rule current_var_first = first_arrlist.get(k);
						if (current_var_first.letter.equals(rule.letter)) {
							//Loop over current lexeme characters
							boolean contains_e = true;
							for(int c=0;c<s.length() && contains_e;c++) {
								char vis = s.charAt(c);
								contains_e  = false;
								// if the lexeme starts with terminal, Add it to the firsts of the current Var.
								if (s.charAt(c)>='a' && s.charAt(c)<='z') {
									if (!current_var_first.lexemes.contains(s.charAt(c)+"")) {
										current_var_first.lexemes.add(s.charAt(c)+"");
										change = true;
										break;
								}}
								//Else if the lexeme starts with a Variable.
								else {
									//Loop over the firsts arrList to get the firsts of this Variable.
									for(int n=0; n<first_arrlist.size(); n++) {
										Rule rule_first_var = first_arrlist.get(n);
										//Get the firsts of that Variable.
										if (rule_first_var.letter.equals(s.charAt(c)+"")){
											//Loop over firsts of that Var.
											for(String first: rule_first_var.lexemes) {
												//Add them to current_var_firsts.
												if (!current_var_first.lexemes.contains(first) && !first.equals("e")) {
													current_var_first.lexemes.add(first);
													change = true;
												}
											}
											//Check if it contains 'e'
											if (rule_first_var.lexemes.contains("e")) {
												contains_e = true;
												if(c==s.length()-1 && !current_var_first.lexemes.contains("e")) {
													current_var_first.lexemes.add("e"); 
													change=true;}											
											}
											break;
										}
		}}}}}}}}
		String firsts ="";
		for(Rule r:first_arrlist) {
			Collections.sort(r.lexemes);
			firsts += r.letter+"/"+String.join("", r.lexemes)+";";
		}
		firsts = firsts.substring(0 , firsts.length()-1);		
		return firsts;
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
