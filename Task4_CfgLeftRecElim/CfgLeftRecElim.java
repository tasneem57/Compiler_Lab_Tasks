//package csen1002.main.task5;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @name Tasneem Nabil Elghobashy
 */

public class CfgLeftRecElim {
	String cfg;
	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	public CfgLeftRecElim(String cfg) {
		// TODO Auto-generated constructor stub
		this.cfg= cfg;
	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return cfg;
	}

	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
		// TODO Auto-generated method stub
		String input = this.cfg;

		//Splitting the NFA input
		String[] input_arr = input.split("#");
		String[] variables = input_arr[0].split(";");
		String[] terminals = input_arr[1].split(";");
		String[] rules = input_arr[2].split(";");	
		String new_vars="";
		String LRE_result;

		//ArrayList of Rules to contain the rules <letter,arrayList<String>)
		ArrayList<Rule> old_rules_arrlist = new ArrayList<Rule>();
		for (String rule:rules) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split(",");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);
			Rule r = new Rule(lhs,rhs_arrlst);
			old_rules_arrlist.add(r);
	   }
		
		
		//Array List for rules which are already handled
		ArrayList<String> done_rules_vars = new ArrayList<String>();
		//Array List for the new rules like A', B', ....
     	ArrayList<Rule> new_rules_arrlist = new ArrayList<Rule>();
     	
		Rule rule;
		String lex;
		boolean recursive;
		boolean has_done_var;
		
		//looping over the old rules to update them
		for(int i=0; i<old_rules_arrlist.size();i++) {
			System.out.print(i+" ");
					rule = old_rules_arrlist.get(i);
					recursive = false;
					has_done_var = true;
					while (has_done_var) {
							has_done_var = false;
							//looping over the strings of the current rule
							for (int m=0; m<rule.lexemes.size(); m++) {
								String	s = rule.lexemes.get(m);
								
								//check if the rule has a string starts with a variable of the done ones, then replace it with the lexemes of that var
								for(String var: done_rules_vars) {
									if(s.startsWith(var)) {
										for(int k=0;k<old_rules_arrlist.size();k++) {		
											Rule r=old_rules_arrlist.get(k);
											if(r.letter.equals(var)) {
												rule.lexemes.remove(s);
												for(String x:r.lexemes) {
													rule.lexemes.add(m, (x+s.substring(1)));
												    m ++;	}			
								}}}}
								
								//check if this rule has left recursion
								if (s.startsWith(rule.letter)) {recursive = true;}
						   }
							
							for (String s :rule.lexemes) {
								  for(String var: done_rules_vars) {
										if(s.startsWith(var)) {
										has_done_var = true;
										break;}
										if (s.startsWith(rule.letter)) {recursive = true;}
							}if(has_done_var)break;}
					}	
					
					
					//LR Elimination for each rule in the old_rules_arrlist
					if(recursive) {
						ArrayList<String> rhs_arrlst = new ArrayList<String>();
						String new_var = rule.letter+"'";
						new_vars+=";"+new_var;
						for(int j = 0; j<rule.lexemes.size();j++) {
							lex = rule.lexemes.get(j);
							if (lex.startsWith(rule.letter)) {
								rule.lexemes.remove(j);
								rule.lexemes.add(j,".");
								rhs_arrlst.add(lex.substring(1)+new_var);
							}else {
								rule.lexemes.remove(j);
								rule.lexemes.add(j,lex+new_var);
							}
						}	
						//Add the new rules like A', B', .... to new_rules_arrlist
						rhs_arrlst.add("e");
						Rule new_r = new Rule(new_var,rhs_arrlst);
						new_rules_arrlist.add(new_r);	
						
						rule.lexemes.removeAll(Collections.singleton("."));
					}
					done_rules_vars.add(rule.letter);
	    	}	
			
			LRE_result = input_arr[0]+new_vars+"#"+input_arr[1]+"#";
			for(int x=0;x<old_rules_arrlist.size();x++) {
				Rule r=old_rules_arrlist.get(x);
				LRE_result+=r.letter+"/";
				for(int p=0;p<r.lexemes.size();p++) {
					LRE_result+=r.lexemes.get(p);
					if(p!=r.lexemes.size()-1) LRE_result+=",";
				}
			    LRE_result+=";";
		}
		
		for(int x=0;x<new_rules_arrlist.size();x++) {
			Rule r=new_rules_arrlist.get(x);
			LRE_result+=r.letter+"/";
			for(int p=0;p<r.lexemes.size();p++) {
				LRE_result+=r.lexemes.get(p);
				if(p!=r.lexemes.size()-1) LRE_result+=",";
			}
		    if(x!=new_rules_arrlist.size()-1) LRE_result+=";";
		}
		
		this.cfg = LRE_result;
		
		
		
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
