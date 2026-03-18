package team5.concert.model;

import org.springframework.data.jpa.repository.JpaRepository;

import team5.concert.model.bean.ConcertStatus;

public interface ConcertStatusRepository extends JpaRepository<ConcertStatus, Integer> {

}
