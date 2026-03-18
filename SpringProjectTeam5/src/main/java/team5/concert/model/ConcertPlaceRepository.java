package team5.concert.model;

import org.springframework.data.jpa.repository.JpaRepository;

import team5.concert.model.bean.ConcertPlace;

public interface ConcertPlaceRepository extends JpaRepository<ConcertPlace, Integer> {

}
