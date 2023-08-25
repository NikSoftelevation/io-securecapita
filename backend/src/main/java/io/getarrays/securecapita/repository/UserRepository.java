package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.entity.User;

import java.util.Collection;

public interface UserRepository<T extends User> {

    T create(T data);

    Collection<T> list(int page, int pageSize);

    T get(int id);

    T update(T data);

    Boolean delete(long id);

}
