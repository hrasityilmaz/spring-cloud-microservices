package art.timestop.users.service;

public interface OutBoxService {
    public void markAsPublished(Long eventId);
}
