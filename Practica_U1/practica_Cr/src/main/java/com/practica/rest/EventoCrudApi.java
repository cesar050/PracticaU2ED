package com.practica.rest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.practica.controller.dao.services.EventoCrudServices;
import com.practica.controller.dao.services.FamiliaServices;
import com.practica.controller.excepcion.ListEmptyException;
import com.practica.controller.tda.list.LinkedList;
import com.practica.eventos.TipoCrud;
import com.practica.models.Familia;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Path("/evento")
public class EventoCrudApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllEventos() throws ListEmptyException, Exception {
        HashMap map = new HashMap<>();
        EventoCrudServices es = new EventoCrudServices();
        try {
            map.put("msg", "OK");
            map.put("data", es.getAllEventosCrud().toArray());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener la lista de eventos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/last/{limit}")
    public Response getLastEventos(@PathParam("limit") Integer limit) {
        HashMap map = new HashMap<>();
        EventoCrudServices es = new EventoCrudServices();
        try {
            map.put("msg", "OK");
            map.put("data", es.listLastEvent(limit).toArray());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener la lista de eventos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

      @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}/{metodo}")
    public Response ordenarFamilia(@PathParam("attribute")String attribute,@PathParam("type")Integer type, @PathParam("metodo") String metodo) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        //EventoCrudServices fs = new EventoCrudServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Familia> lista = ev.OrdenarPorMertodo(attribute, type, metodo);
            res.put("estado", "success");
            res.put("mensaje", "Consulta realizada con éxito.");
            res.put("data", lista.toArray());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}