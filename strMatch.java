import java.util.*;
import java.io.*;
import java.util.regex.*;


public class strMatch  {
        
  final static int BUF_SIZE = 2000;
	static int hashValue = 0;
        //Copied from our FSM project
        public static String readPattern(String inFile) throws Exception {

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
       
 

	public static void main(String[] args)  {
         try{  
	 String p1 = new String("beer");
	 System.out.println(RK(p1, "test1.txt") );
	 }
	 catch(Exception e){
	    System.err.println(e.getMessage() );
	 }	
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
	public static boolean RK(String pattern, String inFile) throws Exception{
		int hashPat = hashPattern(pattern);
	FileInputStream in = new FileInputStream(inFile);
	StringBuffer input = new StringBuffer();

	while(in.available()>0  && (input.length() < BUF_SIZE )){
	    char c = (char) in.read();
	    if( c == '\r'){
	     if (in.available() > 0) {
	      char c2 = (char) in.read();
	      if( c2 != '\n' ) input.append( '\n' );
	      c = c2 ;   
	    }
	    else c = '\n';
	    }
	    input.append( c );
	}

	String text = input.toString();
		
  	int m = pattern.length();
        Queue<Character> q1 = new LinkedList<Character>();
  	for(int i =0; i< text.length(); i++){
	if(in.available() >0 && (i==text.length()-1 )){
	     input.delete(0, BUF_SIZE - m );
	while(in.available() > 0 && ( input.length() < BUF_SIZE) ){
	    input.append( (char) in.read() );
	}
	i = i- (BUF_SIZE - m);
	text = input.toString();
	}
  	int j = 0;
	hash(m, q1, text.charAt(i));
	       if(hashValue == hashPat && (i-m+1>= 0)){
     		for(j=0; j<m && pattern.charAt(j)==text.charAt(i-m+1+j); j++){
     		}
     		if (j==m) {
		 in.close();
        	 return true;
		 }
     		}
		
  
	} 
	        in.close();
		return false;
	} //end RK

} //end strMatch
