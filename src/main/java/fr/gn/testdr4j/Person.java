package fr.gn.testdr4j;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fj.Show;
import fj.data.List;
import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Instances;
import org.derive4j.hkt.TypeEq;
import org.derive4j.hkt.__;

@Data (@Derive(@Instances({Show.class, StdSerializer.class, StdDeserializer.class})))
public interface Person<T> extends __<Person.µ, T> {
  enum µ {}

  interface Cases<R, T> {
    R SimplePers(String name, Integer age, TypeEq<Simple, T> eq);
    R FullPers(String name
      , Integer age
      , Address address
      , List<Person<?>> contacts
      , TypeEq<Full, T> eq);
  }

  <R> R match(Cases<R, T> cases);

  enum Simple {}
  enum Full {}

  @Data(@Derive(@Instances({Show.class, StdSerializer.class, StdDeserializer.class})))
  interface Address {
    interface Cases<R> {
      R Adress(String street, String city);
    }

    <R> R match(Cases<R> cases);
  }
}
