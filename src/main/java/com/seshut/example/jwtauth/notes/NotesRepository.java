package com.seshut.example.jwtauth.notes;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seshut.example.jwtauth.user.User;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer>{

	public List<Notes> findAllByUserOrderByImpactDesc(User user, Pageable pageable);
}
