/* Pair programming log: 

4/30, 1:00pm - 2:30pm, David and Marcell, 3 hours
5/6,  2:30pm - 4:30pm, David and Marcell, 4 hours
5/7, 10:00am -11:00am, David,             1 hour
5/7,  2:00pm - 5:00pm, David and Marcell, 6 hours
5/7,  5:00pm - 9:00pm, Marcell 4 hrs

Total time: 18 hours, 13 hours of pair programming
*/

import java.util.*;
import java.io.*;
import java.util.regex.*;


public class strMatch  {
        
  final static int BUF_SIZE = 512000; //Constant buffer size
  //12500077
  //17
  //1117 , 7919
  final static int P = 7919; //Constant prime number to mod by
	static int hashValue = 0;
	static int collisions = 0;
	static int comparisons = 0;
        /*Copied from our FSM project
        readPattern -- read in a file and return a string*/
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
	// ---------------------------------------------------------------
	
	//David is driving
	public static ArrayList<String> parsePattern(String patterns) {
	        //Use a regular expression to find every pattern between two ampersands
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
       
 
        //David driving
	public static void main(String[] args)  {
         try{
           String patternFile = args[0];
           String textFile = args[1];
           String outputFile = args[2];
           String pat = readPattern(patternFile);
           ArrayList<String> patterns = parsePattern( readPattern( patternFile) );
           FileOutputStream out = new FileOutputStream(outputFile);
           PrintStream writer = new PrintStream(out);
           writer.println(pat);
           writer.println(" ---------------------------------------");
        
	/*
           //Try the RK search with each pattern
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = RK(pattern, textFile);
             long end = System.currentTimeMillis();
             System.out.println("Time for RK: " + pattern + ": " + String.valueOf(end - begin));
             System.out.println("Comparisons for RK: : " + String.valueOf(comparisons));
	     System.out.println("Collisions for RK:  : " + String.valueOf(collisions));
	     if (found)
               writer.println("RK MATCHED: " + pattern);
             else
               writer.println("RK FAILED: " + pattern);
           }*/
           
           //Try the KMP search with each pattern
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = KMP(pattern, textFile);
             long end = System.currentTimeMillis();
             System.out.println("Time for KMP: " + pattern + ": " + 
String.valueOf(end - begin));
             System.out.println("Comparisons for KMP: " + ": " + String.valueOf(comparisons));
             if (found)
               writer.println("KMP MATCHED: " + pattern);
             else
               writer.println("KMP FAILED: " + pattern);
           }
	   /*
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = javaMatch(pattern, readPattern(textFile) );
             long end = System.currentTimeMillis();
             System.out.println("Time for Java built in: " + pattern + ": " + 
String.valueOf(end - begin));
             if (found)
               writer.println("Java MATCHED: " + pattern);
             else
               writer.println("Java FAILED: " + pattern);
	 }
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = naive(pattern, readPattern(textFile) );
             long end = System.currentTimeMillis();
             System.out.println("Time for Naive: " + pattern + ": " + 
String.valueOf(end - begin));
             if (found)
               writer.println("naive MATCHED: " + pattern);
             else
               writer.println("naive FAILED: " + pattern);
	        }*/
		}// end try

	 catch(Exception e){
	    System.err.println(e.getMessage() );
	 }	
	 
		} //end main

	//Marcell driving
	
	public static boolean javaMatch(String pattern, String textFile){
	        return textFile.contains(pattern);    
	}

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

	//Marcell driving
	public static int hash1(int pLen, Queue<Character> q, char c){
	 //Rolling hash function - sophisticated version
	     if(q.size() == pLen){
	       //Get the character we're removing
	       Character c1 = q.remove();
	       
	       //Get the ASCII value of the old character
	       int temp1 = c1.charValue();
	       
	       //Multiply by 256^(patternLen - 1) and mod by prime number P
	       temp1 = (moduArith(256, pLen - 1 , P) * temp1) % P;
	       
	       //Subtract the result from the previous hashValue and mod by P
	       hashValue = (hashValue - temp1) % P;
	       
	       //NOTE: When Java mods by a number, it does not automatically make it positive.
	       // we therefore have to use a loop to automatically make the number positive.
	       while (hashValue < 0) hashValue += P;
	    
	     }
	     
	     //Multiply the remaining numbers by 256 and mod by P
	     //hashValue = (hashValue * 256) % P;
	     hashValue *= 256;
	 
	     //Add the ASCII value of the new character
	     int cInt =  c;
	     //hashValue = (hashValue + cInt) % P;
	     hashValue += cInt;
	     hashValue %= P;
	     q.offer( new Character(c) );
	     assert hashValue>=0;
	   return hashValue;
	}

	public static int hashPattern(String str){
	    //Get the hash value of the passed in string
	    //This function is only for use with finding the pattern's hash value
            hashValue = 0;
	    Queue<Character> q = new LinkedList<Character>();
	    int result = 0;
            for(int i=0; i<str.length(); i++){
	       result = hash1(str.length(), q, str.charAt(i) ); 
	    }
	    hashValue = 0;
	    return result;
	}

	//Marcell driving
	public static boolean RK(String pattern, String inFile) throws Exception{
	collisions = 0;
	comparisons = 0;
	if (pattern.length() == 0) return true;
        int hashPat = hashPattern(pattern);
        
	FileInputStream in = new FileInputStream(inFile);
	StringBuffer input = new StringBuffer();

        //Read characters into the buffer, transforming carriage returns as necessary.
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
        
        //Iterate through the text
  	for(int i =0; i< text.length(); i++){
	comparisons++;
  	//Check to see if we need to fill the buffer 
	if(in.available() >0 && (i==text.length()-1 )){
	     input.delete(0, BUF_SIZE/2 );  
	
	//Fill the buffer
	while(in.available() > 0 && ( input.length() < BUF_SIZE) ){
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
	//Reposition i since the buffer has changed
	i = i- (BUF_SIZE / 2);
	text = input.toString();
	} //end if
  	int j = 0;
  	
  	//Do the rolling hash
	hash1(m, q1, text.charAt(i));

               //If the hashValue is a match, check to see if the string is a match
	       if(hashValue == hashPat && (i-m+1>= 0)){
     		for(j=0; j<m && pattern.charAt(j)==text.charAt(i-m+1+j); j++){comparisons++;
     		}
     		if (j==m) {

		 in.close();
        	 return true;
		 }
		 collisions++;
     		}
		
  
	} 
	        in.close();
		return false;
	} //end RK
	
	
	//David driving
	public static boolean KMP(String pattern, String inFile) throws Exception{
	comparisons = 0;
	if (pattern.length() == 0) return true;
	
	  //Get the lengths of the cores
	  int[] fail = core(pattern);
	  FileInputStream in = new FileInputStream(inFile);
	  StringBuffer input = new StringBuffer();
	  
	  //Read characters into the buffer
	  while(in.available() > 0 && ( input.length() < BUF_SIZE) ){
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
	int n = pattern.length();
	int l = 0;
	int r = 0;
	while( r < text.length() && r - l < n) {
	comparisons++;
	  //Check to see if the buffer needs to be refilled
	  if (l > BUF_SIZE / 2) {
	    //Refill the buffer
	    input.delete(0, BUF_SIZE/2 );
	    while(in.available() > 0 && ( input.length() < BUF_SIZE) ){
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
	//Update L and R since the buffer has been updated
	l = l- (BUF_SIZE / 2);
	r = r - (BUF_SIZE / 2);
	text = input.toString();
	} //end if
	
	// text[r] == pattern[r - l]
	if (text.charAt(r) == pattern.charAt(r - l) ) r++;
	else { // text[r] != pattern[r - l]
	  if (r == l) {r++; l++;}
	  else  //r > l
	     l = r - fail[r - l];
	}
	}//end while
	
	if (r - l == n) return true;
	return false;
	  
	}//end KMP
	
	//David is driving
	public static int[] core (String pattern) {
	  //The algorithm is taken straight from the notes, with minor modifications to make it work
	  assert pattern.length() > 0;
	  int m = pattern.length();
	  int [] result = new int[m];
	  result[0] = 0;
	  if (m == 1) return result; //Edge case: pattern length = 1
	  result[1] = 0;
	  pattern = new String(" " + pattern); //The algorithm assumed first index is 1, so pad the beginning
	  for(int j = 2; j < m; j++) {
	   int k = result[j - 1];
	   while (k > 0 && pattern.charAt(j) != pattern.charAt(k+1)) 
	     k = result[k];  
	   if ( k == 0 && pattern.charAt(j) != pattern.charAt(k + 1) )
	     result[j] = 0;
	   else
	     result[j] = k + 1;
	  }
	  return result;
	}

        // This function was copied from project 2 with minor modifications
        public static int moduArith(int M, int e, int n){
                if ( e == 0 ) return 1;                
		assert e > 0;
		assert n > 0;
		assert M >= 0;
		int c = 1, h = 0;
		//bitTest is a bitmask for bi
		int bitTest = 1 << 30;
		//shift all the way for the most significant bit of e
		while( (bitTest & e) == 0){
		  bitTest = bitTest >>> 1;
		}

		for(; bitTest!=0; bitTest = bitTest >>> 1){
   		if( (bitTest & e) ==0){
		      c = (c*c) % n;
		   }
		   else{
		      c = (((c*c) % n) * M ) % n;
		   }
}

return c;

}// end moduArith


  public static boolean naive(String p, String t){
  int m = p.length();
  int n = t.length();
   
  for(int i =0; i<=(n-m); i++){
  int j = 0;
     for(j=0; j<m && p.charAt(j)==t.charAt(i+j); j++){
     }
     if (j==m) 
         return true;
     }
  
  return false;
	 }//end naive
} //end strMatch
