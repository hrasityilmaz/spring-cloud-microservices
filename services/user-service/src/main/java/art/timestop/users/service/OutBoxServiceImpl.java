package art.timestop.users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import art.timestop.users.entities.OutboxEvent;
import art.timestop.users.repository.OutboxRepository;

@Service
public class OutBoxServiceImpl implements OutBoxService {

    private final OutboxRepository outboxRepository;

    public OutBoxServiceImpl(OutboxRepository outboxRepository) {
		this.outboxRepository = outboxRepository;
    }

    @Transactional
	@Override
	public void markAsPublished(Long eventId) {
        OutboxEvent event =
                outboxRepository.findById(eventId).orElseThrow();

        event.setStatus("PUBLISHED");
	}


}
