package fr.xebia.xebay.api.rest.jersey;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.*;

@Documented
@HttpMethod("PATCH")
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PATCH {
}
