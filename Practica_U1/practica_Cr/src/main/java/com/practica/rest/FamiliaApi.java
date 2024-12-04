package com.practica.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.practica.controller.dao.services.EventoCrudServices;
import com.practica.controller.dao.services.FamiliaServices;
import com.practica.controller.tda.list.LinkedList;
import com.practica.eventos.TipoCrud;
import com.practica.models.Familia;

@Path("/familia")
public class FamiliaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllFamilia() throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        FamiliaServices fs = new FamiliaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", fs.listAll().toArray());
            ev.registrarEvento(TipoCrud.LIST, "Consulta realizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/tiene_generador")
    public Response getFamiliasConGenerador() throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        FamiliaServices familiaService = new FamiliaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Familia> familias = familiaService.listTieneGerador();
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", familias.toArray());
            ev.registrarEvento(TipoCrud.LIST, "Consulta realizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getFamiliaByIndex(@PathParam("id") Integer id) throws Exception {
        FamiliaServices fs = new FamiliaServices();
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("estado", "success");
            res.put("data", fs.getFamiliaById(id));
            ev.registrarEvento(TipoCrud.READ, "Consulta realizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.READ, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(HashMap<String, Object> map) throws Exception {
        FamiliaServices familiaService = new FamiliaServices();
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            // Validación del nombre
            if (!map.containsKey("nombre") || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }

            // Validación del presupuesto
            float presupuesto = 0;
            if (map.containsKey("presupuesto") && !map.get("presupuesto").toString().isEmpty()) {
                presupuesto = Float.parseFloat(map.get("presupuesto").toString());
            }
            if (presupuesto <= 0 && Boolean.parseBoolean(map.get("tieneGenerador").toString())) {
                throw new IllegalArgumentException("No se puede obtener un generador sin un presupuesto válido.");
            }

            // Asignación de valores a la familia
            Familia familia = familiaService.getFamilia();
            familia.setNombre(map.get("nombre").toString());
            familia.setTieneGenerador(Boolean.parseBoolean(map.get("tieneGenerador").toString()));
            familia.setPresupuesto(Double.valueOf(presupuesto));
            familia.setCostoGenerador(Double.valueOf(map.get("costoGenerador").toString()));
            familia.setConsumoPorHora(Double.valueOf(map.get("consumoPorHora").toString()));
            familia.setEnergiaGenerada(Double.valueOf(map.get("energiaGenerada").toString()));
            familia.setUsoGenerador(map.get("usoGenerador").toString());

            familiaService.save();

            res.put("estado", "success");
            res.put("data", "Registro guardado con éxito.");
            ev.registrarEvento(TipoCrud.CREATE, "Registro guardado con exito.");
            return Response.ok(res).build();
        } catch (IllegalArgumentException e) {
            res.put("estado", "error");
            res.put("data", e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, "Error al guardar el registro: " + e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {
        FamiliaServices familiaService = new FamiliaServices();
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            Familia familia = familiaService.getFamilia();
            familia.setId(Integer.valueOf(id));
            familiaService.delete();
            res.put("estado", "success");
            res.put("data", "Registro eliminado con éxito.");
            ev.registrarEvento(TipoCrud.DELETE, "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.DELETE, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response update(HashMap<String, Object> map, @PathParam("id") Integer id) throws Exception {
        FamiliaServices familiaService = new FamiliaServices();
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            familiaService.getFamilia().setId(Integer.valueOf(id));
            familiaService.getFamilia().setNombre(map.get("nombre").toString());
            familiaService.getFamilia().setPresupuesto(Double.valueOf(map.get("presupuesto").toString()));
            familiaService.update();

            res.put("estado", "success");
            res.put("data", "Registro actualizado con éxito.");
            ev.registrarEvento(TipoCrud.UPDATE, "Registro actualizado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.UPDATE, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/disponible")
    public Response getDisponibles(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        FamiliaServices fs = new FamiliaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", fs.obtenerPosibles(id).toArray());
            ev.registrarEvento(TipoCrud.LIST, "Consulta realizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}/{metodo}")
    public Response ordenarFamilia(@PathParam("attribute")String attribute,@PathParam("type")Integer type, @PathParam("metodo") String metodo) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        FamiliaServices fs = new FamiliaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Familia> lista = fs.OrdenarPorMertodo(attribute, type, metodo);
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", lista.toArray());
            ev.registrarEvento(TipoCrud.LIST, "Consulta realizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/{attribute}/{value}")
    public Response buscar(@PathParam("attribute") String attribute, @PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        FamiliaServices ps = new FamiliaServices();
        try {
            if (attribute == null || attribute.isEmpty() || value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }
            if (attribute.equals("id") && !value.matches("[0-9]+")) {
                throw new IllegalArgumentException("El valor de busqueda debe ser un numero entero.");
            }
            if (attribute.equals("nombre") && !value.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("El valor de busqueda debe ser una cadena de texto.");
            }

            if (attribute.equals("id") || attribute.equals("nombre")) {
                Object Familia = ps.buscarFamiliaPor(attribute, value);
                if (Familia == null) {
                    res.put("status", "ERROR");
                    res.put("msg", "No se encontró el Familia con " + attribute + ": " + value);
                    return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
                } else {
                    res.put("status", "OK");
                    res.put("msg", "Consulta exitosa.");
                    res.put("data", Familia);
                    return Response.ok(res).build();
                }
            } else {
                LinkedList<Familia> Familias = ps.filtrarPor(attribute, value);
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", Familias.toArray());
                if (Familias.isEmpty()) {
                    res.put("data", new Object[] {});

                }
                return Response.ok(res).build();
            }
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error realizar la busqueda: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}