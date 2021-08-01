package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Note;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("select note from Note note where note.user.login = ?#{principal.username}")
    List<Note> findByUserIsCurrentUser();
    //    Note findByNoteUserLoginOrderByDateDesc(String currentUserLogin);
}
