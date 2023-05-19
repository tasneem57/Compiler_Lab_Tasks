//package csen1002.main.task4;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @name Tasneem Nabil Elghobashy
 */

public class CfgEpsUnitElim {
	String cfg;

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	public CfgEpsUnitElim(String cfg) {
		// TODO Auto-generated constructor stub
		this.cfg = cfg;
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
	 * Eliminates Epsilon Rules from the grammar
	 */
	public void eliminateEpsilonRules() {
		// TODO Auto-generated method stub
		String input = this.cfg;
		String[] input_arr = input.split("#");
		String[] variables = input_arr[0].split(";");
		String[] terminals = input_arr[1].split(";");
		String[] rules = input_arr[2].split(";");	
		String ebs_rule_result = input_arr[0]+"#"+input_arr[1]+"#";

		//ArrayList of Rules to contain the rules <letter,arrayList<String>)
		ArrayList<Rule> rules_arrlist = new ArrayList<Rule>();
		for (String rule:rules) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split(",");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);
			Rule r = new Rule(lhs,rhs_arrlst);
			rules_arrlist.add(r);
	   }
	
		//Check if there is any e
		boolean e_exist = false;
		for(Rule r:rules_arrlist) {
			if (r.lexemes.contains("e")) {
				e_exist = true;
				break;
		}}
		
		//ArrayList for already done variables
		ArrayList<String> var_done = new ArrayList<String>();
		
		//Loop as long as e exists
		while(e_exist) {
			e_exist = false;
			for(Rule r:rules_arrlist) {
			    if (r.lexemes.contains("e") && !var_done.contains(r.letter)) {
					r.lexemes.remove("e");
					var_done.add(r.letter);
					for(Rule r2:rules_arrlist) {
						for (int k=0;k<r2.lexemes.size();k++) {
							String lex = r2.lexemes.get(k);
							if (lex.contains(r.letter)) {
									String new_lex = lex;
								    while(new_lex.contains(r.letter)){
								    	//vGSbSdS ==>vGbSdS,VGbSdS,VGbdS,vGSbdS
									     for(int i = 0;i<new_lex.length();i++){
									    	 if (new_lex.charAt(i) == r.letter.charAt(0)){
									    		 new_lex = new_lex.substring(0,i)+ new_lex.substring(i+1);		 
									    		 break;
									    	 }
									    	 if (!r2.lexemes.contains(new_lex))  r2.lexemes.add(new_lex); 
									     }
									     if (!r2.lexemes.contains(lex.replace(r.letter,"")) && lex.compareTo(r.letter)!=0)  r2.lexemes.add(lex.replace(r.letter,""));
								   }
								    new_lex = lex;
								    while(new_lex.contains(r.letter)){
								    	//vGSbSd ==>
									     for(int i =new_lex.length()-1;i>=0;i--){
									    	 if (new_lex.charAt(i) == r.letter.charAt(0)){
									    		 new_lex = new_lex.substring(0,i)+ new_lex.substring(i+1);		 
									    		 break;
									    	 }
									    	 if (!r2.lexemes.contains(new_lex))  r2.lexemes.add(new_lex); 
									}} 
						}}
						if(r2.lexemes.contains(r.letter) && !var_done.contains(r2.letter) && !r2.lexemes.contains("e")) r2.lexemes.add("e");
						Collections.sort(r2.lexemes);
					}
			}}
			//Check if there is any e
			for(Rule r:rules_arrlist) {if (r.lexemes.contains("e")) {e_exist = true; break;}}
		}//End of while(e_exist) loop
		
		//Epsilon Rule Elimination Output ==> ebs_rule_result
		for(int i = 0;i<rules_arrlist.size();i++) {
			Rule r = rules_arrlist.get(i);
			ebs_rule_result+=r.letter+"/";
			for(int j = 0;j<r.lexemes.size();j++) {
				ebs_rule_result+= r.lexemes.get(j);
				if(j!=r.lexemes.size()-1) ebs_rule_result+=",";
			}
			if(i!=rules_arrlist.size()-1) ebs_rule_result+=";";
		}
		
		this.cfg = ebs_rule_result;
		
	}

	/**
	 * Eliminates Unit Rules from the grammar
	 */
	public void eliminateUnitRules() {
		// TODO Auto-generated method stub
		String input2 = this.cfg;
		//Splitting the NFA input
		String[] input_arr2 = input2.split("#");
		String[] variables2 = input_arr2[0].split(";");
		String[] terminals2 = input_arr2[1].split(";");
		String[] rules2 = input_arr2[2].split(";");	
		String unit_rule_result = input_arr2[0]+"#"+input_arr2[1]+"#";

		//ArrayList of Rules to contain the rules <letter,arrayList<String>)
		ArrayList<Rule> rules_arrlist2 = new ArrayList<Rule>();
		for (String rule:rules2) {
			String lhs = rule.split("/")[0];
			String[] rhs = rule.split("/")[1].split(",");
			ArrayList<String> rhs_arrlst = new ArrayList<String>();
			for (String r: rhs) rhs_arrlst.add(r);
			Rule r = new Rule(lhs,rhs_arrlst);
			rules_arrlist2.add(r);
	   }
		
		//Remove A from RHS when A --> A
		for(Rule r:rules_arrlist2) {
			for(int i=0; i<r.lexemes.size(); i++) {
				String lex = r.lexemes.get(i);
				if(lex.length()==1 && lex.charAt(0)==r.letter.charAt(0)) {
					r.lexemes.remove(r.letter);
					}	
				}
			}
		
		//Check if unit exists
		boolean unit_exist = false;
		for(Rule r:rules_arrlist2) {
			for(String lex :r.lexemes) {
				if(lex.length()==1 && lex.charAt(0)>='A' && lex.charAt(0)<='Z') {unit_exist = true; break;}
			}
			if(unit_exist) break;
		}
		

		while(unit_exist) {
			unit_exist = false;		
			
			for(Rule r:rules_arrlist2) {
				for(int i=0; i<r.lexemes.size(); i++) {
					String lex = r.lexemes.get(i);
					if(lex.length()==1 && lex.charAt(0)>='A' && lex.charAt(0)<='Z') {
						r.lexemes.remove(lex);
						for (Rule rr:rules_arrlist2) {
							if (rr.letter.compareTo(lex)==0) {
								for (String lexx: rr.lexemes) {
									if(!r.lexemes.contains(lexx)) r.lexemes.add(lexx);
								}
							}
						}
					}
				}
				Collections.sort(r.lexemes);
			}
			
			for(Rule r:rules_arrlist2) {
				for(String lex :r.lexemes) {
					if(lex.length()==1 && lex.charAt(0)>='A' && lex.charAt(0)<='Z') {unit_exist = true;  break;}
			}if(unit_exist) break;}
		}

		
		//Unit Rule Elimination Output ==> ebs_rule_result
		for(int i = 0;i<rules_arrlist2.size();i++) {
					Rule r = rules_arrlist2.get(i);
					unit_rule_result+=r.letter+"/";
					for(int j = 0;j<r.lexemes.size();j++) {
						unit_rule_result+= r.lexemes.get(j);
						if(j!=r.lexemes.size()-1) unit_rule_result+=",";
					}
					if(i!=rules_arrlist2.size()-1) unit_rule_result+=";";
				}
		this.cfg=unit_rule_result;
	}

}

//Class Rule
class Rule{
	String letter;
	ArrayList<String> lexemes;
	
	public Rule (String letter,ArrayList<String> lexemes) {
		this.letter = letter;
		this.lexemes = lexemes;
	}
	
	public ArrayList<String> getNum1() {return this.lexemes;}
	public String getLetter(){return this.letter;}
	

}
