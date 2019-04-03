package com.seshut.example.jwtauth.notes;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seshut.example.jwtauth.common.RequestHelper;
import com.seshut.example.jwtauth.common.WebCommons;
import com.seshut.example.jwtauth.exception.AppBusinessException;
import com.seshut.example.jwtauth.exception.ExceptionDetails;
import com.seshut.example.jwtauth.exception.InvalidTokenException;
import com.seshut.example.jwtauth.payload.notes.NotesRequest;
import com.seshut.example.jwtauth.payload.notes.NotesResponse;
import com.seshut.example.jwtauth.user.User;

@RestController
public class NotesController {

	@Autowired
	NotesService notesService;
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	RequestHelper requestHelper;
	
	@PostMapping(WebCommons.PATH_NOTES)
	public ResponseEntity<NotesResponse> createNotes(HttpServletRequest request, @Valid @RequestBody NotesRequest notesRequest)
	{
		User user = requestHelper.getUser(request);
		if(user == null)
		{
			throw new UsernameNotFoundException("Invalid Token, could not find user. Login is required");
		}
		Notes notes = modelMapper.map(notesRequest, Notes.class);
		Notes savedNotes = notesService.saveNotes(notes, user);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path(WebCommons.PATH_NOTES)
                .build().toUri();
		return ResponseEntity.created(location).body(modelMapper.map(savedNotes, NotesResponse.class));
	}
	
	@DeleteMapping(WebCommons.PATH_NOTES_ID)
	public ResponseEntity<Void> deleteNotes(HttpServletRequest request, @PathVariable int id)
	{
		User user = requestHelper.getUser(request);
		if(user == null)
		{
			throw new UsernameNotFoundException("Invalid Token, could not find user. Login is required");
		}
		
		notesService.deleteNotes(id, user);
		
		return ResponseEntity.noContent().build();
	}

	@GetMapping(WebCommons.PATH_NOTES)
	public ResponseEntity<List<NotesResponse>> getNotes(HttpServletRequest request, @RequestParam(required=true) @Min(1) int page)
	{
		User user = requestHelper.getUser(request);
		if(user == null)
		{
			throw new UsernameNotFoundException("Invalid Token, could not find user. Login is required");
		}
		
		List<Notes> notesList = notesService.getNotes(page, user);
		
		Type listType = new TypeToken<List<NotesResponse>>() {}.getType();
		return ResponseEntity.ok().body(modelMapper.map(notesList, listType));
	}
	
	@PutMapping(WebCommons.PATH_NOTES_ID)
	public ResponseEntity<NotesResponse> updateNotes(HttpServletRequest request, @PathVariable int id, @Valid @RequestBody NotesRequest notesRequest)
	{
		User user = requestHelper.getUser(request);
		if(user == null)
		{
			throw new UsernameNotFoundException("Invalid Token, could not find user. Login is required");
		}
		
		Notes notes = modelMapper.map(notesRequest, Notes.class);
		notes.setId(id);
		
		Notes savedNotes = notesService.updateNotes(notes, user);
		
		return ResponseEntity.ok().body(modelMapper.map(savedNotes, NotesResponse.class));
	}
	
	@ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ExceptionDetails> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDetails(e.getMessage(), null));
	}

	@ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ExceptionDetails> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDetails(e.getMessage(), null));
	}

	@ExceptionHandler({AppBusinessException.class})
    public ResponseEntity<ExceptionDetails> handleAppBusinessException(AppBusinessException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDetails(e.getMessage(), null));
	}
}
