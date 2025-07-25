package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Role;
import yps.systems.ai.repository.IRoleRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
@RestController
@RequestMapping("/roleService")
public class RoleController {

    private final IRoleRepository roleRepository;

    @Autowired
    public RoleController(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/{elementId}")
    ResponseEntity<Role> getById(@PathVariable String elementId) {
        Optional<Role> roleOptional = roleRepository.findById(elementId);
        return roleOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{personElementId}")
    ResponseEntity<Role> getByPersonElementId(@PathVariable String personElementId) {
        Role role = roleRepository.getRoleByPersonElementId(personElementId);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    ResponseEntity<String> save(@RequestBody Role role) {
        Role roleSaved = roleRepository.save(role);
        return new ResponseEntity<>("Role saved with ID: " + roleSaved.getElementId(), HttpStatus.CREATED);
    }

    @PostMapping("/{roleElementId}/{personElementId}")
    ResponseEntity<String> saveRelationToRole(@PathVariable String roleElementId, @PathVariable String personElementId) {
        roleRepository.setRoleTo(personElementId, roleElementId);
        return new ResponseEntity<>("Role saved with ID: " + roleElementId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{elementId}")
    ResponseEntity<String> delete(@PathVariable String elementId) {
        Optional<Role> roleOptional = roleRepository.findById(elementId);
        if (roleOptional.isPresent()) {
            roleRepository.deleteRole(elementId);
            return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Role not founded", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{roleElementId}/{personElementId}")
    ResponseEntity<String> deleteRelation(@PathVariable String roleElementId, @PathVariable String personElementId) {
        Optional<Role> roleOptional = roleRepository.findById(roleElementId);
        if (roleOptional.isPresent()) {
            roleRepository.deleteRoleRelation(personElementId);
            return new ResponseEntity<>("Role relation deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Role relation not founded", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{elementId}")
    ResponseEntity<String> update(@PathVariable String elementId, @RequestBody Role role) {
        Optional<Role> roleOptional = roleRepository.findById(elementId);
        if (roleOptional.isPresent()) {
            role.setElementId(roleOptional.get().getElementId());
            roleRepository.save(role);
            return new ResponseEntity<>("Role updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Role not founded", HttpStatus.NOT_FOUND);
    }

}
