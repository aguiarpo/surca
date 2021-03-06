package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "medications_seq", sequenceName = "medications_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_created_by", columnList="createdBy"),
        @Index(name = "index_last_modified_by", columnList="lastModifiedBy")},
        uniqueConstraints = @UniqueConstraint(name = "name", columnNames=  "name" ))
public class Medications extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medications_seq")
    @Column(name = "id")
    private Long code;
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "medication")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<AnimalMedications> animal;
}
