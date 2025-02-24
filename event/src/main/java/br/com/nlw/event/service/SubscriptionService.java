package br.com.nlw.event.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.event.dto.SubscriptionRankingByUser;
import br.com.nlw.event.dto.SubscriptionRankingItem;
import br.com.nlw.event.dto.SubscriptionResponse;
import br.com.nlw.event.exception.EventNotFoundException;
import br.com.nlw.event.exception.SubscriptionConflictException;
import br.com.nlw.event.exception.UserIndicadorNotFoundException;
import br.com.nlw.event.model.Event;
import br.com.nlw.event.model.Subscription;
import br.com.nlw.event.model.User;
import br.com.nlw.event.repository.EventRepo;
import br.com.nlw.event.repository.SubscriptionRepo;
import br.com.nlw.event.repository.UserRepo;

@Service
public class SubscriptionService {

	@Autowired
	private EventRepo evtRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private SubscriptionRepo subRepo;

	public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
		Event evt = evtRepo.findByPrettyName(eventName);
		if (evt == null) {
			throw new EventNotFoundException("Evento " + eventName + " não existe");
		}
		User userRec = userRepo.findByEmail(user.getEmail());
		if (userRec == null) {
			userRec = userRepo.save(user);
		}

		User indicador = null;
		if (userId != null) {
			indicador = userRepo.findById(userId).orElse(null);
			if (indicador == null) {
				throw new UserIndicadorNotFoundException("Usuário " + userId + " indicador não existe");
			}
		}
		Subscription subs = new Subscription();
		subs.setEvent(evt);
		subs.setSubscriber(userRec);
		subs.setIndication(indicador);

		Subscription tmpSub = subRepo.findByEventAndSubscriber(evt, userRec);
		if (tmpSub != null) {
			throw new SubscriptionConflictException(
					"Já existe incrição para o usuário " + userRec.getName() + " no evento " + evt.getTitle());
		}

		Subscription res = subRepo.save(subs);
		return new SubscriptionResponse(res.getSubscriptionNumber(), "http://codecraft.com/subscription/"
				+ res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());
	}
	
	  public List<SubscriptionRankingItem> getCompleteRanking(String prettyName){
	        Event evt = evtRepo.findByPrettyName(prettyName);
	        if (evt ==null){
	            throw new EventNotFoundException("Ranking do evento " + prettyName + " não existe");
	        }
	        return subRepo.generateRanking(evt.getEventdId());
	    }

	    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId){
	        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);
	        SubscriptionRankingItem item = ranking.stream().filter(i->i.userId().equals(userId)).findFirst().orElse(null);
	        if(item == null){
	            throw new UserIndicadorNotFoundException("Não há inscrições com indicação do usuário "+userId);
	        }
	        Integer posicao = IntStream.range(0, ranking.size())
	                          .filter(pos -> ranking.get(pos).userId().equals(userId))
	                          .findFirst().getAsInt();
	        return new SubscriptionRankingByUser(item, posicao+1);
	    }
}