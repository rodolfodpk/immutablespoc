package poc1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.myeslib.data.Command;
import org.myeslib.data.CommandId;
import org.myeslib.stack2.utils.gson.polymorphic.RuntimeTypeAdapterFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static poc1.CommandExample.*;

public class AppTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void mustValidate() {

        DeliversTo deliversTo = DeliversToCmd.builder()
                .commandId(CommandId.create())
                .string("123")
                .build();

        Set<ConstraintViolation<CommandExample.DeliversTo>> constraintViolations =
                validator.validate( deliversTo );

        assertEquals( 1, constraintViolations.size() );

    }

    @Test
    public void testGson()  {

        CreateCustomer createCustomer = CreateCustomerCmd.builder()
                        .commandId(CommandId.create())
                        .number(22)
                        .build();

        DeliversTo deliversTo = DeliversToCmd.builder()
                .commandId(CommandId.create())
                .string("123")
                .build();

        RuntimeTypeAdapterFactory<Command> commandAdapter =
                RuntimeTypeAdapterFactory.of(Command.class)
                        .registerSubtype(DeliversToCmd.class, DeliversTo.class.getSimpleName())
                        .registerSubtype(CreateCustomerCmd.class, CreateCustomer.class.getSimpleName())
                        ;

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .registerTypeAdapterFactory(commandAdapter).create();

        String asJson1 = gson.toJson(createCustomer, Command.class);

        String asJson2 = gson.toJson(deliversTo, Command.class);

        System.out.println(asJson1);

        System.out.println(asJson2);

        Command command1 = gson.fromJson(asJson1, Command.class);

        assertTrue(command1 instanceof CreateCustomer);

        System.out.println(command1);

        Command command2 = gson.fromJson(asJson2, Command.class);

        assertTrue(command2 instanceof DeliversTo);

        System.out.println(command2);

    }

    @Test @Ignore
    public void testJackson() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        UUID uuid = UUID.randomUUID();

        String uSon = objectMapper.writeValueAsString(uuid);

        System.out.println(uSon);

        CommandId commandId = new CommandId(UUID.randomUUID());

        System.out.println(commandId.uuid());

        String asjSon = objectMapper.writerWithType(CommandId.class).writeValueAsString(commandId);

        System.out.println(asjSon);

        CreateCustomer createCustomer = CreateCustomerCmd.builder()
                .commandId(CommandId.create())
                .number(22)
                .build();

        DeliversTo deliversTo = DeliversToCmd.builder()
                .commandId(CommandId.create())
                .string("123")
                .build();


        String createCustomerAsJson = objectMapper.writerWithType(CreateCustomer.class).writeValueAsString(createCustomer);

        String deliversToAsJson = objectMapper.writerWithType(DeliversTo.class).writeValueAsString(deliversTo);

        System.out.println(createCustomerAsJson);

        System.out.println(deliversToAsJson);

        assertEquals(createCustomer, objectMapper.readValue(createCustomerAsJson, CommandExample.CreateCustomer.class));


    }

}
