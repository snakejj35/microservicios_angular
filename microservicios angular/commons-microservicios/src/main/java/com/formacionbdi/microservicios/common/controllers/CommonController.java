package com.formacionbdi.microservicios.common.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.formacionbdi.microservicios.common.services.ICommonService;

//@CrossOrigin({"http://localhost:4200"}) //Intercambio de Recursos de Origen cruzado entre Angular y Spring Java Application mediante el puerto de ejecucion  de Angulap app y aplicacion externa mediante API Rest
public class CommonController<E, S extends ICommonService<E>>{
	
	@Autowired
	protected S service;
	
	@GetMapping   //Permite mapear una ruta url al metodo. 
	public ResponseEntity<?> listar() {   //RessponseEntity: Es un objeto que permite construir la respuesta, el http entity. Pasa objetos al cuerpo
		return ResponseEntity.ok().body(service.findAll()); // retorna un http status 
	}
	
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable) {  //Listar paginable
		return ResponseEntity.ok().body(service.findAll(pageable));  
	}
	
	@GetMapping("/{id}")    //Es un PathVariable osea una ruta variable url
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<E> opt = service.finById(id);
		if(!opt.isPresent()) {
			return ResponseEntity.notFound().build();  //Construye la respuesta para un notfound 404
		}
		return ResponseEntity.ok(opt.get());	//Http status 200	
	}
	
	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody E entity, BindingResult result) { //Con ResponseBody le indicamos que el objeto que traiga el requestBody los pueble en el objeto Alumno
		if(result.hasErrors()) {
			return this.validar(result);
		}
		E entityDB = service.save(entity);                         //Valid valida el error y binding envia mensaje de error
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDB); //Http status 201
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build(); //Http status 204
	}
	
	protected ResponseEntity<?> validar(BindingResult result) {
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());		
		});
		
		return ResponseEntity.badRequest().body(errores);
	}
}
