package poc1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.myeslib.data.Command;
import org.myeslib.data.CommandId;
import org.myeslib.stack2.utils.gson.polymorphic.RuntimeTypeAdapterFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;

import static junit.framework.TestCase.assertTrue;
import static poc1.CommandExample.*;

public class JsonMicroBench {

    static RuntimeTypeAdapterFactory<Command> commandAdapter =
            RuntimeTypeAdapterFactory.of(Command.class)
                    .registerSubtype(DeliversToCmd.class, DeliversTo.class.getSimpleName())
                    .registerSubtype(CreateCustomerCmd.class, CreateCustomer.class.getSimpleName())
            ;

    static Gson gson = new GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .registerTypeAdapterFactory(commandAdapter).create();

    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(JsonMicroBench.class.getSimpleName())
                .forks(1)
                .operationsPerInvocation(5)
                .threads(100)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void jackson() throws IOException {

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

        CreateCustomer createCustomerFromJson = objectMapper.reader(CreateCustomer.class).readValue(createCustomerAsJson);

        DeliversTo deliversToFromJson = objectMapper.reader(DeliversTo.class).readValue(deliversToAsJson);

        assertTrue(createCustomerFromJson instanceof Command);

        assertTrue(deliversToFromJson instanceof Command);

    }

    @Benchmark
    public void gson() {

        CreateCustomer createCustomer = CreateCustomerCmd.builder()
                .commandId(CommandId.create())
                .number(22)
                .build();

        DeliversTo deliversTo = DeliversToCmd.builder()
                .commandId(CommandId.create())
                .string("123")
                .build();

        String asJson1 = gson.toJson(createCustomer, Command.class);

        String asJson2 = gson.toJson(deliversTo, Command.class);

        Command command1 = gson.fromJson(asJson1, Command.class);

        assertTrue(command1 instanceof CreateCustomer);

        Command command2 = gson.fromJson(asJson2, Command.class);

        assertTrue(command2 instanceof DeliversTo);

    }

}
