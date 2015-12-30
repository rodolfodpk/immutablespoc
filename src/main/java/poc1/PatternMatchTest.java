package poc1;

import javaslang.collection.Stream;
import javaslang.control.Match;
import org.myeslib.data.CommandId;

import java.io.PrintStream;
import java.util.Objects;
import java.util.function.Supplier;

import static java.lang.String.format;
import static poc1.CommandExample.*;

public class PatternMatchTest {

    public static void main(String[] a) {

        PrintStream out = System.out;

//        Stream.of(0, 1, 2, 3, 13, 14, null, -1)
//                .peek(n -> out.print(format("%d -> ", n)))
//                .map(Match.as(Object.class) // Match function (with apply(Object))
//                                .when(Objects::isNull).then("!")
//                                .whenIs(0).then("zero")
//                                .whenIsIn(1, 13, 14).then(i -> "first digit 1: " + i)
//                                .whenType(Double.class).then(d -> "Found a double: " + d)
//                                .otherwise(() -> "whats")
//                )
//                .forEach(out::println);

        CreateCustomerCmd command1 = CreateCustomerCmd.builder().commandId(CommandId.create()).number(1).build();

        DeliversTo deliversToCmd = DeliversToCmd.builder().commandId(CommandId.create()).string("a valid string").build();

        Stream.of(null, command1, deliversToCmd)
                .map(Match.as(Object.class) // Match function (with apply(Object))
                                .when(Objects::isNull).then("is null")
//                                .whenIs(command1).then("command1")
                                .whenType(CreateCustomerCmd.class).then((customerCmd) -> customerCmd.getCommandId())
                                .whenType(DeliversTo.class).then("DeliversTo command instance")
                                .otherwise(() -> "whats")
                )
                .forEach(out::println);
                ;


        out.flush(); // Avoid mixing sout and serr

// Match monad (with map(), flatMap(), get(), orElse(), orElseGet(), orElseThrow(), etc.)
        for (String s : Match.of(0)
                .whenType(Number.class).then(Object::toString)
                .otherwise("unknown")
                .map(String::toUpperCase)) {
            out.println(s);
        }

    }


}
