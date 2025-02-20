package br.com.nlw.event.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.event.model.Event;

public interface EventRepo extends CrudRepository<Event, Integer>{

	public Event findByPrettyName(String prettyName);
}
