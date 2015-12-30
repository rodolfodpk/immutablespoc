package poc1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.myeslib.data.Command;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CommandExample {

    @CommandStyle
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @JsonSubTypes.Type(value = CreateCustomer.class, name = "create"),
                    @JsonSubTypes.Type(value = DeliversTo.class, name = "delivers") })
    interface CustomerCommand extends Command {
    }

    @Value.Immutable
    @CommandStyle
    @JsonSerialize(as = CreateCustomerCmd.class)
    @JsonDeserialize(as = CreateCustomerCmd.class)
    interface CreateCustomer extends CustomerCommand {
        @Valid
        @NotNull
        int getNumber();
    }

    @Value.Immutable
    @CommandStyle
    @JsonSerialize(as = DeliversToCmd.class)
    @JsonDeserialize(as = DeliversToCmd.class)
    interface DeliversTo extends CustomerCommand {
        @Valid
        @NotNull
        @Size(min = 10, max = 14)
        String getString();
    }

}