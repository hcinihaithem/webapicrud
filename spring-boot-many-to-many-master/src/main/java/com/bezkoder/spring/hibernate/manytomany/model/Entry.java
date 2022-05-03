package com.bezkoder.spring.hibernate.manytomany.model;

import com.bezkoder.spring.hibernate.manytomany.model.Entry;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "entrys")
public class Entry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "id_ext")
  private Long idExt;

  @Column(name = "text")
  private String text;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
  })
  @JoinTable(name = "assoc", joinColumns = { @JoinColumn(name = "entry_id") }, inverseJoinColumns = {
      @JoinColumn(name = "label_id") })
  private Set<Label> labels = new HashSet<>();

  public Entry() {
  }

  public Entry(String text, long idExt, long id) {
    this.text = text;
    this.idExt = idExt;
    this.id = id;

  }

  public Entry(String text2) {
    this.text = text2;
  }

  // getters and setters
  public void addLabel(Label label) {
    this.labels.add(label);
    label.getEntrys().add(this);
  }

  public void removeLabel(long LabelId) {
    Label label = this.labels.stream().filter(t -> t.getId() == LabelId).findFirst().orElse(null);
    if (label != null)
      this.labels.remove(label);
    label.getEntrys().remove(this);
  }

  public long getId() {
    return id;
  }

  public long getExtId() {
    return idExt;
  }

  public void setExtId(Long idExt) {
    this.idExt = idExt;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set<Label> getLabels() {
    return labels;
  }

  public void setLabels(Set<Label> label) {
    this.labels = label;
  }

  @Override
  public String toString() {
    return "entry [id=" + id + ", idext=" + idExt + ",  text=" + text + "  ]";
  }

}
