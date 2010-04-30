import java.util.*;
import java.io.*;
import java.util.regex.*;

public class strMatch  {
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
		
		
		
	} //end main
	
	//Marcell driving
	public static boolean RK(String pattern, String text) {
		int hashValue = hash(pattern);
		
		return false;
	}
} //end strMatch
