package com.bezkoder.spring.hibernate.manytomany.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.hibernate.manytomany.exception.ResourceNotFoundException;
import com.bezkoder.spring.hibernate.manytomany.model.Entry;
import com.bezkoder.spring.hibernate.manytomany.repository.EntryRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EntryController {
  @Autowired
  EntryRepository entryRepository;

  @GetMapping("/entrys")
  public ResponseEntity<List<Entry>> getAllEntrys(@RequestParam(required = false) String text) {

    List<Entry> entrys = new ArrayList<Entry>();

    if (text == null)
      entryRepository.findAll().forEach(entrys::add);
    else
      entryRepository.findByTextContaining(text).forEach(entrys::add);

    if (entrys.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(entrys, HttpStatus.OK);
  }

  @PostMapping("/entrys")
  public String createEntry(@RequestBody Entry entry) {
    entryRepository.saveAndFlush(entry);

    return "entry created !! ";
  }

  @GetMapping("/entrys/{id}")
  public ResponseEntity<Entry> getEntryById(@PathVariable("id") long id) {
    Entry entry = entryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Entry with id = " + id));
    return new ResponseEntity<>(entry, HttpStatus.OK);
  }

  @GetMapping("/entrysid/{id_ext}")
  public ResponseEntity<Entry> getEntryByExtId(@PathVariable("id_ext") long idExt) {
    Entry entry = entryRepository.findByIdExt(idExt)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Entry with id_ext = " + idExt));
    return new ResponseEntity<>(entry, HttpStatus.OK);
  }

  @PutMapping("/entrys/{id}")
  public String updateEntry(@PathVariable("id") long id, @RequestBody Entry entry) {
    Entry _entry = entryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Entry with id = " + id));
    _entry.setText(entry.getText());
    _entry.setExtId(entry.getExtId());
    _entry.setLabels(entry.getLabels());
    entryRepository.save(_entry);
    return "success : updated by id : " + id;
  }

  @PutMapping("/entrysid/{id_ext}")
  public String updateEntryIdExt(@PathVariable("id_ext") long idExt, @RequestBody Entry entry) {
    Entry _entry = entryRepository.findByIdExt(idExt)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Entry with idext = " + idExt));
    _entry.setText(entry.getText());
    _entry.setExtId(entry.getExtId());
    _entry.setLabels(entry.getLabels());
    entryRepository.save(_entry);
    return "success : updated by id_ext :  " + idExt;

  }

  @DeleteMapping("/entrys/{id}")
  public String deleteEntry(@PathVariable("id") long id) {
    entryRepository.deleteById(id);

    return "entry by id " + id + "is deleted";
  }

  @DeleteMapping("/entrysid/{id_ext}")

  public String deleteEntryExt(@PathVariable("id_ext") long idExt) {
    Entry entry = entryRepository.findByIdExt(idExt)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Entry with idext = "
            + idExt));
    entryRepository.deleteById(entry.getId());

    return "entry by id " + idExt + "is deleted";
  }

  @DeleteMapping("/entrys")
  public String deleteAllEntrys() {
    entryRepository.deleteAll();

    return "All entrys is deleted !!";
  }

}
