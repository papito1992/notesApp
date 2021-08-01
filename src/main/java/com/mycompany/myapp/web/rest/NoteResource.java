package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Note;
import com.mycompany.myapp.repository.NoteRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.InvalidPasswordException;
import com.mycompany.myapp.service.NoteService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.NoteExpirationDateException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Note}.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteService noteService;

    private final NoteRepository noteRepository;

    public NoteResource(NoteService noteService, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.noteRepository = noteRepository;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param note the note to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new note, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notes")
    public ResponseEntity<?> createNote(@Valid @RequestBody Note note) throws URISyntaxException {
        log.debug("REST request to save Note : {}", note);
        if (note.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (!note.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        Note result = noteService.save(note);
        return ResponseEntity
            .created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notes/:id} : Updates an existing note.
     *
     * @param id   the id of the note to save.
     * @param note the note to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated note,
     * or with status {@code 400 (Bad Request)} if the note is not valid,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notes/{id}")
    public ResponseEntity<?> updateNote(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Note note)
        throws URISyntaxException {
        log.debug("REST request to update Note : {}, {}", id, note);
        if (note.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, note.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (note.getUser() != null && !note.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }

        Note result = noteService.save(note);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, note.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notes/:id} : Partial updates given fields of an existing note, field will ignore if it is null
     *
     * @param id   the id of the note to save.
     * @param note the note to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated note,
     * or with status {@code 400 (Bad Request)} if the note is not valid,
     * or with status {@code 404 (Not Found)} if the note is not found,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<?> partialUpdateNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Note note
    ) throws URISyntaxException {
        log.debug("REST request to partial update Note partially : {}, {}", id, note);
        if (note.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, note.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (note.getUser() != null && !note.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        Optional<Note> result = noteService.partialUpdate(note);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, note.getId().toString())
        );
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        log.debug("REST request to get all Notes");
        return noteService.findAll();
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the note to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the note, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNote(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        Optional<Note> note = noteService.findOne(id);

        if (
            note.isPresent() &&
            note.get().getUser() != null &&
            !note.get().getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))
        ) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        return ResponseUtil.wrapOrNotFound(note);
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the note to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the note, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/public/note/{id}")
    public ResponseEntity<?> getPublicNote(@PathVariable Long id, @RequestHeader String password) {
        log.debug("REST request to get PUBLIC Note : {}", id);
        Optional<Note> note = noteService.findOne(id);
        if (note.isPresent()) {
            if (!note.get().getPassword().equals(password)) {
                throw new InvalidPasswordException();
            }
            if (ZonedDateTime.now().isAfter(note.get().getExpirationDate())) {
                throw new NoteExpirationDateException();
            }
            note.get().setUser(null);
            note.get().setPassword(null);
            note.get().setLink(null);
        }
        return ResponseUtil.wrapOrNotFound(note);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param id the id of the note to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        Optional<Note> note = noteRepository.findById(id);
        if (
            note.isPresent() &&
            note.get().getUser() != null &&
            !note.get().getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))
        ) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        noteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
