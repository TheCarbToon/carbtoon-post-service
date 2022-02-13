package kr.springboot.dcinside.cartoon.postservice.repository;


import kr.springboot.dcinside.cartoon.postservice.domain.Carbtoon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarbtoonRepository extends CrudRepository<Carbtoon, Long> {



}
