package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {
}
