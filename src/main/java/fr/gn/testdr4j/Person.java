package fr.gn.testdr4j;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdConverter;
import fj.Show;
import fj.data.Either;
import fj.data.List;
import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Instances;
import org.derive4j.hkt.TypeEq;
import org.derive4j.hkt.__;

import java.util.Map;

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

  @Data(@Derive(@Instances({Show.class, StdSerializer.class, StdDeserializer.class})))
  interface Thing<T> {
    interface Cases<R, T> {
      R Thing(T t);
    }
    <R> R match(Cases<R, T> cases);

    //public static Show<Thing<?>> show = Show.anyShow();
    static <T> Show<Person.Thing<T>> thingShow(Show<T> tShow) {
      return Things.thingShow(tShow);
    }

    static StdSerializer<Thing<?>> ser = (StdSerializer)
        Things.thingStdSerializer(new StdDelegatingSerializer(new StdConverter<Object, Object>() {
              @Override
              public Object convert(Object value) {
                return value;
              }
            }));

    static StdDeserializer<Thing<?>> deser = (StdDeserializer)
        Things.thingStdDeserializer(new UntypedObjectDeserializer(TypeFactory
            .defaultInstance()
            .constructRawCollectionType(java.util.List.class)
            , TypeFactory.defaultInstance().constructRawMapType(Map.class)));
  }

}
