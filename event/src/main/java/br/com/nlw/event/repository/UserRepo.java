package br.com.nlw.event.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.event.model.User;



public interface UserRepo extends CrudRepository<User, Integer> {
	public User findByEmail(String email);
}
