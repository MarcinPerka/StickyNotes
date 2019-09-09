package com.archu.stickynotes.note;

import com.archu.stickynotes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes/page/{page}")
    public String getAllNotes(@AuthenticationPrincipal User currentUser, Model model, @PathVariable int page,
                              @RequestParam(name = "searchInput", required = false) String searchInput) {
        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifiedAt").descending());
        Page<Note> notes;
        if (searchInput == null) {
            notes = noteService.getAllNotesByUserId(currentUser.getId(), pageable);
        } else {
            model.addAttribute("searchInput", searchInput);
            notes = noteService.getAllNotesByUserIdSearchByTitleOrNote(currentUser.getId(), searchInput, pageable);
            model.addAttribute("message", "We found " + notes.getTotalElements() + " results with provided phrase in browser.");
        }
        int totalPages = notes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("notes", notes.getContent());
        model.addAttribute("note", new Note());
        return "notes";
    }

    @GetMapping("/notes/page/{page}/{sortingItem}/{sortingDirection}")
    public String getAllSortedNotes(@AuthenticationPrincipal User currentUser, Model model, @PathVariable int page, @PathVariable String sortingItem, @PathVariable String sortingDirection, @RequestParam(name = "searchInput", required = false) String searchInput) {
        PageRequest pageable;
        if (sortingDirection.equals("desc")) {
            pageable = PageRequest.of(page - 1, 5, Sort.by(sortingItem).descending());
        } else {
            pageable = PageRequest.of(page - 1, 5, Sort.by(sortingItem).ascending());
        }
        Page<Note> notes;
        if (searchInput == null) {
            notes = noteService.getAllNotesByUserId(currentUser.getId(), pageable);
        } else {
            model.addAttribute("searchInput", searchInput);
            notes = noteService.getAllNotesByUserIdSearchByTitleOrNote(currentUser.getId(), searchInput, pageable);
            model.addAttribute("message", "We found " + notes.getTotalElements() + " results with provided phrase in browser.");
        }
        int totalPages = notes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSortingItem", sortingItem);
        model.addAttribute("currentSortingDirection", sortingDirection);
        model.addAttribute("notes", notes.getContent());
        model.addAttribute("note", new Note());
        return "notesSorted";
    }

    @GetMapping("/notes/create")
    public String showCreateNoteForm(Model model) {
        Note newNote = new Note();
        model.addAttribute("note", newNote);
        return "createNote";
    }

    @PostMapping("/notes/create")
    public String createNote(@AuthenticationPrincipal User currentUser, Note note, RedirectAttributes redirectAttributes) {
        note.setUser(currentUser);
        noteService.createNote(note);
        redirectAttributes.addFlashAttribute("message", "The note with title '" + note.getTitle() + "' has been created");
        return "redirect:/notes/page/1";
    }

    @GetMapping("/notes/{id}/update")
    public String showUpdateNoteForm(Model model, @PathVariable String id, @AuthenticationPrincipal User currentUser) {
        Optional<Note> optionalNote = noteService.getNoteByIdAndUserId(currentUser.getId(),id);
        optionalNote.ifPresent(note -> model.addAttribute("note", note));
        return "updateNote";
    }

    @PutMapping("/notes/{id}/update")
    public String updateNote(Note note, @PathVariable String id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        noteService.updateNote(note, id, currentUser.getId());
        redirectAttributes.addFlashAttribute("message", "The note with title '" + note.getTitle() + "' has been updated");
        return "redirect:/notes/page/1";
    }

    @DeleteMapping("notes/{id}/delete")
    public String deleteNote(@PathVariable String id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        noteService.deleteNoteByIdAndUserId(currentUser.getId(),id);
        redirectAttributes.addFlashAttribute("message", "The note has been deleted");
        return "redirect:/notes/page/1";
    }

    @DeleteMapping("/notes")
    public String deleteNotes(@AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        noteService.deleteNotes(currentUser.getId());
        redirectAttributes.addFlashAttribute("message", "All Notes has been deleted");
        return "redirect:/notes/page/1";
    }

}
