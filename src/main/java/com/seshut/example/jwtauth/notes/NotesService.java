package com.seshut.example.jwtauth.notes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seshut.example.jwtauth.exception.AppBusinessException;
import com.seshut.example.jwtauth.user.User;

@Service
public class NotesService {

	@Autowired
	private NotesRepository notesRepository;
	
	
	public Notes saveNotes(Notes notes, User user)
	{
		notes.setUser(user);
		notes.setCreatedTime(System.currentTimeMillis());
		Notes savedNotes = notesRepository.save(notes);
		
		return savedNotes;
	}
	
	public void deleteNotes(int notesId, User user)
	{
		Optional<Notes> optional = notesRepository.findById(notesId);
		if(optional.isPresent())
		{
			Notes notes = optional.get();
			if(notes.getUser().getId() == user.getId())
			{
				notesRepository.delete(notes);
			}
			else
			{
				throw new AppBusinessException(String.format("Invalid Request. Notes id (%d) is not matching for this user (%d)", notes.getId(), user.getId()));
			}
		}
		else
		{
			throw new AppBusinessException(String.format("Invalid Request. Notes not found by id (%d)", notesId));
		}
	}

	public List<Notes> getNotes(int pageNum, User user) 
	{
		Pageable pageable = PageRequest.of(pageNum - 1 , 10);
		List<Notes> notes = notesRepository.findAllByUserOrderByImpactDesc(user, pageable);
		return notes;
	}
	
	public Notes updateNotes(Notes notes, User user)
	{
		Notes savedNotes = null;
		
		Optional<Notes> optional = notesRepository.findById(notes.getId());
		if(optional.isPresent())
		{
			Notes existingNotes = optional.get();
			if(existingNotes.getUser().getId() == user.getId())
			{
				existingNotes.setContent(notes.getContent());
				existingNotes.setImpact(notes.getImpact());
				existingNotes.setColorCode(notes.getColorCode());
				savedNotes = notesRepository.save(existingNotes);
			}
			else
			{
				throw new AppBusinessException(String.format("Invalid Request. Notes id (%d) is not matching for this user (%d)", notes.getId(), user.getId()));
			}
		}
		else
		{
			throw new AppBusinessException(String.format("Invalid Request. Notes not found by id (%d)", notes.getId()));
		}
		
		return savedNotes;
	}
}
