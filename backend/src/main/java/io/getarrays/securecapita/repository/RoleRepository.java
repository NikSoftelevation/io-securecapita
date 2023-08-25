package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.entity.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {

    T create(T data);

    Collection<T> list(int page, int pageSize);

    T get(int id);

    T update(T data);

    Boolean delete(int id);

    void addRoleToUser(int userId, String roleName);
}