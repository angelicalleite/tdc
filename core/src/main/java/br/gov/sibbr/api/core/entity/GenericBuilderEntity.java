package br.gov.sibbr.api.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provider create any entity of form generic
 * Ex: GenericBuilderEntity.of(User::new).with(User::setNome, "nome").with(User::setUsername, "username").build();
 */
public class GenericBuilderEntity<T> {

    private final Supplier<T> instantiate;

    private List<Consumer<T>> instanceModifiers = new ArrayList<>();

    public GenericBuilderEntity(Supplier<T> instantiate) {
        this.instantiate = instantiate;
    }

    public static <T> GenericBuilderEntity<T> of(Supplier<T> instantiate) {
        return new GenericBuilderEntity<>(instantiate);
    }

    public <U> GenericBuilderEntity<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instanceModifiers.add(c);

        return this;
    }

    public T build() {
        T value = instantiate.get();

        instanceModifiers.forEach(modifier -> modifier.accept(value));
        instanceModifiers.clear();

        return value;
    }
}
