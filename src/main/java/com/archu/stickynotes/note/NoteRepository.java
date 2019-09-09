package com.archu.stickynotes.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, String> {

    Page<Note> findByUserId(String userId, Pageable pageable);

    void deleteByUserId(String userId);

    List<Note> findByNoteContainingIgnoreCaseOrTitleContainingIgnoreCase(String note, String title);
}

