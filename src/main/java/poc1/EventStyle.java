package poc1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.immutables.value.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Value.Style(
        get = {"is*", "get*"}, // Detect 'get' and 'is' prefixes in accessor methods
        passAnnotations = {JsonTypeInfo.class, JsonSubTypes.class, NotNull.class, Min.class, Size.class},
     //   init = "set*", // Builder initialization methods will have 'set' prefix
     //   typeAbstract = {"Abstract*"}, // 'Abstract' prefix will be detected and trimmed
        typeImmutable = "*Evt", // No prefix or suffix for generated immutable type
     //   builder = "new", // construct builder using 'new' instead of factory method
     //   build = "create", // rename 'build' method on builder to 'create'
     //   visibility = Value.Style.ImplementationVisibility.PUBLIC, // Generated class will be always public
        defaults = @Value.Immutable(copy = false)) // Disable copy methods by default
public @interface EventStyle {}