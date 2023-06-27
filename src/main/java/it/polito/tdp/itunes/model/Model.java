package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private Graph<Album,DefaultEdge> grafo;
	private Map<Integer, Album> idMap;
	private ItunesDAO dao;
	private Set<Album> compConnessa;
	//per la ricorsione
	private int dimMax;
	private double durataMax;
	private Set<Album> setMassimo;
	private double durata;
	
	
	public void creaGrafo(double durata) {
		this.compConnessa=null;
		this.dao=new ItunesDAO();
		this.idMap=new HashMap<Integer, Album>();
		this.grafo=new SimpleGraph<Album, DefaultEdge>(DefaultEdge.class);
		List<Album> vertici=this.dao.getAlbumsWithDuration(durata);
		for(Album b: vertici) {
			this.idMap.put(b.getAlbumId(), b);
		}
		Graphs.addAllVertices(this.grafo, vertici);
		List<Pair<Integer,Integer>> archi=this.dao.getCompatibleAlbums();
		for (Pair<Integer,Integer> p: archi) {
			Album a1=this.idMap.get(p.getFirst());
			Album a2=this.idMap.get(p.getSecond());
			if(a1!=null && a2!=null) {
				this.grafo.addEdge(a1,a2);
			}
		}
		System.out.println("Vertici: "+this.grafo.vertexSet().size()) ;
		System.out.println("Archi:   "+this.grafo.edgeSet().size()) ;
		
	}
	
	public List<Album> getAlbums(){
		List<Album> list=new ArrayList<Album>(this.grafo.vertexSet());
		
		Collections.sort(list);
		return list;
		
	}
	
	public Set<Album> getComponente (Album al){
		ConnectivityInspector<Album, DefaultEdge> inspector=new ConnectivityInspector<>(this.grafo);
		Set<Album> componenteConnessa=inspector.connectedSetOf(al);
		this.compConnessa=componenteConnessa;
		return this.compConnessa;	
	}
	
	public double sommaDurate() {
		if(this.compConnessa.size()==0 || this.compConnessa==null) {
			return 0;
		}
		double durata=0;
		for(Album a: this.compConnessa) {
			durata+=a.getDurata();
		}
		return durata;
	}
	
	public Set<Album> ricercaSetMassimo(Album a1,double dTot){
		this.durataMax=dTot;
		this.setMassimo=new HashSet<>();
		this.dimMax=0;
		Set<Album > parziale =new HashSet<>();
		Set<Album> rimanenti= new HashSet<>(this.getComponente(a1));
		parziale.add(a1);
		rimanenti.remove(a1);
		
		cerca(parziale,a1.getDurata(),rimanenti);
		return this.setMassimo;
		
		
	}
	private void cerca(Set<Album> parziale, double durataParziale,Set<Album> rimanenti) {
		if(parziale.size()>this.dimMax) {
			this.setMassimo=new HashSet<>(parziale);
			this.dimMax=parziale.size();
			this.durata=durataParziale;
			if(durataParziale==this.durataMax) {
				return;
			}
		}
		Set<Album> rimanentiAggiornato=new HashSet<>(rimanenti);
		
		for( Album a: rimanenti) {
			if(durataParziale+a.getDurata()<this.durataMax) {
				parziale.add(a);
				rimanentiAggiornato.remove(a);
				cerca(parziale,durataParziale+a.getDurata(), rimanentiAggiornato);
				rimanentiAggiornato.add(a);
				parziale.remove(a);	
			}
		}
		
		
		
	
	
	
	
	
	
	
//	public Set<Album> ricercaSetMassimo(Album a1,double dTot){
//		if (a1.getDurata()>dTot) {
//			return null;
//		}
//		List<Album> parziale=new ArrayList<>();
//		List<Album> tutti=new ArrayList<>(getComponente(a1));
//		dimMax=0;
//		setMassimo=null;
//		//parziale.add(a1);
//		tutti.remove(a1);
//		cerca(parziale,0,dTot-a1.getDurata(),tutti,0.0);
//		Set<Album> result =new HashSet<>(this.setMassimo);
//		result.add(a1);
//		return this.setMassimo;
//		
//	}
	
//	private void cerca(List<Album> parziale, int livello, double dTot, List<Album> tutti,double durataParziale) {
//		//condizione di terminazione
//		if(parziale.size()>dimMax) {
//			dimMax=parziale.size();
//			this.setMassimo=new HashSet<Album>(parziale);
//			return;
//		}
//		
//		for (Album a: tutti) {
//			if((livello==0)||(a.getAlbumId()>parziale.get(parziale.size()-1).getAlbumId() && durataParziale+a.getDurata()<=dTot)) {			
//				parziale.add(a);
//				cerca(parziale,livello+1,dTot,tutti,durataParziale+a.getDurata());
//				parziale.remove(parziale.size()-1);	
//			}	
//		}
//		
		
		
	}

	public double getDurata() {
		
		return this.durata;
	}
	
}
