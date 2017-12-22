package fr.gn.testdr4j;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.ReferenceType;
import fj.Show;
import fj.data.List;
import fr.gn.testdr4j.Person.Address;
import fr.gn.testdr4j.Person.Full;
import fr.gn.testdr4j.Person.Simple;

import java.io.IOException;

public final class Main {
  private Main() {}

  public static <T> void main(String[] args) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new TestModule());

    final Person<Simple> tata = Persons.SimplePers("Tata", 23);
    final Address adress = Addresses.Adress("rue Titi", "TitiVille");
    final Person<Full> tutu = Persons.FullPers("Tutu", 18, adress, List.nil());
    final Person<Full> toto = Persons.FullPers("Toto", 37, adress
        , List.list(tata, tutu));
        //, List.single(Things.Thing("toto")));

    final String strAddress = mapper.writeValueAsString(adress);
    System.out.println(strAddress);

    final String strTata = mapper.writeValueAsString(tata);
    System.out.println(strTata);

    final String strToto = mapper.writeValueAsString(toto);
    System.out.println(strToto);

    final Address addressFromJson = mapper.readValue(strAddress, Address.class);
    Addresses.addressShow().println(addressFromJson);
    final Person<T> totoFromJson = mapper.readValue(strToto, new TypeReference<Person<T>>(){});
    Persons.<T>personShow().println(totoFromJson);
  }

  static final class TestModule extends Module {
    @Override
    public String getModuleName() {
      return "TestModule";
    }

    @Override
    public Version version() { return Version.unknownVersion(); }

    @Override
    public void setupModule(SetupContext context) {
      context.addSerializers(new Serializers.Base() {
        @Override
        public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
          if (Address.class.isAssignableFrom(type.getRawClass())) return Addresses.addressStdSerializer();

          if (Person.class.isAssignableFrom(type.getRawClass())) return Persons.personStdSerializer();

          return super.findSerializer(config, type, beanDesc);
        }

        @Override
        public JsonSerializer<?> findReferenceSerializer(SerializationConfig config
          , ReferenceType type
          , BeanDescription beanDesc
          , TypeSerializer contentTypeSerializer
          , JsonSerializer<Object> contentValueSerializer) {


          return super.findReferenceSerializer(config, type, beanDesc, contentTypeSerializer, contentValueSerializer);
        }
      });

      context.addDeserializers(new Deserializers.Base() {
        @Override
        public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {

          if (Address.class.isAssignableFrom(type.getRawClass())) return Addresses.addressStdDeserializer();
          if (Person.class.isAssignableFrom(type.getRawClass())) return Persons.personStdDeserializer();

          return super.findBeanDeserializer(type, config, beanDesc);
        }
      });
    }
  }
}
