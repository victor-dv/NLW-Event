package br.com.nlw.event.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.nlw.event.model.Event;
import br.com.nlw.event.model.Subscription;
import br.com.nlw.event.model.User;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer>{

	public Subscription findByEventAndSubscriber(Event evt, User user);
}
