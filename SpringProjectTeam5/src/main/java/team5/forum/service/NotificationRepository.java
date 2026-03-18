package team5.forum.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team5.forum.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findAllByAcct(String acct, Sort sort);

    List<Notification> findByAcctAndIsRead(String acct, Integer isRead);
    List<Notification> findByAcctAndIsDelivered(String acct, Integer isDelivered);
}
