package br.org.catolicasc.surca.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "animal_seq", sequenceName = "animal_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_name", columnList="name"),
                @Index(name = "index_species", columnList="species"),
                @Index(name = "index_birth_date", columnList="birthDate"),
                @Index(name = "index_breed", columnList="breed"),
                @Index(name = "index_date_microchip", columnList="dateMicrochip"),
                @Index(name = "index_created_by", columnList="createdBy"),
                @Index(name = "index_last_modified_by", columnList="lastModifiedBy")},
                uniqueConstraints = @UniqueConstraint(name = "microchip_number", columnNames=  "microchipNumber" ))
public class Animal extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_seq")
    @Column(name = "id")
    private Long code;
    @Size(max = 20)
    @NotEmpty
    private String microchipNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private String species;
    @NotNull
    private LocalDate birthDate;
    @NotEmpty
    private String coatColor;
    @NotEmpty
    private String breed;
    @NotNull
    @Enumerated(EnumType.STRING)
    private br.org.catolicasc.surca.model.Size size;
    @NotNull
    private LocalDate dateMicrochip;
    @Column(columnDefinition = "TEXT")
    private String comments;
    @NotEmpty
    private String genre;

    @NotNull
    private boolean castrated;

    @ManyToOne
    private Tutor tutor;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.VISIBLE;

    @ManyToOne
    private Vet vetMicrochip;

    @OneToMany(mappedBy = "animal")
    private List<AnimalMedications> medications;
}
