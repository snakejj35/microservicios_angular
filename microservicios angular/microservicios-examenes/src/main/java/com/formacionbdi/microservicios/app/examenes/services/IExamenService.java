package com.formacionbdi.microservicios.app.examenes.services;

import java.util.List;

import com.formacionbdi.microservicios.common.examenes.models.entity.Asignatura;
import com.formacionbdi.microservicios.common.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.common.services.ICommonService;

public interface IExamenService extends ICommonService<Examen>{
	public List<Examen> findByNombre(String value);
	
	public Iterable<Asignatura> findAllAsignaturas();
	
	public Iterable<Long> findExamenesIdsWithRespuestasByPreguntasIds(Iterable<Long> preguntasIds);	
}
