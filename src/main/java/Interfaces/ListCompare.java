package Interfaces;




//import org.neo4j.cypher.internal.compiler.v2_1.functions.E;

import java.util.List;

@FunctionalInterface
public interface ListCompare {

    <R>R compare(List<?> listOne, List<?> listTwo);

}
