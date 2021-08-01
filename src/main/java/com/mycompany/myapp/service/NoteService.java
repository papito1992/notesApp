package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Note;
import com.mycompany.myapp.repository.NoteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteService.class);

    @Value("${spring.application.dev-base-public-note-url}")
    private String publicUrlBase;

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        Note tempNote = noteRepository.save(note);
        tempNote.setLink(publicUrlBase + tempNote.getId());
        return noteRepository.save(tempNote);
    }

    /**
     * Partially update a note.
     *
     * @param note the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Note> partialUpdate(Note note) {
        log.debug("Request to partially update Note : {}", note);

        return noteRepository
            .findById(note.getId())
            .map(
                existingNote -> {
                    if (note.getContent() != null) {
                        existingNote.setContent(note.getContent());
                    }
                    if (note.getPassword() != null) {
                        existingNote.setPassword(note.getPassword());
                    }
                    if (note.getLink() != null) {
                        existingNote.setLink(note.getLink());
                    }
                    if (note.getExpirationDate() != null) {
                        existingNote.setExpirationDate(note.getExpirationDate());
                    }

                    return existingNote;
                }
            )
            .map(noteRepository::save);
    }

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        log.debug("Request to get all Notes");
        return noteRepository.findByUserIsCurrentUser();
    }

    /**
     * Get one note by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    /**
     * Delete the note by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
