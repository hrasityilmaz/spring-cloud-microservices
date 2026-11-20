package art.timestop.users.outbox;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import art.timestop.users.entities.OutboxEvent;
import art.timestop.users.repository.OutboxRepository;
import art.timestop.users.service.OutBoxService;

@Component
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final OutBoxService outBoxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(KafkaTemplate<String, String> kafkaTemplate, OutboxRepository outboxRepository, OutBoxService outBoxService) {
		this.outboxRepository = outboxRepository;
		this.outBoxService = outBoxService;
		this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled (fixedDelay = 1000) // TODO: @EnableScheduling needed !!!
    public void publishPendingEvents() {

        List<OutboxEvent> events =
            outboxRepository.findTop100ByStatusOrderByIdAsc("PENDING");

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                    "user.deleted",
                    event.getAggregateId(),
                    event.getPayload()
                ).get(10, TimeUnit.SECONDS);

                outBoxService.markAsPublished(event.getId());

                System.out.println(
                    "Event published: " + event.getId()
                );

            } catch (Exception e) {
                System.err.println(
                    "Event publishing failed: " + event.getId()
                );
            }
        }
    }
}
