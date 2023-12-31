package com.htc.jwtauthentication.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmailId(String emailId);

}
