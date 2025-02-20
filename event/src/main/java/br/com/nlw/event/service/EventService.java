package br.com.nlw.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.event.model.Event;
import br.com.nlw.event.repository.EventRepo;

@Service
public class EventService {
	
	@Autowired
	private EventRepo eventRepo;
	
	public Event addNewEvent(Event event) {
		event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
		return eventRepo.save(event);
	}
	
	public List<Event> getAllEvents(){
		return (List<Event>)eventRepo.findAll();
	}
	
	public Event getByprettyName(String prettyName) {
		return eventRepo.findByPrettyName(prettyName);

	}
	
}
