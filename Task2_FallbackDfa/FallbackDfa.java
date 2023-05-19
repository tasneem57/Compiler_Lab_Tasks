//package csen1002.main.task3;

import java.util.ArrayList;

/**
 * @name Tasneem Nabil Elghobashy
 */

public class FallbackDfa {
	String input;

	/**
	 * Constructs a Fallback DFA
	 * 
	 * @param fdfa A formatted string representation of the Fallback DFA. The string
	 *             representation follows the one in the task description
	 */
	public FallbackDfa(String fdfa) {
		this.input = fdfa;
	}

	/**
	 * @param input The string to simulate by the FDFA.
	 * 
	 * @return Returns a formatted string representation of the list of tokens. The
	 *         string representation follows the one in the task description
	 */
	public String run(String in) {
		// TODO Auto-generated method stub
		String input_string = in;
		
		//Splitting the NFA input
		String[] input_arr = this.input.split("#");
		String[]  NFA_states = input_arr[0].split(";");
		String[] NFA_letters = input_arr[1].split(";");
		String[] NFA_trans = input_arr[2].split(";");
		String NFA_start = input_arr[3];
		String[] NFA_final = input_arr[4].split(";");
		String result = "";
		
		//ArrayList of Integer to contain the accept states >> NFA_accept
		ArrayList<Integer> NFA_accept = new ArrayList<Integer>();
		for (String s:NFA_final) {
			int f = Integer.parseInt(s);
			NFA_accept.add(f);
		}
		
		//Declare arryList tran_objs_arr of transition objects
		ArrayList<Transition> tran_objs_arr = new ArrayList<Transition>();
		//ArrayList of NFA transitions as objects
		for (String t : NFA_trans) {
		String[] tran = t.split(",");
		Transition tran_obj = new Transition(Integer.parseInt(tran[0]),tran[1].charAt(0),Integer.parseInt(tran[2]));
		tran_objs_arr.add(tran_obj);
	    }		
		
		//to print the transitions
	    //for(Transition t:tran_objs_arr) {System.out.println(""+t.num1+t.letter+t.num2);}
		
		ArrayList<Integer> stack = new ArrayList<Integer>();
		int left = 0, right = 0;
		String input_copy = input_string;
		int input_len = input_copy.length();
		char letter;
		int peak;
		boolean found;
		while (input_len!=0){
			found = false;
			stack.clear();
			stack.add(Integer.parseInt(NFA_start));
			for (int i =0; i<input_len;i++) {
				letter = input_copy.charAt(i);
				for(Transition t:tran_objs_arr)	{
					if (t.num1 == stack.get(i) && t.letter==letter) {
						stack.add(t.num2);
						left++;
						break;
					}
				}
			}
			
			peak = stack.get(left); 
			for(int i = left;i>0;i--) {
				if (NFA_accept.contains(stack.get(i))) {
					left = i;
					found = true;
					break;
					//result_arr.add(""+input_copy+","+stack.get(i));
				}
			}
			if (found == true) {
				for (int i =right; i<left;i++) {
					result+=input_copy.charAt(i);
				}
				result+=","+stack.get(left);
			}else {
				for (int i =right; i<left;i++) {
					result+=input_copy.charAt(i);
				}
				result+=","+peak;
			}
			
				String input_copy2 = "";
				for (int i =left; i<input_len;i++) {
					input_copy2+=input_copy.charAt(i);
				}
				input_copy = input_copy2;	
				input_len = input_copy.length();
				right = 0;
				left = 0;
			
			
			if (input_len!=0) {
				result+=";";
			}
		}	
		return result;
	}
	
}
//Class Transition
class Transition implements Comparable<Transition>{
	char letter;
int num1;
	int num2;
	public int count;
	
	public Transition(int num1,char letter,int num2) {
		this.letter = letter;
		this.num1 = num1;
		this.num2 = num2;
	}
	
	public int getNum1() {return this.num1;}
	public char  getLetter(){return this.letter;}
	public int getNum2() {return this.num2;}
	
	@Override
	public int compareTo(Transition o) {
		// TODO Auto-generated method stub
		int res = Integer.compare(this.num1, o.num1);
		if (res == 0) {res = String.valueOf(this.letter).compareTo(String.valueOf(o.letter));}
		if (res == 0) {res = Integer.compare(this.num2, o.num2);}
		return res;
	}
}