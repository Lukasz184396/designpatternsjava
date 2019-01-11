package udemy.com.solid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
    MY SUMMARY - CONCLUSIONS

    In the broken option class Research takes in constructor Relationships class
    the rule is broken because high-level modules should not depend on low-level modules.
    Both should depend on abstraction ( interface or abstract class).
    Abstractions should not depend on details.
    Details should depend on abstraction.

    The solution use RelationshipsBrowser interface (abstractions)
    which is passed as constructor param of Research class instead of Relationships class.
    And Relationships implements RelationshipsBrowser so we can pass it in place of RelationshipsBrowser.

    Now we can use classes which implements RelationshipsBrowser and pass ass constructor param of Research.

*/

public class DependencyInversionPrinciple {
    public static void main(String[] args) {
        Person parent = new Person("John");
        Person child1 = new Person("Chris");
        Person child2 = new Person("Matt");

        Relationships relationships = new Relationships();
        relationships.addParentAndChild(parent, child1);
        relationships.addParentAndChild(parent, child2);
         new Research(relationships);
    }

}

enum Relationship {
    PARENT,
    CHILD,
    SIBLING
}

class Person {

    public String name;

    public Person(String name) {
        this.name = name;
    }
}

class Relationships implements RelationshipsBrowser{  //low-level - related to data storage, just allow manipulation of the list

    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }

    public void addParentAndChild(Person parent, Person child) {
        relations.add(new Triplet<>(parent, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, parent));
    }

    @Override
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
                .filter( x -> Objects.equals(x.getFirst().name, name)
                        && x.getSecond() == Relationship.PARENT)
                .map(Triplet::getThird)
                .collect(Collectors.toList());
    }
}

class Research {    //high-level - does not care about data storage just care about actual research

    //constructor takes low level module as a dependency this broke DependencyInversionPrinciple
//    public Research(Relationships relationships) {
//        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
//        relations.stream()
//                .filter(p-> p.getFirst().name.equals("John") && p.getSecond() == Relationship.PARENT)
//                .forEach( ch -> System.out.println(
//                        "John has a child called " + ch.getThird().name
//                ));
//    }

    public Research( RelationshipsBrowser browser) {
        List<Person> children = browser.findAllChildrenOf("John");
        for(Person child : children) {
            System.out.println("John has a child called " + child.name);
        }

    }
}

//helper class not attached to jdk 8
class Triplet<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
}

//solution
interface RelationshipsBrowser {
    List<Person> findAllChildrenOf(String name);
}
