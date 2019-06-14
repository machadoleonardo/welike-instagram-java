package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {

    boolean existsByUserName(String username);
    List<Influencer> findAllByUserNameIn(List<String> usernames);

}
