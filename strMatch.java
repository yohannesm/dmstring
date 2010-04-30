import java.util.*;
import java.io.*;
import java.util.regex.*;

public class strMatch  {
        
	static int hashValue = 0;
        //Copied from our FSM project
        public static String read(String inFile) throws Exception {

	FileInputStream in = new FileInputStream(inFile);
	StringBuffer input = new StringBuffer();
	//DataInputStream dataIn = new DataInputStream(in);
        
	while(in.available() > 0){
	 input.append( (char)in.read());
	 }
   	 in.close();
   	 return input.toString();
	}//end read
	
	//David is driving
	public static ArrayList<String> parsePattern(String patterns) {
		Pattern pat = Pattern.compile("&([^&])*&");
		Matcher mat = pat.matcher(patterns);
		ArrayList<String> patternList = new ArrayList<String>();
		while (mat.find() ) {
			String pattern = mat.group();
			pattern = pattern.substring(1, pattern.length() - 1);
			patternList.add(pattern);
		}
		return patternList;
	}

	public static void main(String[] args) {
	 String test1 = new String("abcdefghi");
	 String p1 = new String("efk");
	 System.out.println(RK(p1, test1) );
		
		} //end main

	//Marcell driving
	//FIXME: need more sophisticated Hash function 
	public static int hash(int pLen, Queue<Character> q, char c){
	     if(q.size() == pLen){
	       Character c1 = q.remove();
	       int temp1 = c1.charValue();
	       hashValue -= temp1; 
	     }
	     int cInt =  c;
	     hashValue += cInt;
	     q.offer( new Character(c) );
	   return hashValue;
	}

	public static int hashPattern(String str){
	    Queue<Character> q = new LinkedList<Character>();
	    int result = 0;
            for(int i=0; i<str.length(); i++){
	       result = hash(str.length(), q, str.charAt(i) ); 
	    }
	    hashValue = 0;
	    return result;
	}

	//Marcell driving
	public static boolean RK(String pattern, String text) {
		int hashPat = hashPattern(pattern);
		
  	int m = pattern.length();
  	int n = text.length();
        Queue<Character> q1 = new LinkedList<Character>();
  	for(int i =0; i<n; i++){
  	int j = 0;
	hash(m, q1, text.charAt(i));
	       if(hashValue == hashPat){
     		for(j=0; j<m && pattern.charAt(j)==text.charAt(i-m+1+j); j++){
     		}
     		if (j==m) 
        	 return true;
     		}
		
  
	}
		return false;
	} //end RK

} //end strMatch
