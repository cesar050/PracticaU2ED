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
import com.practica.controller.dao.services.GeneradorServices;
import com.practica.controller.excepcion.ListEmptyException;
import com.practica.controller.tda.list.LinkedList;
import com.practica.eventos.TipoCrud;
import com.practica.models.Familia;
import com.practica.models.Generador;

@Path("/generador")
public class GeneradorApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllGenerador() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ecs = new EventoCrudServices();
        GeneradorServices gs = new GeneradorServices();
        try {
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", gs.listAll().toArray());
            ecs.registrarEvento(TipoCrud.LIST, "Generador listado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.LIST, "Error al listar generador.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getGeneradorById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        GeneradorServices gs = new GeneradorServices();
        EventoCrudServices ecs = new EventoCrudServices();
        try {

            res.put("estado", "success");
            res.put("data", gs.getGeneradorBy(id));
            ecs.registrarEvento(TipoCrud.READ, "Generador leído con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.READ, "Error al leer generador.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response saveGenerador(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        GeneradorServices gs = new GeneradorServices();
        EventoCrudServices ecs = new EventoCrudServices();
        try {
            // Validación de campos obligatorios
            if (map.get("precio") == null || map.get("consumoPorHora") == null || map.get("energiaGenerada") == null) {
                throw new IllegalArgumentException("Campos obligatorios no proporcionados.");
            }
            gs.getGenerador().setNombre(map.get("nombre").toString());
            gs.getGenerador().setPrecio(Float.parseFloat(map.get("precio").toString()));
            gs.getGenerador().setConsumoPorHora(Float.parseFloat(map.get("consumoPorHora").toString()));
            gs.getGenerador().setEnergiaGenerada(Float.parseFloat(map.get("energiaGenerada").toString()));
            gs.getGenerador().setUso(map.get("uso") != null ? map.get("uso").toString() : "No especificado");

            gs.save();
            res.put("estado", "success");
            res.put("data", "Generador guardado con exito.");
            ecs.registrarEvento(TipoCrud.CREATE, "Generador creado con exito.");
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("estado", "error");
            res.put("data", e.getMessage());
            ecs.registrarEvento(TipoCrud.CREATE, "Error al crear generador.");
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.CREATE, "Error al crear generador.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response deleteGenerador(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        GeneradorServices gs = new GeneradorServices();
        EventoCrudServices ecs = new EventoCrudServices();
        try {
            gs.getGenerador().setId(String.valueOf(id));
            gs.delete();
            res.put("estado", "success");
            res.put("data", "Generador eliminado con éxito.");
            ecs.registrarEvento(TipoCrud.DELETE, "Generador eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.DELETE, "Error al eliminar generador.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response updateGenerador(HashMap<String, Object> map, @PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        GeneradorServices gs = new GeneradorServices();
        EventoCrudServices ecs = new EventoCrudServices();
        try {
            // Validación de parámetros de entrada
            if (map == null || !map.containsKey("precio") || !map.containsKey("consumoPorHora")
                    || !map.containsKey("energiaGenerada")) {
                res.put("estado", "error");
                res.put("data", "Parámetros insuficientes.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

            // Asignación de valores
            gs.getGenerador().setId(String.valueOf(id));
            gs.getGenerador().setNombre(map.get("nombre").toString());
            gs.getGenerador().setPrecio(Float.parseFloat(map.get("precio").toString()));
            gs.getGenerador().setConsumoPorHora(Float.parseFloat(map.get("consumoPorHora").toString()));
            gs.getGenerador().setEnergiaGenerada(Float.parseFloat(map.get("energiaGenerada").toString()));
            gs.getGenerador().setUso(map.get("uso") != null ? map.get("uso").toString() : "No especificado");

            // Actualización del generador
            gs.update();
            res.put("estado", "success");
            res.put("data", "Generador actualizado con éxito.");
            ecs.registrarEvento(TipoCrud.UPDATE, "Generador actualizado con exito.");
            return Response.ok(res).build();
        } catch (NumberFormatException e) {
            res.put("estado", "error");
            res.put("data", "Error en el formato de los datos: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.UPDATE, "Error al actualizar generador.");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ecs.registrarEvento(TipoCrud.UPDATE, "Error al actualizar generador.");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}/{metodo}")
    public Response ordenarGenerador(@PathParam("attribute")String attribute,@PathParam("type")Integer type, @PathParam("metodo") String metodo) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        GeneradorServices gs = new GeneradorServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Generador> lista = gs.OrdenarPorMertodo(attribute, type, metodo);
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con exito.");
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
        GeneradorServices gs = new GeneradorServices();
        try {
            if (attribute == null || attribute.isEmpty() || value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }

            if (attribute.equals("nombre") && !value.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("El valor de busqueda debe ser una cadena de texto.");
            }

            if (attribute.equals("id") || attribute.equals("nombre")) {
                Object Generador = gs.buscarGeneradorPor(attribute, value);
                if (Generador == null) {
                    res.put("status", "ERROR");
                    res.put("msg", "No se encontró el Familia con " + attribute + ": " + value);
                    return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
                } else {
                    res.put("status", "OK");
                    res.put("msg", "Consulta exitosa.");
                    res.put("data", Generador);
                    return Response.ok(res).build();
                }
            } else {
                LinkedList<Generador> Generadores = gs.filtrarPor(attribute, value);
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", Generadores.toArray());
                if (Generadores.isEmpty()) {
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
