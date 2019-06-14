package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    boolean existsByUserName(String username);
    List<Reference> findAllByUserNameIn(List<String> usernames);
}
