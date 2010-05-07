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
        test = strMatch.parsePattern(new String("&abcde&   &waqa&   &afweaaq\nwera\nqera&zelda&poop&&"));
        assertEquals(4, test.size() );
        test = strMatch.parsePattern(new String("awepfkawopefkawpo&afpawfkaweopkfa"));
        assertEquals(0, test.size() );
    	} //end test1

        @Test
        public void test2() {
        int test = strMatch.moduArith(256, 3, 7);
        assertEquals(1, test);
        test = strMatch.moduArith(256, 5, 7);
        assertEquals(2, test);
        test = strMatch.moduArith(0, 7, 5);
        assertEquals(0, test);
        test = strMatch.moduArith(256, 0, 11);
        assertEquals(1, test);
        }
    	
    	@Test
    	public void test3() {
    	Queue<Character> q = new LinkedList<Character>() ;
    	assertEquals(65, strMatch.hash1(1, q, 'A'));
    	assertEquals(68, strMatch.hash1(1, q, 'D'));
    	assertEquals(66, strMatch.hash1(1, q, 'B'));
    	strMatch.hashValue = 0;
    	q = new LinkedList<Character>() ;
    	assertEquals(65, strMatch.hash1(2, q, 'A'));
    	assertEquals(16708 % strMatch.P, strMatch.hash1(2, q, 'D'));
    	assertEquals(17474 % strMatch.P, strMatch.hash1(2, q, 'B'));
    	}
    	
    	@Test
    	public void test4() {
    	 String singlePattern = new String("a");
    	 int[] test1 = strMatch.core(singlePattern);
    	 assertEquals(0, test1[0]);
    	 assertEquals(1, test1.length);
    	 String longPattern = new String("AABCAABD");
    	 int[] test2 = strMatch.core(longPattern);
    	 assertEquals(8, test2.length);
    	 assertEquals(0, test2[0]);
    	 assertEquals(0, test2[1]);
    	 assertEquals(1, test2[2]);
    	 assertEquals(0, test2[3]);
    	 assertEquals(0, test2[4]);
    	 assertEquals(1, test2[5]);
    	 assertEquals(2, test2[6]);
    	 assertEquals(3, test2[7]);
    	}
} //end strMatchTest
