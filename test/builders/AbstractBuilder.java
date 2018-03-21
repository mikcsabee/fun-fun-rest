package builders;

import io.ebean.Model;

abstract public class AbstractBuilder<T extends Model> {
    protected Long id;

    public AbstractBuilder<T> withId(Long id) {
        this.id = id;
        return this;
    }

    abstract public T build();

    public T inject() {
        T thing = build();
        thing.save();
        return thing;
    }
}
