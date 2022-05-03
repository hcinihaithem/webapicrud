package com.bezkoder.spring.hibernate.manytomany.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.hibernate.manytomany.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findByTextContaining(String text);

    List<Entry> findEntrysByLabelsId(Long labelId);

    Optional<Entry> findByIdExt(Long idExt);
}