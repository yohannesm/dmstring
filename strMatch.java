import java.util.*;
import java.io.*;
import java.util.regex.*;


public class strMatch  {
        
  final static int BUF_SIZE = 2000;
  final static int P = 1117;
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
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = RK(pattern, textFile);
             long end = System.currentTimeMillis();
             System.out.println("Time - RK: " + pattern + ": " + String.valueOf(end - begin));
             if (found)
               writer.println("RK MATCHED: " + pattern);
             else
               writer.println("RK FAILED: " + pattern);
           }
           for ( String pattern : patterns) {
             long begin = System.currentTimeMillis();
             boolean found = KMP(pattern, textFile);
             long end = System.currentTimeMillis();
             System.out.println("Time - KMP: " + pattern + ": " + 
String.valueOf(end - begin));
             if (found)
               writer.println("KMP MATCHED: " + pattern);
             else
               writer.println("KMP FAILED: " + pattern);
           }
           
	 
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

	//Marcell driving
	public static int hash1(int pLen, Queue<Character> q, char c){
	     if(q.size() == pLen){
	       Character c1 = q.remove();
	       int temp1 = c1.charValue();
	       temp1 = (moduArith(256, pLen - 1 , P) * temp1) % P;
	       hashValue = (hashValue - temp1) % P;
	       while (hashValue < 0) hashValue += P;
	    
	     }
	     hashValue = (hashValue * 256) % P;
	 
	     int cInt =  c;
	     hashValue = (hashValue + cInt) % P;
	     q.offer( new Character(c) );
	     assert hashValue>=0;
	   return hashValue;
	}

	public static int hashPattern(String str){
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
	if (pattern.length() == 0) return true;
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
	i = i- (BUF_SIZE / 2);
	text = input.toString();
	}
  	int j = 0;
	hash1(m, q1, text.charAt(i));

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
	
	
	//David driving
	public static boolean KMP(String pattern, String inFile) throws Exception{
	if (pattern.length() == 0) return true;
	  int[] fail = core(pattern);
	  FileInputStream in = new FileInputStream(inFile);
	  StringBuffer input = new StringBuffer();
	  
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
	  if (l > BUF_SIZE / 2) {
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
	l = l- (BUF_SIZE / 2);
	r = r - (BUF_SIZE / 2);
	text = input.toString();
	} //end if
	
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
	
	public static int[] core (String pattern) {
	  assert pattern.length() > 0;
	  int m = pattern.length();
	  int [] result = new int[m];
	  result[0] = 0;
	  if (m == 1) return result;
	  result[1] = 0;
	  pattern = new String(" " + pattern);
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
} //end strMatch
