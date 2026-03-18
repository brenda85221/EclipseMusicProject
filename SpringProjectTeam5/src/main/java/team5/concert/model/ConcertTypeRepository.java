package team5.concert.model;

import org.springframework.data.jpa.repository.JpaRepository;

import team5.concert.model.bean.ConcertType;

public interface ConcertTypeRepository extends JpaRepository<ConcertType, Integer>{

}
