package fr.gn.testdr4j;

import fj.data.Either;
import org.derive4j.Data;
import org.derive4j.hkt.TypeEq;

@Data
public interface Weird<A, B> {
    interface Cases<R, A, B> {
        R Weird1(TypeEq<Integer, A> eqa, TypeEq<String, B> eqb);
        R Weird2(TypeEq<String, B> eq);
        R Weird3(String s);
    }

    <R> R match(Cases<R, A, B> cases);
}
