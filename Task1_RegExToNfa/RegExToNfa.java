//package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @name Tasneem Nabil Elghobashy
 */

public class RegExToNfa {

	String inp ;
	String str ;
	String Letters;
	int counter = 0;
	int s=0,f=0,op2_inx,op1_inx;
	String g,op2,op1,tr;
	String ps1,pf1,ps2,pf2;
	String[] op_arr;
	String result = "";

	ArrayList<String> N=new ArrayList<>();
	ArrayList<Integer> States=new ArrayList<>();
	ArrayList<String> Trans=new ArrayList<>();
	
	/**
	 * Constructs an NFA corresponding to a regular expression based on Thompson's
	 * construction
	 * 
	 * @param input The alphabet and the regular expression in postfix notation for
	 *              which the NFA is to be constructed
	 */
	public RegExToNfa(String input) {
		// TODO Auto-generated constructor stub
		inp = input.toLowerCase().trim();
		str = inp.split("#")[1];
		Letters = inp.split("#")[0];
		for (int i = 0; i < str.length(); i++) {

			// Concatination
			if (str.charAt(i) == '.'){
				op2_inx = N.size()-1;
				op2 = N.get(op2_inx);
				op_arr = op2.split(",");
				ps2 = op_arr[0];
				pf2 = op_arr[op_arr.length-1];


				op1_inx = N.size()-2;
				op1 = N.get(op1_inx);
				op_arr = op1.split(",");
				ps1 = op_arr[0];
				pf1 = op_arr[op_arr.length-1];

				op2 = op2.replace(ps2,pf1);
				States.remove(new Integer(Integer.parseInt(ps2)));

				g = op1+ ";"+op2;

				N.remove(op2_inx);
				N.remove(op1_inx);
				N.add(g);

				for (int j = 0; j < Trans.size(); j++){
					tr = Trans.get(j);
					Pattern pattern = Pattern.compile("^"+ps2+",");
				    Pattern pattern2 = Pattern.compile(","+ps2+"$");
				    Matcher matcher = pattern.matcher(tr);
				    Matcher matcher2 = pattern2.matcher(tr);
				    boolean matchFound = matcher.find();
				    boolean matchFound2 = matcher2.find();
				    
				    if((matchFound || matchFound2) && tr.contains(ps2))  {
						Trans.set(j, tr.replace(ps2,pf1));
					}
				}

			// Union
			}else if (str.charAt(i) == '|'){
				s = counter++;
		    	f = counter++;
				op2_inx = N.size()-1;
				op2 = N.get(op2_inx);
				op_arr = op2.split(",");
				ps2 = op_arr[0];
				pf2 = op_arr[op_arr.length-1];


				op1_inx = N.size()-2;
				op1 = N.get(op1_inx);
				op_arr = op1.split(",");
				ps1 = op_arr[0];
				pf1 = op_arr[op_arr.length-1];

				g = s+",e,"+ps1+ ";"+s+",e,"+ps2   +";"+op1+ ";"+op2+";"+   pf1+",e,"+f+";" +pf2+",e,"+f;

				N.remove(op2_inx);
				N.remove(op1_inx);
				N.add(g);

				Trans.add(s+",e,"+ps1);
				Trans.add(s+",e,"+ps2);
				Trans.add(pf1+",e,"+f);
				Trans.add(pf2+",e,"+f);

				States.add(s);
				States.add(f);

			// Star
			}else if (str.charAt(i) == '*'){
				s = counter++;
		    	f = counter++;

				op2_inx = N.size()-1;
				op2 = N.get(op2_inx);
				op_arr = op2.split(",");
				ps2 = op_arr[0];
				pf2 = op_arr[op_arr.length-1];

				g = s+",e,"+ps2  +";"+op2+";"+  pf2+",e,"+f;

				N.remove(op2_inx);
				N.add(g);

				States.add(s);
				States.add(f);

				Trans.add(s+",e,"+ps2);
				Trans.add(pf2+",e,"+f);

				Trans.add(s+",e,"+f);
				Trans.add(pf2+",e,"+ps2);

			}else{
				s = counter++;
	     		f = counter++;
				g = s+","+str.charAt(i)+","+f;
				N.add(g);

				States.add(s);
				States.add(f);

				Trans.add(g);

			}
	    }
		
	    // ArrayList of Transition classes to sort transitions
	    ArrayList<Transition> Trans2 = new ArrayList<Transition>();

	    for (int j = 0; j < Trans.size(); j++){
			tr = Trans.get(j);
			op_arr = tr.split(",");
			Transition t = new Transition(Integer.parseInt(op_arr[0]),op_arr[1],Integer.parseInt(op_arr[2]));

			Trans2.add(t);
		}
	    Collections.sort(Trans2);
	    
	    for (int i = 0; i < States.size(); i++) {
			result += States.get(i);
			if (i != States.size()-1){
				result +=";";
			}
		}
		result += "#"+Letters+"#";
		
		int count = 0;
		for (Transition t:Trans2) {
			count++;
		    result+=t.num1+","+t.letter+","+t.num2;
		    if (count != Trans2.size()) {
		    	result+=";";
		    }
		}
		
		int ss = Integer.parseInt(N.get(0).split(",")[0]);
		if (s!=ss) s=ss; 
		result += "#"+s+"#"+f;
		  
	}

	/**
	 * @return Returns a formatted string representation of the NFA. The string
	 *         representation follows the one in the task description
	 */

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.result;
	}

}

class Transition implements Comparable<Transition>{
	String letter;
        int num1;
	int num2;
	
	public Transition(int num1,String letter,int num2) {
		this.letter = letter;
		this.num1 = num1;
		this.num2 = num2;
	}
	public int getNum1() {
		return this.num1;
	}
	
	public String  getLetter(){
		return this.letter;
	}
	
	public int getNum2() {
		return this.num2;
	}
	@Override
	public int compareTo(Transition o) {
		// TODO Auto-generated method stub
		int res = Integer.compare(this.num1, o.num1);
		if (res == 0) {
			res = this.letter.compareTo(o.letter);
		}
		if (res == 0) {
			res = Integer.compare(this.num2, o.num2);
		}
		return res;
	}

}
