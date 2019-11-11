package com.mmnaseri.utils.spring.data.sample.models;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Auditable;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 5:23 PM)
 */
public class AuditableEntity implements Auditable<String, String, Instant> {

    private String id;
    private Optional<String> createdBy = Optional.empty();
    private Optional<String> lastModifiedBy = Optional.empty();
    private Optional<Instant> createdDate = Optional.empty();
    private Optional<Instant> lastModifiedDate = Optional.empty();

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = Optional.of(createdBy);
    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = Optional.of(lastModifiedBy);
    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = Optional.of(createdDate);
    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = Optional.of(lastModifiedDate);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
