package udemy.com.solid;

import java.util.ArrayList;
import java.util.List;

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

class Relationships {  //low-level - related to data storage, just allow manipulation of the list

    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }

    public void addParentAndChild(Person parent, Person child) {
        relations.add(new Triplet<>(parent, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, parent));
    }
}

class Research {    //high-level - does not care about data storage just care about actual research

    //constructor takes low level module as a dependency this broke DependencyInversionPrinciple
    public Research(Relationships relationships) {
        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
        relations.stream()
                .filter(p-> p.getFirst().name.equals("John") && p.getSecond() == Relationship.PARENT)
                .forEach( ch -> System.out.println(
                        "John has a child called " + ch.getThird().name
                ));
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
