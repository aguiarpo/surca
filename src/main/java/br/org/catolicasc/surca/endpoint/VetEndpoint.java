package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.email.EmailMessage;
import br.org.catolicasc.surca.email.Mailer;
import br.org.catolicasc.surca.model.Animal;
import br.org.catolicasc.surca.model.LevelsOfAccess;
import br.org.catolicasc.surca.model.Vet;
import br.org.catolicasc.surca.repository.AnimalRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.org.catolicasc.surca.endpoint.UserEndpoint.getString;

@RestController
@RequestMapping("v1")
public class VetEndpoint {

    private VetRepository vetDao;
    private AnimalRepository animalDao;
    private Mailer mailer;

    @Autowired
    public VetEndpoint(VetRepository vetDao, AnimalRepository animalDao, Mailer mailer) {
        this.vetDao = vetDao;
        this.animalDao = animalDao;
        this.mailer = mailer;
    }

    @GetMapping(path = "/admin/veterinario")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(vetDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/{id}")
    public ResponseEntity<?> getVeterinarioById(@PathVariable("id") Long id){
        Optional<Vet> vet =  vetDao.findById(id);
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }


    @GetMapping(path = "/admin/veterinario/nome/{nome}")
    public ResponseEntity<?> getVeterinarioName(@PathVariable("nome") String name, Pageable pageable){
        Page<Vet> vets = vetDao.findByUserName(pageable, name);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario//crmv/{crmv}")
    public ResponseEntity<?> getVeterinarioCrmv(@PathVariable("crmv") String crmv, Pageable pageable){
        Page<Vet> vets = vetDao.findByCrmv(pageable, crmv);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/nome/like/{nome}")
    public ResponseEntity<?> getVeterinarioNameLike(@PathVariable("nome") String name, Pageable pageable){
        Page<Vet> vets = vetDao.findByUserNameStartingWith(pageable, name);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/email/{email}")
    public ResponseEntity<?> getVeterinarioEmail(@PathVariable("email") String email, Pageable pageable){
        Page<Vet> vets = vetDao.findByUserEmail(pageable, email);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/cidade/{city}")
    public ResponseEntity<?> getVetByCityLike(@PathVariable("city") String city, Pageable pageable){
        Page<Vet> vets =  vetDao.findByUserCityStartingWith(pageable, city);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/estado/{state}")
    public ResponseEntity<?> getVetByState(@PathVariable("state") String state, Pageable pageable){
        Page<Vet> vets =  vetDao.findByUserState(pageable, state);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/criadoPor/{createdBy}")
    public ResponseEntity<?> getCreatedBy(@PathVariable("createdBy")String name, Pageable pageable){
        Page<Vet> vets =  vetDao.findByUserCreatedByStartingWith(pageable, name);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/veterinario/modificadoPor/{lastModifiedBy}")
    public ResponseEntity<?> getLastModifiedBy(@PathVariable("lastModifiedBy")String name, Pageable pageable){
        Page<Vet> vets =  vetDao.findByUserLastModifiedByStartingWith(pageable, name);
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @PostMapping(path = "/admin/veterinario")
    public ResponseEntity<?> save(@RequestBody Vet vet){
        vet.getUser().setLevelsOfAccess(LevelsOfAccess.VETERINARIO);
        String password = generatePassword();
        vet.getUser().setPassword(password);
        vet.getUser().setBcryptPassword();
        Vet vetSave = vetDao.save(vet);
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add("Eduardo Poerner <eduardo.poerner@catolicasc.org.br>");
        sendEmail(recipients, password);
        return new ResponseEntity<>(vetSave, HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/veterinario/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        List<Animal> vets = animalDao.findByVetMicrochipIdOrCastratorId(id, id);
        animalDao.deleteAll(vets);
        vetDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/veterinario")
    public ResponseEntity<?> update(@RequestBody Vet vet){
        vet.getUser().setLevelsOfAccess(LevelsOfAccess.VETERINARIO);
        vet.getUser().setBcryptPassword();
        return new ResponseEntity<>(vetDao.save(vet), HttpStatus.OK);
    }

    private String generatePassword(){
        return getString();
    }

    private void sendEmail(ArrayList<String> recipients,String body){
        try{
            mailer.submit(new EmailMessage("Eduardo Aguiar <emailtestesurca@gmail.com>",
                    recipients, "Senha", "Senha -> " + body));
        }catch (MailException ignored){

        }
    }
}