import java.util.*;
import org.junit.* ;
import static org.junit.Assert.*;

public class strMatchTest {
        //David is driving
	@Test
	public void test1() {
	ArrayList<String> test = strMatch.parsePattern(new String("&abcde&   &waqa&   &afweaaq\nwera\nqera&zelda&poop&"));
	assertTrue((test.get(0)).equals(new String("abcde"))); 
        assertTrue((test.get(1)).equals(new String("waqa")));
        assertTrue((test.get(2)).equals(new String("afweaaq\nwera\nqera"))); 
        assertTrue((test.get(3)).equals(new String("poop"))); 
    	} //end test1
    	
} //end strMatchTest
