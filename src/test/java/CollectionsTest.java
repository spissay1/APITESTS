import org.testng.annotations.Test;

import java.util.*;

public class CollectionsTest {

    /*ArrayList - Contains Duplicates
                maintains Insertion order*/
    @Test
    public static void arrayListTest() {
        List<String> topCompanies = new ArrayList<String>();
        System.out.println(topCompanies.isEmpty());
        topCompanies.add("Google");
        topCompanies.add("a");
        topCompanies.add("b");
        topCompanies.add("c");

        for(int i=0;i<topCompanies.size();i++){
            System.out.println(topCompanies.get(i));
        }

        Iterator<String> companiesIterator = topCompanies.iterator();
        while (companiesIterator.hasNext()){
            System.out.println(companiesIterator.next());
        }
    }

    @Test
    public static void linkedlistTest(){
        List<String> humanSpecies = new LinkedList<String>();
        humanSpecies.add("abc");
        humanSpecies.add("def");
        humanSpecies.add("xyz");
        Iterator<String> humanIterator = humanSpecies.iterator();
        while (humanIterator.hasNext()){
            System.out.println(humanIterator.next());
        }
    }
}