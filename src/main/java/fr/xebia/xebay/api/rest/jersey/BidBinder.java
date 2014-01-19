package fr.xebia.xebay.api.rest.jersey;

import fr.xebia.xebay.domain.Users;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class BidBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(Users.class).to(Users.class).in(Singleton.class);
    }
}
