package br.org.catolicasc.surca.model;

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
        @Index(name = "index_last_modified_by", columnList="lastModifiedBy")})
public class Medications extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medications_seq")
    private Long id;
    @Column(unique = true)
    @NotEmpty
    private String name;

    @ManyToMany
    @JoinTable(name="animal_medications", joinColumns=
            {@JoinColumn(name="medications_id")}, inverseJoinColumns=
            {@JoinColumn(name="animal_id")})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Animal> animals;

}
