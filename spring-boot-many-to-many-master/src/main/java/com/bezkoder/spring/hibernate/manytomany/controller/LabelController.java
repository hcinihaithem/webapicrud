package com.bezkoder.spring.hibernate.manytomany.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.hibernate.manytomany.exception.ResourceNotFoundException;
import com.bezkoder.spring.hibernate.manytomany.model.Label;
import com.bezkoder.spring.hibernate.manytomany.model.Entry;
import com.bezkoder.spring.hibernate.manytomany.repository.LabelRepository;
import com.bezkoder.spring.hibernate.manytomany.repository.EntryRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class LabelController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private LabelRepository labelRepository;

    @GetMapping("/labels")
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = new ArrayList<Label>();

        labelRepository.findAll().forEach(labels::add);

        if (labels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(labels, HttpStatus.OK);
    }

    @GetMapping("/entrys/{entryId}/labels")
    public ResponseEntity<List<Label>> getAllLabelsByEntryId(@PathVariable(value = "entryId") Long entryId) {
        if (!entryRepository.existsById(entryId)) {
            throw new ResourceNotFoundException("Not found entry with id = " + entryId);
        }
        List<Label> labels = labelRepository.findLabelsByEntrysId(entryId);
        return new ResponseEntity<>(labels, HttpStatus.OK);
    }

    @GetMapping("/labels/{labelId}/entrys")
    public ResponseEntity<List<Entry>> getAllEntrysByLabelId(@PathVariable(value = "labelId") Long labelId) {
        if (!labelRepository.existsById(labelId)) {
            throw new ResourceNotFoundException("Not found Label with id = " + labelId);
        }
        List<Entry> entrys = entryRepository.findEntrysByLabelsId(labelId);
        return new ResponseEntity<>(entrys, HttpStatus.OK);
    }

    @GetMapping("/labels/{id}")
    public ResponseEntity<Label> getLabelsById(@PathVariable(value = "id") Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Label with id = " +
                        id));

        return new ResponseEntity<>(label, HttpStatus.OK);
    }

    @GetMapping("/labelsid/{id_ext}")
    public ResponseEntity<Label> getLabelByExtId(@PathVariable("id_ext") long idExt) {
        Label label = labelRepository.findByIdExt(idExt)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Label with id_ext = " + idExt));
        return new ResponseEntity<>(label, HttpStatus.OK);
    }

    @PostMapping("/entrys/{entryId}/labels")
    public String addLabel(@PathVariable(value = "entryId") Long entryId,
            @RequestBody Label labelRequest) {
        entryRepository.findById(entryId).map(entry -> {
            long labelId = labelRequest.getId();

            // label is existed
            if (labelId != 0L) {
                Label _label = labelRepository.findById(labelId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Label with id = " + labelId));
                entry.addLabel(_label);
                entryRepository.save(entry);
                return _label;
            }

            // add and create new Label
            entry.addLabel(labelRequest);
            return labelRepository.save(labelRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Entry with id = " + entryId));
        // return new ResponseEntity<>(label, HttpStatus.CREATED);
        return "label created !! ";
    }

    @PutMapping("/labels/{id}")
    public String updateLabel(@PathVariable("id") long id,
            @RequestBody Label label) {
        Label _label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabelId " + id +
                        "notfound"));

        _label.setName(label.getName());
        _label.setIdExt(label.getIdExt());
        _label.setEntrys(label.getEntrys());
        labelRepository.save(_label);
        return "success : update by id : " + id;
    }

    @PutMapping("/labelsid/{id_ext}")
    public String updateLabelIdExt(@PathVariable("id_ext") long idExt,
            @RequestBody Label label) {
        Label _label = labelRepository.findByIdExt(idExt)
                .orElseThrow(() -> new ResourceNotFoundException("LabelIdExt " + idExt +
                        "notfound"));

        _label.setName(label.getName());
        _label.setIdExt(label.getIdExt());
        _label.setEntrys(label.getEntrys());

        labelRepository.save(_label);
        return "success : update by idext : " + idExt;

    }

    @DeleteMapping("/entrys/{entryId}/labels/{labelId}")
    public String deleteLabelFromEntry(@PathVariable(value = "entryId") Long entryId,
            @PathVariable(value = "labelId") Long labelId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entry with id = "
                        + entryId));

        entry.removeLabel(labelId);
        entryRepository.save(entry);

        return "label is deleted by entryid : " + entryId;
    }

    @DeleteMapping("/entrysid/{entryIdext}/labelsid/{labelIdext}")
    public String deleteLabelFromEntryIdExt(@PathVariable(value = "entryIdext") Long entryIdExt,
            @PathVariable(value = "labelIdext") Long labelIdExt) {
        Entry entry = entryRepository.findByIdExt(entryIdExt)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entry with id = "
                        + entryIdExt));

        entry.removeLabel(labelIdExt);
        entryRepository.save(entry);

        return "label is deleted by idext : " + entryIdExt;
    }

    @DeleteMapping("/labels/{id}")
    public String deleteLabelid(@PathVariable("id") long id) {
        entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entry with id = "
                        + id));

        labelRepository.deleteById(id);

        return "success: label is deleted by id :  " + id;
    }

    @DeleteMapping("/labelsid/{id_ext}")

    public String deleteLabelExt(@PathVariable("id_ext") long idExt) {
        Label label = labelRepository.findByIdExt(idExt)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Label with id = "
                        + idExt));
        labelRepository.deleteById(label.getId());

        return "label is deleted by idext :  " + idExt;
    }

    @DeleteMapping("/labels")
    public String deleteAllLabels() {
        labelRepository.deleteAll();

        return "All labels is deleted";
    }
}
