package fr.xebia.xebay.domain;

@FunctionalInterface
public interface Expirable {
    boolean isExpired();
}
