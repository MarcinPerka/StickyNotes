package com.archu.stickynotes.note;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Page<Note> getAllNotesByUserId(String userId, Pageable pageable) {
        return noteRepository.findByUserId(userId, pageable);
    }


    public Page<Note> getAllNotesByUserIdSearchByTitleOrNote(String userId, String searchInput, Pageable pageable) {
        List<Note> searchResults =  noteRepository.findByNoteContainingIgnoreCaseOrTitleContainingIgnoreCase(searchInput, searchInput);// from repository we get search results for all user ids
        List<Note> listResultsByUserId = searchResults.stream().filter(note -> userId.equals(note.getUser().getId())).collect(Collectors.toList());
        return new PageImpl<>(listResultsByUserId, pageable, listResultsByUserId.size());
    }

    public Optional<Note> getNoteByIdAndUserId(String userId, String id) {
        return noteRepository.findByIdAndUserId(id, userId);
    }

    public void createNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNoteByIdAndUserId(String userId, String id) {
        if (noteRepository.findByIdAndUserId(id, userId).isPresent())
            noteRepository.deleteByIdAndUserId(id, userId);
    }

    public void updateNote(Note note, String id) {
        Optional<Note> noteToUpdate = noteRepository.findById(id);
        if (noteToUpdate.isPresent()) {
            noteToUpdate.get().setTitle(note.getTitle());
            noteToUpdate.get().setNote(note.getNote());
            noteRepository.save(noteToUpdate.get());
        }
    }

    public void deleteNotes(String userId) {
        noteRepository.deleteByUserId(userId);
    }

}

