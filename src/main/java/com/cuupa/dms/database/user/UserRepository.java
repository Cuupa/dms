package com.cuupa.dms.database.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<DbUser, Integer> {

    DbUser findByUsername(String username);

    DbUser findByUsernameAndPassword(String username, String password);

    DbUser save(DbUser user);

    boolean existsUserByUsername(String username);
}
