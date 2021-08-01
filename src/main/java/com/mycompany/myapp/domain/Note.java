package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 5, max = 150)
    @Column(name = "content", length = 150, nullable = false)
    private String content;

    @NotNull
    @Size(min = 5, max = 20)
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "link")
    private String link;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Note id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Note content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return this.password;
    }

    public Note password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLink() {
        return this.link;
    }

    public Note link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ZonedDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public Note expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return this.user;
    }

    public Note user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", password='" + getPassword() + "'" +
            ", link='" + getLink() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
