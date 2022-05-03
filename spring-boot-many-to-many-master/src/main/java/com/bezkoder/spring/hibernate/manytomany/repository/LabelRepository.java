package com.bezkoder.spring.hibernate.manytomany.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.hibernate.manytomany.model.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findLabelById(Long entryId);

    List<Label> findLabelsByEntrysId(Long entrylId);

    Optional<Label> findByIdExt(Long idExt);
}
