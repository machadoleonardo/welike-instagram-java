package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.Error;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<Error, Long> {

}
